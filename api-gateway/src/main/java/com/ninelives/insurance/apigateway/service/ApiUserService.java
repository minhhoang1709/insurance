package com.ninelives.insurance.apigateway.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.apigateway.adapter.ModelMapperAdapter;
import com.ninelives.insurance.apigateway.dto.UserDto;
import com.ninelives.insurance.apigateway.model.RegisterUsersResult;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.BatchFileUpload;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.UserStatus;

@Service
public class ApiUserService {
	private static final Logger logger = LoggerFactory.getLogger(ApiUserService.class);
	
	private static final boolean DEFAULT_IS_NOTIFICATION_ENABLED = true;
	
	@Autowired 
	UserService userService;
		
	@Autowired 
	ModelMapperAdapter modelMapperAdapter;
	
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public RegisterUsersResult registerUserByWithoutEmailValidation(BatchFileUpload batchFileUpload) 
			throws AppException {
		
		logger.info("Start register, registration:<{}>", batchFileUpload);
		boolean isNew;
		User user = userService.fetchByEmail(batchFileUpload.getEmail());
		
		if(user!=null){
			isNew = false;
		}else{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate ldBirthDate = LocalDate.parse(batchFileUpload.getTanggalLahir(), formatter);

			user = new User();
			user.setUserId(userService.generateUserId());
			user.setEmail(batchFileUpload.getEmail());
			user.setPassword(DigestUtils.sha1Hex("password"));
			user.setIsNotificationEnabled(DEFAULT_IS_NOTIFICATION_ENABLED);
			user.setStatus(UserStatus.ACTIVE);
			user.setIdCardNo(batchFileUpload.getKtpNumber());
			user.setName(batchFileUpload.getNama());
			user.setBirthDate(ldBirthDate);
			user.setBirthPlace(batchFileUpload.getTempatLahir());
			user.setGender(Gender.toEnum(batchFileUpload.getJenisKelamin()));
			user.setPhone(batchFileUpload.getNoTelpon());
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
	
}
