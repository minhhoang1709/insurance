package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.dto.UsersDto;
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
	

	/**
	 * Check jika google valid, jika tidak maka return error
	 * Check jika users exists, jika iya maka di-update dan return users (perlu logging new/existing/..)
	 * Check jika users tidak exists, maka insert as new user
	 * 
	 * @param googleEmail
	 * @param googleId
	 * @param googleAuthCode
	 * @param googleIdToken
	 * @param name
	 * @param password
	 * @return
	 * @throws NotAuthorizedException
	 */
	public Users registerUserByGoogleAccount(String googleEmail, String googleId, String googleAuthCode,
			String googleIdToken, String name, String password) throws NotAuthorizedException {
		
		/**
		 * TODO: verify google login valid, if valid then continue, otherwise return login failure with error code google login not valid
		 */
		
		Users user = userMapper.selectByEmail(googleEmail);

		if(user!=null){
			/**
			 * TODO: whether to verify password or to replace, update the exception
			 */
			if(!user.getPassword().equals(DigestUtils.sha1Hex(password))){
				throw new NotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
			}
		}else{
			Users newUser = new Users();
			newUser.setUserId(generateUserId());
			newUser.setEmail(googleEmail);
			newUser.setPassword(DigestUtils.sha1Hex(password));
			newUser.setName(name);
			newUser.setGoogleAuthCode(googleAuthCode);
			newUser.setStatus("ACTIVE");
			// user.setGoogle
			userMapper.insert(newUser);
			
			user = new Users();
			user.setUserId(newUser.getUserId());
			user.setEmail(newUser.getEmail());
		}
		
		/**
		 * TODO: insert selective
		 */
		return user;
	}
	
	public UsersDto getUserDto(String userId) {
		Users users = userMapper.selectByUserId(userId);
		UsersDto usersDto = new UsersDto();
		usersDto.setUserId(users.getUserId());
		usersDto.setEmail(users.getEmail());
		usersDto.setName(users.getName());
		
		return usersDto;
	}
	
	private String generateUserId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
