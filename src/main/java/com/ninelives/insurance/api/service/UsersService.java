package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UsersDto;
import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.exception.BadRequestException;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.mybatis.mapper.UsersMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.UserStatus;

@Service
public class UsersService {
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = false;
	
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
	public RegisterUsersResult registerUserByGoogleAccount(RegistrationDto registrationDto) throws BadRequestException {
		
		/**
		 * TODO: verify google login valid, if valid then continue, otherwise return login failure with error code google login not valid
		 * TODO: get access token and refresh token incase the isSyncGmailEnabled is true
		 */
			
		boolean isNew;
		
		Users user = userMapper.selectByEmail(registrationDto.getGoogleEmail());
		
		if(user!=null){
			if(!user.getPassword().equals(DigestUtils.sha1Hex(registrationDto.getPassword()))){
				throw new BadRequestException(ErrorCode.ERR3002_REGISTER_PASSWORD_CONFLICT, "Register error, register token doesn't match existing user");
			}
			if(user.getIsSyncGmailEnabled()!=registrationDto.getIsSyncGmailEnabled()){
				user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
				userMapper.updateSyncGmailEnabledByUserId(user);
			}
			isNew = false;
			
		}else{
			//validate field is valid
			if(StringUtils.isEmpty(registrationDto.getGoogleEmail())
					|| StringUtils.isEmpty(registrationDto.getGoogleId())
					|| StringUtils.isEmpty(registrationDto.getPassword())
					){
				throw new BadRequestException(ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, "Register error, missing required parameter");
			}
			
			user = new Users();
			user.setUserId(generateUserId());
			user.setEmail(registrationDto.getGoogleEmail());
			user.setPassword(DigestUtils.sha1Hex(registrationDto.getPassword()));
			user.setName(registrationDto.getGoogleName());
			user.setGoogleAuthCode(registrationDto.getGoogleServerAuth());
			user.setGoogleUserId(registrationDto.getGoogleId());
			user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
			user.setIsNotificationEnabled(DEFAULT_IS_NOTIFICATION_ENABLED);
			user.setStatus(UserStatus.ACTIVE);

			userMapper.insertSelective(user);

			isNew = true;
		}
		
		UsersDto usersDto = new UsersDto();
		usersDto.setUserId(user.getUserId());
		usersDto.setName(user.getName());
		usersDto.setBirthDate(user.getBirthDate());
		usersDto.setBirthPlace(user.getBirthPlace());
		usersDto.setEmail(user.getEmail());
		usersDto.setGender(user.getGender());
		usersDto.setIdCardFileId(user.getIdCardFileId());
		usersDto.setPhone(user.getPhone());
		usersDto.setAddress(user.getAddress());
		
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(UsersDto.CONFIG_KEY_IS_NOTIFICATION_ENABLED, user.getIsNotificationEnabled());
		configMap.put(UsersDto.CONFIG_KEY_IS_SYNC_GMAIL_ENABLED, user.getIsSyncGmailEnabled());
		usersDto.setConfig(configMap);
		
		RegisterUsersResult registerResult = new RegisterUsersResult();
		registerResult.setIsNew(isNew);
		registerResult.setUserDto(usersDto);
		
		
		return registerResult;
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
