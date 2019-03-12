package com.ninelives.insurance.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
import com.ninelives.insurance.api.dto.ChangePasswordDto;
import com.ninelives.insurance.api.dto.PasswordResetDto;
import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.provider.account.AccountProvider;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppConflictException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.core.service.ResetPasswordService;
import com.ninelives.insurance.core.service.SignupVerificationService;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserTempPassword;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.UserSource;
import com.ninelives.insurance.ref.SignupVerificationType;
import com.ninelives.insurance.ref.UserRegisterChannel;
import com.ninelives.insurance.ref.UserStatus;
import com.ninelives.insurance.util.ValidationUtil;

@Service
public class ApiUserService {
	private static final Logger logger = LoggerFactory.getLogger(ApiUserService.class);
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired UserService userService;
	@Autowired SignupVerificationService signupVerificationService;
	@Autowired ResetPasswordService resetPasswordService;
	@Autowired RedisService redisService;
	@Autowired AccountProvider accountProvider;
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Autowired NotificationService notificationService;
	
	@Autowired MessageSource messageSource;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		
	public RegisterUsersResult registerUser(RegistrationDto registrationDto) throws AppException {
		logger.info("Start register, registration:<{}>", registrationDto);
		
		if(registrationDto == null) {
			logger.error("Register with error:<{}>, registration:<{}>", ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, registrationDto);
			throw new AppBadRequestException(ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, "Register error, missing required parameter");
		}
		
		if(registrationDto.getSource().equals(UserSource.EMAIL)) {
			return registerUserByEmail(registrationDto);
		}
		else {
			return registerUserByGoogleAccount(registrationDto);
		}
	}
	/**
	 *	 
	 */
	private RegisterUsersResult registerUserByEmail(RegistrationDto registrationDto) throws AppException {
		logger.info("Start register by email, registration:<{}>", registrationDto);
		
		User user = userService.fetchByEmail(registrationDto.getEmail());
		
		if(user!=null && user.getIsEmailVerified()) {
			logger.error("Register by email, error:<User already registered>, exception:<{}>, registerDto:<{}>",
					ErrorCode.ERR3101_REGISTER_EMAIL_USER_EXISTS, registrationDto);
			throw new AppConflictException(ErrorCode.ERR3101_REGISTER_EMAIL_USER_EXISTS,
					"Maaf email ini sudah terdaftar.");
		}
		
		SignupVerification signUpVerification = signupVerificationService
				.fetchActiveSignupVerificationByEmailAndType(registrationDto.getEmail(), SignupVerificationType.EMAIL_LINK);
		
		if(signUpVerification!=null) {
			//there is signUpVerification that still unconfirmed
			logger.debug("Register by email, error:<last verification still unconfirmed>, exception:<{}>, registerDto:<{}>",
					ErrorCode.ERR3102_REGISTER_EMAIL_VERIFICATION_ACTIVE, registrationDto);
			throw new AppConflictException(ErrorCode.ERR3102_REGISTER_EMAIL_VERIFICATION_ACTIVE,
					"Maaf, kami sudah mengirimkan email verifikasi sebelum nya, silahkan cek email Anda.");
		}
		
		SignupVerification newSignUpVerification = signupVerificationService.signupRequest(registrationDto.getEmail(), 
				registrationDto.getPassword(), registrationDto.getFcmToken(), UserSource.EMAIL, UserRegisterChannel.ANDROID);
		
		RegisterUsersResult registerResult = null;
		if(newSignUpVerification!=null) {
			User pendingUser = new User();
			pendingUser.setEmail(registrationDto.getEmail());
			pendingUser.setIsNotificationEnabled(UserService.DEFAULT_IS_NOTIFICATION_ENABLED);
			pendingUser.setIsSyncGmailEnabled(false);		
			
			registerResult = new RegisterUsersResult();
			registerResult.setUserDto(modelMapperAdapter.toDto(pendingUser));
			registerResult.setIsNew(true);
		}
		
		return registerResult;
	}
	
		
	/**
	 * Check jika google valid, jika tidak maka return error
	 * Check jika users exists, jika iya maka di-update dan return users (perlu logging new/existing/..)
	 * Check jika users tidak exists, maka insert as new user
	 */
	private RegisterUsersResult registerUserByGoogleAccount(RegistrationDto registrationDto) throws AppException {
		
		/**
		 * TODO: get access token and refresh token incase the isSyncGmailEnabled is true
		 */
		logger.info("Start register by google-id, registration:<{}>", registrationDto);
		
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
		
		/*
		 *	If user exists and verified by google-id then allow password update, set isNew=false
		 *  If user exists and verified by other source then return ERROR
		 *  If user exists but not verified then update user, set as verified by google-id, set isNew=true
		 *  
		 */
		if(user!=null){
			if(user.getIsEmailVerified()) {
				if(UserSource.GOOGLE.equals(user.getVerifySource())) {
					if(!userService.isPasswordEquals(user, registrationDto.getPassword())){
						//throw new ApiBadRequestException(ErrorCode.ERR3002_REGISTER_PASSWORD_CONFLICT, "Register error, register token doesn't match existing user");
						userService.updatePassword(user.getUserId(), registrationDto.getPassword());
					}
					if(user.getIsSyncGmailEnabled()!=registrationDto.getIsSyncGmailEnabled()){
						user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
						userService.updateSyncGmailEnabled(user);
					}
					isNew = false;					
				} else {
					logger.error("Register with error:<{}>, registration:<{}>", ErrorCode.ERR3004_REGISTER_GOOGLE_USER_EXISTS, registrationDto);
					throw new AppConflictException(ErrorCode.ERR3004_REGISTER_GOOGLE_USER_EXISTS, "Maaf akun gmail ini sudah terdaftar, silahkan login menggunakan email anda"); 
				}				
			}else {				
				if (StringUtils.isEmpty(registrationDto.getGoogleId()) || StringUtils.isEmpty(registrationDto.getPassword())) {
					logger.error("Register with error:<{}>, registration:<{}>",
							ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER, registrationDto);
					throw new AppBadRequestException(ErrorCode.ERR3003_REGISTER_MISSING_PARAMETER,
							"Register error, missing required parameter");
				} 
				
				//User updateUser = new User();
				//updateUser.setUserId(user.getUserId());
				user.setPassword(DigestUtils.sha1Hex(registrationDto.getPassword()));
				user.setVerifyDate(LocalDateTime.now());
				user.setVerifySource(UserSource.GOOGLE);
				user.setIsEmailVerified(true);
				user.setGoogleName(registrationDto.getGoogleName());
				user.setGoogleAuthCode(registrationDto.getGoogleServerAuth());
				user.setGoogleUserId(registrationDto.getGoogleId());
				user.setFcmToken(registrationDto.getFcmToken());
				user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
				user.setIsNotificationEnabled(UserService.DEFAULT_IS_NOTIFICATION_ENABLED);
				user.setStatus(UserStatus.ACTIVE);
				
				userService.updateVerificationInfo(user);
				
				isNew = true;					
			}
		
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
			user.setRegSource(UserSource.GOOGLE);
			user.setRegChannel(UserRegisterChannel.ANDROID);
			user.setVerifySource(UserSource.GOOGLE);
			user.setVerifyDate(LocalDateTime.now());
			user.setIsEmailVerified(true);
			user.setUserId(userService.generateUserId());
			user.setEmail(registrationDto.getGoogleEmail());
			user.setPassword(DigestUtils.sha1Hex(registrationDto.getPassword()));
			user.setGoogleName(registrationDto.getGoogleName());
			user.setGoogleAuthCode(registrationDto.getGoogleServerAuth());
			user.setGoogleUserId(registrationDto.getGoogleId());
			user.setFcmToken(registrationDto.getFcmToken());
			user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
			user.setIsNotificationEnabled(UserService.DEFAULT_IS_NOTIFICATION_ENABLED);			
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
	
	public void resetPassword(PasswordResetDto passwordResetDto) throws AppException {		
		logger.info("Start password reset, passwordResetDto:<{}>", passwordResetDto);
		
		if(passwordResetDto == null || StringUtils.isEmpty(passwordResetDto.getEmail())) {
			logger.error("Reset passsword, email:<{}>, result:<error null parameter>, error:<{}>", "null",
					ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND);
			throw new AppNotFoundException(ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND, "Maaf email ini tidak terdaftar");
		}
		
		String email = passwordResetDto.getEmail();
		
		User user = userService.fetchByEmail(email);
		
		if(user==null) {
			logger.error("Reset passsword, email:<{}>, result:<error user not found>, error:<{}>", email,
					ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND);
			throw new AppNotFoundException(ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND, "Maaf email ini tidak terdaftar");
		}
		
		if(!user.getVerifySource().equals(UserSource.EMAIL)) {
			//only support Email based only
			logger.error("Reset passsword, email:<{}>, result:<error user is not from email>, error:<{}>", email,
					ErrorCode.ERR3302_RESET_PASSWORD_SOURCE_NOT_SUPPORTED);
			throw new AppNotFoundException(ErrorCode.ERR3302_RESET_PASSWORD_SOURCE_NOT_SUPPORTED,
					"Maaf, silahkan verifikasi email Anda terlebih dahulu sebelum menggunakan fitur lupa kata sandi");
		}
		
		if(user.getHasTempPassword()) {
			//user has temp password set, check the existing temp password
			UserTempPassword tempPassword = resetPasswordService.fetchByUserId(user.getUserId());
			//if still within 24 hours
			if(tempPassword!=null) {
				if (!resetPasswordService.isExpired(tempPassword)) {
					logger.debug("Reset password, error:<last temporary password still unused>, exception:<{}>, email:<{}>",
							ErrorCode.ERR3303_RESET_PASSWORD_EXISTS, email);
					throw new AppConflictException(ErrorCode.ERR3303_RESET_PASSWORD_EXISTS,
							"Maaf, kami sudah mengirimkan email password baru Anda sebelumnya, silahkan cek email Anda.");
				}
			}
			user.setTempPassword(tempPassword);			
		}
	
		resetPasswordService.resetPassword(user);
	}
	public void updatePassword(String authUserId, ChangePasswordDto changePasswordDto) throws AppException {
		logger.info("Start password update, user:<{}>", authUserId);
		
		if(changePasswordDto == null || StringUtils.isEmpty(changePasswordDto.getPassword())) {
			logger.error("Update passsword, userId:<{}>, result:<error null parameter>, error:<{}>", authUserId,
					ErrorCode.ERR3401_UPDATE_PASSWORD_INVALID);
			throw new AppBadRequestException(ErrorCode.ERR3401_UPDATE_PASSWORD_INVALID, "Masukan kata kunci");
		}
		
		User user = userService.fetchByUserId(authUserId);
		
		if(user==null) {
			logger.error("Reset passsword, userId:<{}>, result:<error user not found>, error:<{}>", authUserId,
					ErrorCode.ERR3402_UPDATE_PASSWORD_USER_NOT_FOUND);
			throw new AppNotFoundException(ErrorCode.ERR3402_UPDATE_PASSWORD_USER_NOT_FOUND, "User tidak ditemukan");
		}
		
		if(user.getHasTempPassword()) {
			resetPasswordService.replaceTempPassword(user, changePasswordDto.getPassword());
		}else {
			logger.error("Reset passsword, userId:<{}>, result:<error temp password not found>, error:<{}>", authUserId,
					ErrorCode.ERR3403_UPDATE_PASSWORD_TEMP_NOT_FOUND);
			throw new AppBadRequestException(ErrorCode.ERR3403_UPDATE_PASSWORD_TEMP_NOT_FOUND, "Tidak didukung");
		}
	}
	
//	public void resetPassword(String email) throws AppException {
//		User user = userService.fetchByEmail(email);
//		
//		if(user==null) {
//			logger.error("Reset passsword, email:<{}>, result:<error user not found>, error:<{}>", email,
//					ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND);
//			throw new AppNotFoundException(ErrorCode.ERR3301_RESET_PASSWORD_USER_NOT_FOUND, "Alamat email tidak ditemukan");
//		}
//		
//		if(!user.getVerifySource().equals(UserSource.EMAIL)) {
//			//only support Email based only
//			logger.error("Reset passsword, email:<{}>, result:<error user is not from email>, error:<{}>", email,
//					ErrorCode.ERR3302_RESET_PASSWORD_SOURCE_NOT_SUPPORTED);
//			throw new AppNotFoundException(ErrorCode.ERR3302_RESET_PASSWORD_SOURCE_NOT_SUPPORTED, "Alamat email tidak ditemukan");
//		}
//		
//		if(user.getHasTempPassword()) {
//			//user has temp password set, check the existing temp password
//			UserTempPassword tempPassword = resetPasswordService.fetchByUserId(user.getUserId());
//			//if still within 24 hours
//			if(tempPassword!=null) {
//				if (ChronoUnit.HOURS.between(tempPassword.getRegisterDate(), LocalDateTime.now()) <= config.getAccount()
//						.getTemporaryPasswordValidHours()) {
//					logger.debug("Reset password, error:<last temporary password still unused>, exception:<{}>, email:<{}>",
//							ErrorCode.ERR3303_RESET_PASSWORD_EXISTS, email);
//					throw new AppConflictException(ErrorCode.ERR3303_RESET_PASSWORD_EXISTS,
//							"Kami telah mengirimkan email untuk pembaruan kata kunci. Silakan periksa kembali kotak masuk email Anda.");
//				}
//			}
//		}
//	
//		resetPasswordService.resetPassword(user);
//	}
}
