package com.ninelives.insurance.api.service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.provider.account.AccountProvider;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.UserStatus;
import com.ninelives.insurance.util.ValidationUtil;

@Service
public class ApiUserService {
	private static final Logger logger = LoggerFactory.getLogger(ApiUserService.class);
	
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = true;
	
	@Autowired UserService userService;
	@Autowired RedisService redisService;
	@Autowired AccountProvider accountProvider;
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Autowired NotificationService notificationService;
	
	@Autowired MessageSource messageSource;
	
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
	 * @throws AppNotAuthorizedException
	 */
	public RegisterUsersResult registerUserByGoogleAccount(RegistrationDto registrationDto) throws AppException {
		
		/**
		 * v verify google login valid, if valid then continue, otherwise return login failure with error code google login not valid
		 * TODO: get access token and refresh token incase the isSyncGmailEnabled is true
		 */
		logger.info("Start register, registration:<{}>", registrationDto);
		
		//verify google login
		String verifyEmail = accountProvider.verifyEmail(registrationDto);
		if(verifyEmail==null || !verifyEmail.equals(registrationDto.getGoogleEmail())){
			logger.error("Register, registration:<{}>, error:<Fail to verify email>, exception:<{}>, email:<{}>",
					registrationDto, ErrorCode.ERR3001_REGISTER_GOOGLE_FAIL, verifyEmail);
			throw new AppNotAuthorizedException(ErrorCode.ERR3001_REGISTER_GOOGLE_FAIL,
					"Maaf, terjadi kesalahan pada saat verifikasi email Anda");
		}
			
		boolean isNew;
		
		User user = userService.fetchByEmail(registrationDto.getGoogleEmail());
		
		//check google login valid
		//1. kalo dia select sycngmail true? ambil refresh token, authentication token
		//2. kalo gak select sync -> 
		
		if(user!=null){
			if(!userService.isPasswordEquals(user, registrationDto.getPassword())){
				//throw new ApiBadRequestException(ErrorCode.ERR3002_REGISTER_PASSWORD_CONFLICT, "Register error, register token doesn't match existing user");
				userService.updatePassword(user.getUserId(), registrationDto.getPassword());
			}
			if(user.getIsSyncGmailEnabled()!=registrationDto.getIsSyncGmailEnabled()){
				user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
				userService.updateSyncGmailEnabled(user);
			}
			isNew = false;
			
		}else{
			//validate field is valid
			if(StringUtils.isEmpty(registrationDto.getGoogleEmail())
					|| StringUtils.isEmpty(registrationDto.getGoogleId())
					|| StringUtils.isEmpty(registrationDto.getPassword())
					){
				logger.error("Register with error:<{}>, registration:<{}>", ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, registrationDto);
				throw new AppBadRequestException(ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, "Register error, missing required parameter");
			}
			
			user = new User();
			user.setUserId(userService.generateUserId());
			user.setEmail(registrationDto.getGoogleEmail());
			user.setPassword(DigestUtils.sha1Hex(registrationDto.getPassword()));
			user.setGoogleName(registrationDto.getGoogleName());
			user.setGoogleAuthCode(registrationDto.getGoogleServerAuth());
			user.setGoogleUserId(registrationDto.getGoogleId());
			user.setFcmToken(registrationDto.getFcmToken());
			user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
			user.setIsNotificationEnabled(DEFAULT_IS_NOTIFICATION_ENABLED);
			user.setStatus(UserStatus.ACTIVE);

			userService.insertUser(user);

			isNew = true;
			
			logger.info("New register, registration:<{}>, user:<{}>", registrationDto, user);
		}
		
		UserDto userDto = modelMapperAdapter.toDto(user);
		
		RegisterUsersResult registerResult = new RegisterUsersResult();
		registerResult.setIsNew(isNew);
		registerResult.setUserDto(userDto);
		
		if(isNew && !StringUtils.isEmpty(registrationDto.getFcmToken())){
			FcmNotifMessageDto.Notification notifMessage = new FcmNotifMessageDto.Notification();
			notifMessage.setTitle(messageSource.getMessage("message.notification.welcome.title", null, Locale.ROOT));
			notifMessage.setBody(messageSource.getMessage("message.notification.welcome.body", new Object[]{"9Lives"}, Locale.ROOT));
			
			try {
				notificationService.sendFcmNotification(user.getUserId(), user.getFcmToken(), notifMessage);
			} catch (Exception e) {
				logger.error("Failed to send message notif for register user",e);
			}
		}
		
		return registerResult;
	}
	
		
	public UserFileDto updateIdCardFile(String userId, MultipartFile file) throws AppException {
		return modelMapperAdapter.toDto(userService.updateIdCardFile(userId, file));
	}
	
	public UserFileDto updatePassportFile(String userId, MultipartFile file) throws AppException {
		return modelMapperAdapter.toDto(userService.updatePassportFile(userId, file));
	}
	
