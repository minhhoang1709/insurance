package com.ninelives.insurance.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.SignupVerificationService;

@Controller
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired NinelivesConfigProperties config;
	@Autowired SignupVerificationService signupVerificationService;
	
	
	@RequestMapping(value="/email/verify", 
			method=RequestMethod.GET)
	public String charge(HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam ("token") String token) {
		
		logger.debug("Token verify {} ", token);
		
		String returnUrl = "redirect:"+config.getAccount().getVerificationSuccessUrl();
		
		try {
			signupVerificationService.verifyEmail(token);
		} catch (AppException e) {
			returnUrl = "redirect:"+config.getAccount().getVerificationFailUrl();
		}
		
		return returnUrl;
		
	}
}
