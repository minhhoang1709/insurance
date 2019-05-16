package com.ninelives.insurance.core.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

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
		
	public void replaceTempPassword(User user, String newPassword) {
		UserTempPassword tempPassword = user.getTempPassword();
		
		if(tempPassword==null) {
			tempPassword = fetchByUserId(user.getUserId());
		}
		
		if(tempPassword != null) {
			UserTempPassword updTempPassword = new UserTempPassword();
			updTempPassword.setUserId(tempPassword.getUserId());
			updTempPassword.setStatus(UserTempPasswordStatus.REPLACED);
			updTempPassword.setReplaceDate(LocalDateTime.now());

			userTrxService.updatePasswordAndTempPasswordStatus(updTempPassword, DigestUtils.sha1Hex(newPassword));
			
			log(tempPassword, updTempPassword);
					
//			UserTempPasswordLog log = new UserTempPasswordLog();
//			log.setEmail(tempPassword.getEmail());
//			log.setUserId(tempPassword.getUserId());
//			log.setPassword(tempPassword.getPassword());
//			log.setOldStatus(tempPassword.getStatus());
//			log.setNewStatus(updTempPassword.getStatus());
//			
//			tempPasswordLogMapper.insert(log);
		}

	}

	public void resetPassword(User user, Locale locale) throws AppException {	
		UserTempPassword oldTempPassword = user.getTempPassword();
		
		if(oldTempPassword==null) {
			oldTempPassword = fetchByUserId(user.getUserId());
		}
		
		UserTempPassword tempPassword = new UserTempPassword();
		tempPassword.setUserId(user.getUserId());
		tempPassword.setEmail(user.getEmail());
		tempPassword.setClearTextPassword(generateRandomPassword());
		tempPassword.setPassword(DigestUtils.sha1Hex(tempPassword.getClearTextPassword()));
		tempPassword.setStatus(UserTempPasswordStatus.REGISTERED);
		tempPassword.setRegisterDate(LocalDateTime.now());
		
		//send email
		emailService.sendPasswordResetEmail(tempPassword, locale);
		
		saveTempPassword(tempPassword, oldTempPassword!=null);
		
//		UserTempPasswordStatus oldStatus = null;
//		if(user.getHasTempPassword() && user.getTempPassword()!=null) {
//			oldStatus = user.getTempPassword().getStatus();
//		}
		
		log(oldTempPassword, tempPassword);
		
//		UserTempPasswordLog log = new UserTempPasswordLog();
//		log.setEmail(user.getEmail());
//		log.setUserId(user.getUserId());
//		log.setPassword(tempPassword.getPassword());
//		log.setOldStatus(oldStatus);
//		log.setNewStatus(tempPassword.getStatus());
//		
//		tempPasswordLogMapper.insert(log);

	}
	
	public void applyTempPassword(UserTempPassword tempPassword) {
		if(!tempPassword.getStatus().equals(UserTempPasswordStatus.APPLIED)) {
			UserTempPassword updTempPassword = new UserTempPassword();
			updTempPassword.setUserId(tempPassword.getUserId());
			updTempPassword.setStatus(UserTempPasswordStatus.APPLIED);
			updTempPassword.setApplyDate(LocalDateTime.now());
		
			tempPasswordMapper.updateByUserIdSelective(updTempPassword);
			
			log(tempPassword, updTempPassword);
			
//			UserTempPasswordLog log = new UserTempPasswordLog();
//			log.setEmail(tempPassword.getEmail());
//			log.setUserId(tempPassword.getUserId());
//			log.setPassword(tempPassword.getPassword());
//			log.setOldStatus(tempPassword.getStatus());
//			log.setNewStatus(updTempPassword.getStatus());
//			
//			tempPasswordLogMapper.insert(log);
		}		
	}
	
	private int log(UserTempPasswordStatus oldStatus, UserTempPassword newTempPassword) {		
		UserTempPasswordLog log = new UserTempPasswordLog();
		log.setEmail(newTempPassword.getEmail());
		log.setUserId(newTempPassword.getUserId());
		log.setPassword(newTempPassword.getPassword());
		log.setOldStatus(oldStatus);
		log.setNewStatus(newTempPassword.getStatus());
		
		return tempPasswordLogMapper.insert(log);
	}
	
	private int log(UserTempPassword oldTempPassword, UserTempPassword newTempPassword) {
		UserTempPasswordLog log = new UserTempPasswordLog();
		log.setEmail(newTempPassword.getEmail()!=null?newTempPassword.getEmail():oldTempPassword.getEmail());
		log.setUserId(newTempPassword.getUserId()!=null?newTempPassword.getUserId():oldTempPassword.getUserId());
		log.setPassword(newTempPassword.getPassword()!=null?newTempPassword.getPassword():oldTempPassword.getPassword());
		log.setOldStatus(oldTempPassword==null?null:oldTempPassword.getStatus());
		log.setNewStatus(newTempPassword.getStatus());
		
		return tempPasswordLogMapper.insert(log);
	}
	
	public boolean isValid(UserTempPassword tempPassword) {
		if(tempPassword.getStatus().equals(UserTempPasswordStatus.APPLIED)) {
			return true;
		}
		if(tempPassword.getStatus().equals(UserTempPasswordStatus.REPLACED)) {
			return false;
		}
		if(!isExpired(tempPassword)) {
			return true;
		}
		return false;
	}
	
	public boolean isExpired(UserTempPassword tempPassword) {
		if(tempPassword.getStatus().equals(UserTempPasswordStatus.EXPIRED)) {
			return true;
		}
		if (tempPassword.getStatus().equals(UserTempPasswordStatus.REGISTERED)
				&& ChronoUnit.HOURS.between(tempPassword.getRegisterDate(), LocalDateTime.now()) > config.getAccount()
						.getTemporaryPasswordValidHours()) {
			return true;
		}
		return false;
	}
	public void saveTempPassword(UserTempPassword tempPassword, boolean isOldTempPasswordExists) {
		if(isOldTempPasswordExists) {
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