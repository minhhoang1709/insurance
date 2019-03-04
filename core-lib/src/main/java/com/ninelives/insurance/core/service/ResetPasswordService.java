package com.ninelives.insurance.core.service;

import java.time.LocalDateTime;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.UserTempPasswordLogMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserTempPasswordMapper;
import com.ninelives.insurance.core.trx.UserTrxService;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserTempPassword;
import com.ninelives.insurance.model.UserTempPasswordLog;
import com.ninelives.insurance.ref.UserTempPasswordStatus;
import com.ninelives.insurance.util.RandomStringUtil;
import com.ninelives.insurance.util.RandomStringUtil.RANDOM_TYPE;

@Service
public class ResetPasswordService {
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired EmailService emailService;
	@Autowired UserTrxService userTrxService;
	@Autowired UserTempPasswordMapper tempPasswordMapper;
	@Autowired UserTempPasswordLogMapper tempPasswordLogMapper;
		
	public void resetPassword(User user) throws AppException {	
		System.out.println("reset password for "+user);
		
		UserTempPassword tempPassword = new UserTempPassword();
		tempPassword.setUserId(user.getUserId());
		tempPassword.setEmail(user.getEmail());
		tempPassword.setClearTextPassword(generateRandomPassword());
		tempPassword.setPassword(DigestUtils.sha1Hex(tempPassword.getClearTextPassword()));
		tempPassword.setStatus(UserTempPasswordStatus.REGISTERED);
		tempPassword.setRegisterDate(LocalDateTime.now());
		
		//send email
		emailService.sendPasswordResetEmail(tempPassword);
		
		setTempPassword(tempPassword, user.getHasTempPassword());
		
		UserTempPasswordStatus oldStatus = null;
		if(user.getHasTempPassword() && user.getTempPassword()!=null) {
			oldStatus = user.getTempPassword().getStatus();
		}
		
		UserTempPasswordLog log = new UserTempPasswordLog();
		log.setEmail(user.getEmail());
		log.setUserId(user.getUserId());
		log.setPassword(tempPassword.getPassword());
		log.setOldStatus(oldStatus);
		log.setNewStatus(tempPassword.getStatus());
		
		tempPasswordLogMapper.insert(log);

	}
	
	public void setTempPassword(UserTempPassword tempPassword, boolean isUserHasTempPassword) {
		if(isUserHasTempPassword) {
			userTrxService.updateTempPassword(tempPassword);		
		}else {
			userTrxService.registerTempPassword(tempPassword);
		}
	}
	
	public UserTempPassword fetchByUserId(String userId) {
		return tempPasswordMapper.selectByUserId(userId);
	}
	
	private String generateRandomPassword() {
		return RandomStringUtil.generate(config.getAccount().getTemporaryPasswordLength(), RANDOM_TYPE.TYPE_1);
	}
}
