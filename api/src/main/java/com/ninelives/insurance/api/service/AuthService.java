package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.LoginDto;
import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.mybatis.mapper.UserLoginMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserMapper;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserLogin;
import com.ninelives.insurance.ref.ErrorCode;

@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	public static final String AUTH_USER_ID = "authUserId";
	
	@Autowired UserMapper userMapper;
	@Autowired UserLoginMapper loginMapper;
	@Autowired RedisService redisService;
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	
	public LoginDto login(String email, String password, String fcmToken) throws AppNotAuthorizedException{	
		UserLogin login = loginByEmail(email, password, fcmToken);

		return modelMapperAdapter.toDto(login);
	}
	
	private UserLogin loginByEmail(String email, String password, String fcmToken) throws AppNotAuthorizedException{		
		UserLogin newLogin = null;
		
		if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)||StringUtils.isEmpty(fcmToken)){
			logger.info("Process login result:<{}>, reason:<Empty>, email:<{}>", ErrorCode.ERR2001_LOGIN_FAILURE, email);
			throw new AppNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
		}
		
		User user = userMapper.selectByEmail(email);
		if(user==null || !user.getPassword().equals(DigestUtils.sha1Hex(password))){
			logger.info("Process login result:<{}>, reason:<Wrong password>, email:<{}>", ErrorCode.ERR2001_LOGIN_FAILURE, email);
			throw new AppNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
		}else{
			AuthToken token = generateAuthToken();
			
			newLogin = new UserLogin();
			newLogin.setUserId(user.getUserId());
			newLogin.setTokenId(token.getTokenId());
			newLogin.setUser(user);
			
			UserLogin login = loginMapper.selectByUserId(user.getUserId());
			if(login!=null){
				redisService.deleteAuthToken(login.getTokenId());
				loginMapper.updateByUserId(newLogin);
			}else{
				loginMapper.insert(newLogin);
			}						
			
			ApiSessionData sessionData = new ApiSessionData();
			sessionData.setUserId(user.getUserId());
			sessionData.setTokenCreatedDateTimeStr(token.getCreatedDateTimeStr());
			
			redisService.saveAuthToken(token, sessionData);
			if(!StringUtils.isEmpty(fcmToken) && !fcmToken.equals(user.getFcmToken())){
				userMapper.updateFcmTokenByUserId(user.getUserId(), fcmToken);
			}
			logger.info("Process login result:<Success>, reason:<>, email:<{}>, userId:<{}>", email, user.getUserId());
		}

		return newLogin;
	}
//	public AuthToken loginByEmail(String email, String password, String fcmToken) throws AppNotAuthorizedException{		
//		AuthToken token = null;
//		
//		if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)||StringUtils.isEmpty(fcmToken)){
//			logger.info("Process login result:<{}>, reason:<Empty>, email:<{}>", ErrorCode.ERR2001_LOGIN_FAILURE, email);
//			throw new AppNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
//		}
//		
//		User user = userMapper.selectByEmail(email);
//		if(user==null || !user.getPassword().equals(DigestUtils.sha1Hex(password))){
//			logger.info("Process login result:<{}>, reason:<Wrong password>, email:<{}>", ErrorCode.ERR2001_LOGIN_FAILURE, email);
//			throw new AppNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
//		}else{
//			token = generateAuthToken();
//			
//			UserLogin newLogin = new UserLogin();
//			newLogin.setUserId(user.getUserId());
//			newLogin.setTokenId(token.getTokenId());
//			
//			UserLogin login = loginMapper.selectByUserId(user.getUserId());
//			if(login!=null){
//				redisService.deleteAuthToken(login.getTokenId());
//				loginMapper.updateByUserId(newLogin);
//			}else{
//				loginMapper.insert(newLogin);
//			}						
//			
//			ApiSessionData sessionData = new ApiSessionData();
//			sessionData.setUserId(user.getUserId());
//			sessionData.setTokenCreatedDateTimeStr(token.getCreatedDateTimeStr());
//			
//			redisService.saveAuthToken(token, sessionData);
//			if(!StringUtils.isEmpty(fcmToken) && !fcmToken.equals(user.getFcmToken())){
//				userMapper.updateFcmTokenByUserId(user.getUserId(), fcmToken);
//			}
//			logger.info("Process login result:<Success>, reason:<>, email:<{}>, userId:<{}>", email, user.getUserId());
//		}
//
//		return token;
//	}
	
	public ApiSessionData validateAuthToken(String tokenId) throws AppNotAuthorizedException{
		if(StringUtils.isEmpty(tokenId)){
			throw new AppNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
		}		
		final ApiSessionData sessionData = redisService.getApiSessionData(tokenId);
		if(sessionData==null || StringUtils.isEmpty(sessionData.getUserId())){
			UserLogin login = loginMapper.selectByTokenId(tokenId);
			if(login==null){
				throw new AppNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
			}
			String createdDateTimeStr = login.getCreatedDate().format(formatter);
			
			AuthToken newToken =  new AuthToken();
			newToken.setTokenId(login.getTokenId());
			newToken.setCreatedDateTimeStr(createdDateTimeStr);
			
			ApiSessionData newSessionData = new ApiSessionData();
			newSessionData.setUserId(login.getUserId());
			newSessionData.setTokenCreatedDateTimeStr(createdDateTimeStr);
			
			redisService.saveAuthToken(newToken, newSessionData);
			
			logger.info("Fetch session from db, login:<{}>",login);
			
			return newSessionData;
		}else{
			return sessionData;
		}
		
	}
	
	public void logout(String tokenId) {
		redisService.deleteAuthToken(tokenId);
		loginMapper.deleteByTokenId(tokenId);
	}
	
	private AuthToken generateAuthToken(){
		AuthToken token = new AuthToken();
		token.setTokenId(generateTokenId());
		token.setCreatedDateTimeStr(LocalDateTime.now().format(formatter));
		
		return token;
	}
	
	private String generateTokenId(){
		return UUID.randomUUID().toString().replace("-", "");
	}

	
}
