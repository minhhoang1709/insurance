package com.ninelives.insurance.core.provider.email;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.mybatis.mapper.EmailLogMapper;
import com.ninelives.insurance.core.util.GsonUtil;
import com.ninelives.insurance.model.EmailLog;
import com.ninelives.insurance.model.SignupVerification;
import com.ninelives.insurance.model.UserTempPassword;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Personalization;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class SendGridEmailProvider {
	private static final Logger logger = LoggerFactory.getLogger(SendGridEmailProvider.class);
	
	//private static int httpStatusOk = 200;
	
	@Autowired NinelivesConfigProperties config;
	@Autowired EmailLogMapper emailLogMapper;
	
	String bearerToken;
	Map<String, String> templateForVerification;
	Map<String, String> templateForPasswordReset;
	String emailFrom;
	
	String verificationLink;
	
	public void sendConfirmationEmail(SignupVerification signupVerification) throws EmailProviderException{
		Mail mail = new Mail();
		mail.setFrom(new Email(emailFrom));
		mail.setTemplateId(templateForVerification.get(signupVerification.getLanguageCode()));

		Personalization pern = new Personalization();
		pern.addDynamicTemplateData("verificationLink", String.format(config.getEmail().getSignupVerificationLink(),
				signupVerification.getVerificationToken(), signupVerification.getLanguageCode()));
		pern.addTo(new Email(signupVerification.getEmail()));

		mail.addPersonalization(pern);

		Response response = null;
		SendGrid sg = new SendGrid(bearerToken);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			response = sg.api(request);

		} catch (IOException ex) {
			logger.error("Error on send signup email request to sendgrid", ex);
			//throw new EmailProviderException(ex.getMessage());
			response = new Response();
			response.setStatusCode(-1);
			response.setBody(ex.getMessage());
		}
		
		//Insert log
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(signupVerification.getEmail());
		emailLog.setParam(GsonUtil.gson.toJson(mail));
		emailLog.setResponseHttpStatus((response==null?"-1":String.valueOf(response.getStatusCode())));
		emailLog.setResponseHttpBody(response==null?"":response.getBody());
		
		emailLogMapper.insert(emailLog);
		
		if (response == null || response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
			logger.error("Receiving non ok http status, response:<{}>, request:<{}>", response, signupVerification);			
			throw new EmailProviderException("Receiving non ok http status");
		}
		
	}
	
	public void sendPasswordResetEmail(UserTempPassword tempPassword, Locale locale) throws EmailProviderException{
		Mail mail = new Mail();
		mail.setFrom(new Email(emailFrom));
		mail.setTemplateId(templateForPasswordReset.get(locale.getLanguage()));

		Personalization pern = new Personalization();
		pern.addDynamicTemplateData("temporaryPassword", tempPassword.getClearTextPassword());
		pern.addTo(new Email(tempPassword.getEmail()));

		mail.addPersonalization(pern);

		Response response = null;
		SendGrid sg = new SendGrid(bearerToken);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			response = sg.api(request);

		} catch (IOException ex) {
			logger.error("Error on send password reset email request to sendgrid", ex);
			//throw new EmailProviderException(ex.getMessage());
			response = new Response();
			response.setStatusCode(-1);
			response.setBody(ex.getMessage());
		}
		
		//Insert log
		mail.getPersonalization().get(0).addDynamicTemplateData("temporaryPassword", "***"); //dont log the password		
		EmailLog emailLog = new EmailLog();
		emailLog.setEmail(tempPassword.getEmail());
		emailLog.setParam(GsonUtil.gson.toJson(mail));
		emailLog.setResponseHttpStatus((response==null?"-1":String.valueOf(response.getStatusCode())));
		emailLog.setResponseHttpBody(response==null?"":response.getBody());
		
		emailLogMapper.insert(emailLog);
		
		if (response == null || response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
			logger.error("Receiving non ok http status, response:<{}>, request:<{}>", response, tempPassword);
			throw new EmailProviderException("Receiving non ok http status");
		}
	}
	
	@PostConstruct
	private void init() {
		bearerToken = config.getEmail().getSendgridBearerToken();
		templateForVerification = config.getEmail().getSendgridTemplateForVerification();
		templateForPasswordReset = config.getEmail().getSendgridTemplateForPasswordReset();
		emailFrom = config.getEmail().getSendgridEmailFrom();
		verificationLink = config.getEmail().getSignupVerificationLink();
	}

	@Override
	public String toString() {
		return "SendGridEmailProvider [bearerToken=" + bearerToken + ", templateForVerification="
				+ templateForVerification + ", emailFrom=" + emailFrom + ", verificationLink=" + verificationLink + "]";
	}
	
}
