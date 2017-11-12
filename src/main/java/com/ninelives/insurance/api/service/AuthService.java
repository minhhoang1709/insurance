package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.mybatis.mapper.UsersMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.api.ref.ErrorCode;

@Service
public class AuthService {
	@Autowired UsersMapper userMapper;
	@Autowired RedisService redisService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
	public AuthToken loginByEmail(String email, String password, String fcmToken) throws NotAuthorizedException{
		AuthToken token = null;
		
		if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)||StringUtils.isEmpty(fcmToken)){
			throw new NotAuthorizedException(ErrorCode.ERR2001_NOT_AUTHORIZED, "Wrong email or password");
		}
		
		Users user = userMapper.selectByEmail(email);
		if(!user.getPassword().equals(DigestUtils.sha1Hex(password))){
			throw new NotAuthorizedException(ErrorCode.ERR2001_NOT_AUTHORIZED, "Wrong email or password");
		}else{
			token = generateAuthToken(String.valueOf(user.getUserId()));			
			redisService.saveAuthToken(token);
			//test
			//redisService.touchAuthToken(token.getTokenId());
			if(!StringUtils.isEmpty(fcmToken) && !fcmToken.equals(user.getFcmToken())){
				userMapper.updateFcmTokenByUserId(user.getUserId(), fcmToken);
			}
		}

		return token;
	}
	
	private AuthToken generateAuthToken(String userId){
		AuthToken token = new AuthToken();
		token.setTokenId(generateTokenId());
		token.setUserId(userId);
		token.setCreatedDateTimeStr(LocalDateTime.now().format(formatter));		
		
		return token;
	}
	
	private String generateTokenId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
