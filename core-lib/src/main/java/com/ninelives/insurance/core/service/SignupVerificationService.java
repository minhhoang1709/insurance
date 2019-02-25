package com.ninelives.insurance.core.service;

import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.SignupVerificationMapper;
import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.ref.SignupVerificationStatus;
import com.ninelives.insurance.ref.SignupVerificationType;

@Service
public class SignupVerificationService {
	private static final Logger logger = LoggerFactory.getLogger(SignupVerificationService.class);
	
	@Autowired NinelivesConfigProperties config;
	
	@Autowired EmailService emailService;
	@Autowired SignupVerificationMapper signupVerificationMapper;
	
	public SignupVerification fetchSignupVerificationByCodeAndType(String verificationCode, SignupVerificationType verificationType) {
		return signupVerificationMapper.selectByVerificationCodeAndType(verificationCode, verificationType);
	}
	
	public List<SignupVerification> fetchSignupVerificationByEmailAndType(String email, SignupVerificationType verificationType) {
		return signupVerificationMapper.selectByVerificationEmailAndType(email, verificationType);
	}
	
	public SignupVerification fetchActiveSignupVerificationByEmailAndType(String email,
			SignupVerificationType verificationType) {
		SignupVerification verification = signupVerificationMapper.selectByVerificationEmailAndTypeAndStatusAndPeriodInHour(email,
				verificationType, SignupVerificationStatus.ACTIVE, config.getAccount().getEmailVerificationActiveHours());
		return verification;	
	}

	public SignupVerification signupRequest(String email, String password) throws AppException{
		// TODO Auto-generated method stub
		////id, email, password, confirmation_code, confirmation_type, created_date, confirm_date, status
		String code = "email"+"-"+UUID.randomUUID().toString();
		
		SignupVerification signupVerification = new SignupVerification();
		signupVerification.setEmail(email);
		signupVerification.setPassword(DigestUtils.sha1Hex(password));
		signupVerification.setVerificationCode(code);
		signupVerification.setVerificationType(SignupVerificationType.EMAIL_LINK);
		signupVerification.setStatus(SignupVerificationStatus.ACTIVE);
		
		emailService.sendSignupEmail(signupVerification);
		
		signupVerificationMapper.insert(signupVerification);				
		
		return signupVerification;
	}
}
