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
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.model.User;
import com.ninelives.insurance.api.mybatis.mapper.UserMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.UserStatus;

@Service
public class UserService {
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = false;
	
	@Autowired UserMapper userMapper;
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
	 * @throws ApiNotAuthorizedException
	 */
	public RegisterUsersResult registerUserByGoogleAccount(RegistrationDto registrationDto) throws ApiBadRequestException {
		
		/**
		 * TODO: verify google login valid, if valid then continue, otherwise return login failure with error code google login not valid
		 * TODO: get access token and refresh token incase the isSyncGmailEnabled is true
		 */
			
		boolean isNew;
		
		User user = userMapper.selectByEmail(registrationDto.getGoogleEmail());
		
		if(user!=null){
			if(!user.getPassword().equals(DigestUtils.sha1Hex(registrationDto.getPassword()))){
				throw new ApiBadRequestException(ErrorCode.ERR3002_REGISTER_PASSWORD_CONFLICT, "Register error, register token doesn't match existing user");
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
				throw new ApiBadRequestException(ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, "Register error, missing required parameter");
			}
			
			user = new User();
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
		
		UserDto userDto = new UserDto();
		userDto.setUserId(user.getUserId());
		userDto.setName(user.getName());
		userDto.setBirthDate(user.getBirthDate());
		userDto.setBirthPlace(user.getBirthPlace());
		userDto.setEmail(user.getEmail());
		userDto.setGender(user.getGender());
		userDto.setIdCardFileId(user.getIdCardFileId());
		userDto.setPhone(user.getPhone());
		userDto.setAddress(user.getAddress());
		
		Map<String, Object> configMap = new HashMap<>();
		configMap.put(UserDto.CONFIG_KEY_IS_NOTIFICATION_ENABLED, user.getIsNotificationEnabled());
		configMap.put(UserDto.CONFIG_KEY_IS_SYNC_GMAIL_ENABLED, user.getIsSyncGmailEnabled());
		userDto.setConfig(configMap);
		
		RegisterUsersResult registerResult = new RegisterUsersResult();
		registerResult.setIsNew(isNew);
		registerResult.setUserDto(userDto);
		
		
		return registerResult;
	}
	
	public User fetchUserByUserId(String userId){
		return userMapper.selectByUserId(userId);
	}
	
	//test
	public UserDto getUserDto(String userId) {
		User users = userMapper.selectByUserId(userId);
		UserDto userDto = new UserDto();
		userDto.setUserId(users.getUserId());
		userDto.setEmail(users.getEmail());
		userDto.setName(users.getName());
		
		return userDto;
	}
	
	private String generateUserId(){
		return UUID.randomUUID().toString().replace("-", "");
	}
}
