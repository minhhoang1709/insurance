package com.ninelives.insurance.apigateway.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ninelives.insurance.apigateway.adapter.ModelMapperAdapter;
import com.ninelives.insurance.apigateway.dto.UserDto;
import com.ninelives.insurance.apigateway.model.AuthTokenCms;
import com.ninelives.insurance.apigateway.model.RegisterUsersResult;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.core.mybatis.mapper.UserCmsMapper;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserCms;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.UserRegisterChannel;
import com.ninelives.insurance.ref.UserSource;
import com.ninelives.insurance.ref.UserStatus;



@Service
public class ApiUserService {
	private static final Logger logger = LoggerFactory.getLogger(ApiUserService.class);
	
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = true;
	
	@Autowired 
	UserService userService;
		
	@Autowired 
	ModelMapperAdapter modelMapperAdapter;
	
	@Autowired 
	UserCmsMapper userCmsMapper;

	
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public RegisterUsersResult registerUserByWithoutEmailValidation(BatchFileUpload batchFileUpload) 
			throws AppException {
		
		logger.info("Start register, registration:<{}>", batchFileUpload);
		boolean isNew;
		User user = null;
		
		if(!StringUtils.isEmpty(batchFileUpload.getEmail())){
			user = userService.fetchByEmail(batchFileUpload.getEmail());
		}else{
			user = userService.fetchByIdCardNumber(batchFileUpload.getKtpNumber());
		}
		
		
		if(user!=null){
			isNew = false;
			if(user.getIdCardFileId()==null && StringUtils.isEmpty(user.getIdCardNo())){
					logger.debug("Process order for <{}> with result: id card no and id card file not found", user.getUserId());
					throw new AppBadRequestException(ErrorCode.ERR4017_ORDER_IDCARD_NOTFOUND,
							"Permintaan tidak dapat diproses, KTP dan ID Card file tidak ditemukan.");
			}
			
		}else{
			if(StringUtils.isEmpty(batchFileUpload.getKtpNumber())) {
				logger.debug("Process order for batch no <{}> with result: id card not found", batchFileUpload.getBatchNumber());
				throw new AppBadRequestException(ErrorCode.ERR4017_ORDER_IDCARD_NOTFOUND,
						"Permintaan tidak dapat diproses, unggah KTP Anda untuk melanjutkan pemesanan.");
			}
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate ldBirthDate = LocalDate.parse(batchFileUpload.getTanggalLahir(), formatter);

			user = new User();
			user.setUserId(userService.generateUserId());
			if(!StringUtils.isEmpty(batchFileUpload.getEmail())){
				user.setEmail(batchFileUpload.getEmail());
			}
			user.setPassword(DigestUtils.sha1Hex("password"));
			user.setIsNotificationEnabled(DEFAULT_IS_NOTIFICATION_ENABLED);
			user.setStatus(UserStatus.ACTIVE);
			user.setIdCardNo(batchFileUpload.getKtpNumber());
			user.setName(batchFileUpload.getNama());
			user.setBirthDate(ldBirthDate);
			user.setBirthPlace(batchFileUpload.getTempatLahir());
			user.setGender(Gender.toEnum(batchFileUpload.getJenisKelamin()));
			user.setPhone(batchFileUpload.getNoTelpon());
			user.setRegChannel(UserRegisterChannel.BATCH);
			user.setRegSource(UserSource.BATCH_B2B);
			userService.insertUser(user);

			isNew = true;
			logger.info("New register, registration:<{}>, user:<{}>", batchFileUpload, user);
		}
		
		UserDto userDto = modelMapperAdapter.toDto(user);
		
		RegisterUsersResult registerResult = new RegisterUsersResult();
		registerResult.setIsNew(isNew);
		registerResult.setUserDto(userDto);
		
		return registerResult;
	}	
	
	public AuthTokenCms loginCms(String userId, String password) throws AppNotAuthorizedException{		
		AuthTokenCms token = null;
		
		UserCms userCms = userCmsMapper.selectByUserId(userId);
		if(userCms==null || !userCms.getPassword().equals(DigestUtils.sha1Hex(password))){
			logger.info("Process login result:<{}>, reason:<Wrong password>, userId:<{}>", 
					ErrorCode.ERR2001_LOGIN_FAILURE, userId);
			throw new AppNotAuthorizedException(ErrorCode.ERR2001_LOGIN_FAILURE, "Wrong email or password");
		
		}else{
			token = generateAuthTokenCms();
			token.setUserCms(userCms);
			logger.info("Process login result:<Success>, reason:<>, userId:<{}>", userId);
		}

		return token;
	}
	
	private AuthTokenCms generateAuthTokenCms(){
		AuthTokenCms token = new AuthTokenCms();
		token.setTokenId(generateTokenId());
		token.setCreatedDateTimeStr(LocalDateTime.now().format(formatter));
		
		return token;
	}
	
	private String generateTokenId(){
		return UUID.randomUUID().toString().replace("-", "");
	}


	
}
