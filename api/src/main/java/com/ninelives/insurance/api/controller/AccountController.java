package com.ninelives.insurance.api.controller;

import java.util.Locale;

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
import com.ninelives.insurance.core.service.LocaleService;
import com.ninelives.insurance.core.service.SignupVerificationService;

@Controller
public class AccountController {
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired NinelivesConfigProperties config;
	@Autowired SignupVerificationService signupVerificationService;
	@Autowired LocaleService localeService;
	
	
	@RequestMapping(value="/email/verify", 
			method=RequestMethod.GET)
	public String charge(HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam ("token") String token,
			@RequestParam ("lang") String language
			) {
		
		logger.debug("Token verify <{}> lang <{}>", token, language);
		
		//TODO: support locale instead in parameter instead of lang
		String defaultCountry = localeService.getDefaultLocale().getCountry();
		Locale parsedLocale = localeService.supportedLocale(language + "_"+ defaultCountry, localeService.getDefaultLocale());
		
		String returnUrl = String.format(config.getAccount().getVerificationSuccessUrl(), parsedLocale.getCountry(),
				parsedLocale.getLanguage());
		
		try {
			signupVerificationService.verifyEmail(token);
		} catch (AppException e) {
			returnUrl =  String.format(config.getAccount().getVerificationFailUrl(), parsedLocale.getCountry(),
					parsedLocale.getLanguage());
		}
		
		//System.out.println("return url string is " + returnUrl);
		
		return "redirect:" + returnUrl;
		
	}
}
