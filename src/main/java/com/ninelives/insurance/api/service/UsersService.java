package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.mybatis.mapper.UsersMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.api.ref.ErrorCode;

@Service
public class UsersService {
	@Autowired UsersMapper userMapper;
	@Autowired RedisService redisService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	

	public Users registerUserByGoogleAccount(String googleEmail, String googleId, String googleAuthCode,
			String googleIdToken, String name, String password) {
		Users user = new Users();
		user.setUserId(generateUserId());
		user.setEmail(googleEmail);
		user.setPassword(DigestUtils.sha1Hex(password));
		user.setName(name);
		user.setGoogleAuthCode(googleAuthCode);
		// user.setGoogle
		userMapper.insert(user);
		/**
		 * TODO: insert selective
		 */
		return user;
	}
	
	

	

	
	private String generateUserId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
