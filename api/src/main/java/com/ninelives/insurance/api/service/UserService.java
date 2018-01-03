package com.ninelives.insurance.api.service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.ProducerTemplate;
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
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.exception.ApiNotFoundException;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.mybatis.mapper.UserBeneficiaryMapper;
import com.ninelives.insurance.api.mybatis.mapper.UserMapper;
import com.ninelives.insurance.api.provider.redis.RedisService;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.provider.notification.message.FcmNotifMessageDto;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.UserStatus;
import com.ninelives.insurance.route.EndPointRef;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = false;
	
	@Autowired UserMapper userMapper;
	@Autowired UserBeneficiaryMapper userBeneficiaryMapper;
	@Autowired RedisService redisService;
	@Autowired FileUploadService fileUploadService;
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	@Autowired FluentProducerTemplate producerTemplate;
	
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
	 * @throws ApiNotAuthorizedException
	 */
	public RegisterUsersResult registerUserByGoogleAccount(RegistrationDto registrationDto) throws ApiBadRequestException {
		
		/**
		 * TODO: verify google login valid, if valid then continue, otherwise return login failure with error code google login not valid
		 * TODO: get access token and refresh token incase the isSyncGmailEnabled is true
		 */
			
		boolean isNew;
		
		User user = userMapper.selectByEmail(registrationDto.getGoogleEmail());
		
		//check google login valid
		//1. kalo dia select sycngmail true? ambil refresh token, authentication token
		//2. kalo gak select sync -> 
		
		if(user!=null){
			if(!user.getPassword().equals(DigestUtils.sha1Hex(registrationDto.getPassword()))){
				//throw new ApiBadRequestException(ErrorCode.ERR3002_REGISTER_PASSWORD_CONFLICT, "Register error, register token doesn't match existing user");
				userMapper.updatePasswordByUserId(user.getUserId(), DigestUtils.sha1Hex(registrationDto.getPassword()));
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
			user.setFcmToken(registrationDto.getFcmToken());
			user.setIsSyncGmailEnabled(registrationDto.getIsSyncGmailEnabled());
			user.setIsNotificationEnabled(DEFAULT_IS_NOTIFICATION_ENABLED);
			user.setStatus(UserStatus.ACTIVE);

			userMapper.insertSelective(user);

			isNew = true;
		}
		
		UserDto userDto = modelMapperAdapter.toDto(user);
		
		RegisterUsersResult registerResult = new RegisterUsersResult();
		registerResult.setIsNew(isNew);
		registerResult.setUserDto(userDto);
		
		if(isNew && !StringUtils.isEmpty(registrationDto.getFcmToken())){
			FcmNotifMessageDto.Notification notifMessage = new FcmNotifMessageDto.Notification();
			notifMessage.setTitle(messageSource.getMessage("message.notification.welcome.title", null, Locale.ROOT));
			notifMessage.setBody(messageSource.getMessage("message.notification.welcome.body", new Object[]{"9Lives"}, Locale.ROOT));
			
			FcmNotifMessageDto messageDto = new FcmNotifMessageDto();
			messageDto.setMessage(new FcmNotifMessageDto.Message());
			messageDto.getMessage().setToken(user.getFcmToken());
			messageDto.getMessage().setNotification(notifMessage);
			
			logger.debug("sending notif for new user <{}>", messageDto);
			producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		}
		
		return registerResult;
	}
	
	public User fetchByUserId(String userId){
		return userMapper.selectByUserId(userId);
	}
	
	public int updateProfileInfo(User user) {
		return userMapper.updateProfileByUserId(user);
	}

	public int updatePhoneInfo(String userId, String phone) {
		return userMapper.updatePhoneByUserId(userId, phone);
	}
	
	public UserBeneficiary fetchUserBeneficiaryByUserId(String userId){
		return userBeneficiaryMapper.selectByUserId(userId);
	}
	
	public int registerUserBeneficiary(UserBeneficiary userBeneficiary){
		return userBeneficiaryMapper.insert(userBeneficiary);
	}
	
	public int updateUserBeneficiaryFromOrder(UserBeneficiary userBeneficiary) {
		return userBeneficiaryMapper.updateByUserBeneficiaryId(userBeneficiary);
	}
	
	public UserFileDto updateIdCardFile(String userId, MultipartFile file) throws ApiException {
		if(file==null){
			logger.debug("Update idcard for user {} with empty file", userId);
			throw new ApiBadRequestException(ErrorCode.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, periksa kembali berkas yang akan Anda unggah");
		}
		logger.info("Update idcard for user {} with content-type {} and size {}", userId, file.getContentType(), file.getSize());
		
		UserFile userFile =  fileUploadService.save(userId, file, FileUseType.IDT);
		if(userFile!=null && userFile.getFileId()!=null){
			userMapper.updateIdCardFileIdByUserId(userId, userFile.getFileId());
		}
		return modelMapperAdapter.toDto(userFile);
	}
	
	/**
	 * If existing profile is not empty, user is only allowed to edit phone number or configuration
	 * 
	 * @param userId
	 * @param userDto
	 * @return
	 * @throws ApiException
	 */
	public UserDto updateUser(String userId, UserDto userDto) throws ApiException{
		logger.debug("Update user {} with dto {} ", userId, userDto);
		if(userDto==null){
			throw new ApiBadRequestException(ErrorCode.ERR2004_USER_EMPTY,
					"Permintaan tidak dapat diproses, periksa kembali data pribadi Anda");
		}
		User existingProfile = fetchByUserId(userId);
		
		if(existingProfile==null){
			throw new ApiNotFoundException(ErrorCode.ERR2003_USER_NOT_FOUND, "User not found");
		}
		
		User updateUser = new User();
		updateUser.setUserId(userId);
		if(!isUserProfileComplete(existingProfile)){
			//submitted profile has include name (means modify profile)
			if(!StringUtils.isEmpty(userDto.getName())){
				if(!isUserProfileComplete(userDto)){
					logger.error("Update profile {} with result: error profile not complete", userDto);
					throw new ApiBadRequestException(ErrorCode.ERR2005_USER_PROFILE_INVALID,
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
			updateUser.setPhone(userDto.getPhone());
			existingProfile.setPhone(userDto.getPhone());
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
		
		userMapper.updateProfileAndConfigByUserIdSelective(updateUser);
		
		return modelMapperAdapter.toDto(existingProfile);
	}

	public UserDto getUserDto(String userId) throws ApiNotFoundException {
		User user = userMapper.selectByUserId(userId);
		if(user==null){
			throw new ApiNotFoundException(ErrorCode.ERR2003_USER_NOT_FOUND, "User not found");
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
	protected Boolean isUserProfileComplete(User user){
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

	private String generateUserId(){
		return UUID.randomUUID().toString().replace("-", "");
	}



}