	/**
	 * If existing profile is not empty, user is only allowed to edit phone number or address or configuration
	 * 
	 * @param userId
	 * @param userDto
	 * @return
	 * @throws AppException
	 */
	public UserDto updateUser(String userId, UserDto userDto) throws AppException{
		logger.info("Update user profile, userId:<{}>, dto:<{}>", userId, userDto);
		if(userDto==null){
			throw new AppBadRequestException(ErrorCode.ERR2004_USER_EMPTY,
					"Permintaan tidak dapat diproses, periksa kembali data pribadi Anda");
		}
		User existingProfile = userService.fetchByUserId(userId);
		
		if(existingProfile==null){
			logger.error("Update profile:<{}>, result:<error user not found>, error:<{}>", userDto,
					ErrorCode.ERR2003_USER_NOT_FOUND);
			throw new AppNotFoundException(ErrorCode.ERR2003_USER_NOT_FOUND, "User not found");
		}
		
		User updateUser = new User();
		updateUser.setUserId(userId);
		if(!userService.isUserProfileComplete(existingProfile)){
			if(!StringUtils.isEmpty(userDto.getName())){
				//first time modifying profile, case submitted profile has include name (means modify full profile and not setting)
				if(!isUserProfileComplete(userDto)){
					logger.error("Update profile:<{}>, result:<error profile not complete>, error:<{}>", userDto,
							ErrorCode.ERR2005_USER_PROFILE_INVALID);
					throw new AppBadRequestException(ErrorCode.ERR2005_USER_PROFILE_INVALID,
							"Permintaan tidak dapat diproses, periksa kembali data pribadi Anda");
				}
				updateUser.setName(userDto.getName());
				updateUser.setGender(userDto.getGender());
				updateUser.setBirthDate(userDto.getBirthDate().toLocalDate());
				updateUser.setBirthPlace(userDto.getBirthPlace());
				updateUser.setAddress(userDto.getAddress());
				
				existingProfile.setName(userDto.getName());
				existingProfile.setGender(userDto.getGender());
				existingProfile.setBirthDate(userDto.getBirthDate().toLocalDate());
				existingProfile.setBirthPlace(userDto.getBirthPlace());
				existingProfile.setAddress(userDto.getAddress());
			}			
		}
		if(!StringUtils.isEmpty(userDto.getPhone()) 
				&& !userDto.getPhone().equals(existingProfile.getPhone())){
			
			String modifiedPhone = userDto.getPhone().replaceAll("\\D+", "");
			if(!ValidationUtil.isPhoneNumberValid(modifiedPhone)){
				logger.debug("Update profile:<{}> result:<phone invalid>, error:<{}>", userDto, ErrorCode.ERR4027_ORDER_PROFILE_PHONE_INVALID);
				throw new AppBadRequestException(ErrorCode.ERR4027_ORDER_PROFILE_PHONE_INVALID,
						"Periksa kembali nomor telepon Anda");
			}
			updateUser.setPhone(modifiedPhone);
			existingProfile.setPhone(modifiedPhone);
		}
		if(!StringUtils.isEmpty(userDto.getAddress()) 
				&& !userDto.getAddress().equals(existingProfile.getAddress())){
			updateUser.setAddress(userDto.getAddress());
			existingProfile.setAddress(userDto.getAddress());
		}
		if(!MapUtils.isEmpty(userDto.getConfig())){
			Boolean isNotifEnabledFromDto = (Boolean) userDto.getConfig().get(UserDto.CONFIG_KEY_IS_NOTIFICATION_ENABLED);
			Boolean isGmailSyncEnabledFromDto = (Boolean) userDto.getConfig().get(UserDto.CONFIG_KEY_IS_SYNC_GMAIL_ENABLED);
			if(isNotifEnabledFromDto!=null
					&& !isNotifEnabledFromDto.equals(existingProfile.getIsNotificationEnabled())){
				updateUser.setIsNotificationEnabled(isNotifEnabledFromDto);
				existingProfile.setIsNotificationEnabled(isNotifEnabledFromDto);
			}
			if(isGmailSyncEnabledFromDto!=null
					&& !isGmailSyncEnabledFromDto.equals(existingProfile.getIsSyncGmailEnabled())){
				updateUser.setIsSyncGmailEnabled(isGmailSyncEnabledFromDto);
				existingProfile.setIsSyncGmailEnabled(isGmailSyncEnabledFromDto);
			}
		}
		
		userService.updateProfileInfo(updateUser);
		
		return modelMapperAdapter.toDto(existingProfile);
	}

	public UserDto getUserDto(String userId) throws AppNotFoundException {
		User user = userService.fetchByUserId(userId);
		if(user==null){
			throw new AppNotFoundException(ErrorCode.ERR2003_USER_NOT_FOUND, "User not found");
		}
		return modelMapperAdapter.toDto(user);
	}

	protected Boolean isUserProfileComplete(UserDto user){
		boolean result = true;
		if(user == null 
				|| StringUtils.isEmpty(user.getName()) 
				|| user.getGender()==null
				|| user.getBirthDate()==null
				|| StringUtils.isEmpty(user.getBirthPlace())
				|| StringUtils.isEmpty(user.getPhone())
				){
			result = false;
		}
		return result;
	}
}
