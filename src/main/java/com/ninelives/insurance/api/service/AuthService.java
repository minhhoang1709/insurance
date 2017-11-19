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

import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.mybatis.mapper.UsersMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.api.ref.ErrorCode;

@Service
public class AuthService {
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	public static final String AUTH_USER_ID = "authUserId";
	
	@Autowired UsersMapper userMapper;
	@Autowired RedisService redisService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
	public AuthToken loginByEmail(String email, String password, String fcmToken) throws ApiNotAuthorizedException{
		AuthToken token = null;
		
		if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)||StringUtils.isEmpty(fcmToken)){
			throw new ApiNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
		}
		
		Users user = userMapper.selectByEmail(email);
		if(user==null || !user.getPassword().equals(DigestUtils.sha1Hex(password))){
			throw new ApiNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
		}else{
			token = generateAuthToken();
			
			ApiSessionData sessionData = new ApiSessionData();
			sessionData.setUserId(user.getUserId());
			sessionData.setTokenCreatedDateTimeStr(token.getCreatedDateTimeStr());
			
			redisService.saveAuthToken(token, sessionData);
			//test
			//redisService.touchAuthToken(token.getTokenId());
			if(!StringUtils.isEmpty(fcmToken) && !fcmToken.equals(user.getFcmToken())){
				userMapper.updateFcmTokenByUserId(user.getUserId(), fcmToken);
			}
		}

		return token;
	}
	
	public ApiSessionData validateAuthToken(String tokenId) throws ApiNotAuthorizedException{		
		if(StringUtils.isEmpty(tokenId)){
			throw new ApiNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
		}		
		ApiSessionData sessionData = redisService.getApiSessionData(tokenId);
		if(sessionData==null || StringUtils.isEmpty(sessionData.getUserId())){
			throw new ApiNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
		}		
		return sessionData;
	}
	
	public void logout(String tokenId) {
		redisService.deleteAuthToken(tokenId);
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
