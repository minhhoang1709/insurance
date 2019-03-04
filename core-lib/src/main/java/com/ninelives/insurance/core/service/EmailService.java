package com.ninelives.insurance.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.provider.email.EmailProviderException;
import com.ninelives.insurance.core.provider.email.SendGridEmailProvider;
import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.model.UserTempPassword;
import com.ninelives.insurance.ref.ErrorCode;

@Service
public class EmailService {
	@Autowired SendGridEmailProvider emailProvider;
	
	public void sendSignupEmail(SignupVerification signupVerification) throws AppInternalServerErrorException{
		try {
			emailProvider.sendConfirmationEmail(signupVerification);
		} catch (EmailProviderException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new AppInternalServerErrorException(ErrorCode.ERR8401_EMAIL_ERROR, "Error on sending email signup");
		}
	}
	
	public void sendPasswordResetEmail(UserTempPassword tempPassword) throws AppInternalServerErrorException{
		try {
			emailProvider.sendPasswordResetEmail(tempPassword);
		} catch (EmailProviderException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new AppInternalServerErrorException(ErrorCode.ERR8401_EMAIL_ERROR, "Error on sending email signup");
		}
	}
}
