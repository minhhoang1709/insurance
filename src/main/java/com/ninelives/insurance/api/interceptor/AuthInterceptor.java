package com.ninelives.insurance.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ninelives.insurance.api.service.AuthService;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	private static final String HEADER_AUTHENTICATION = "Authentication";
	
	@Autowired AuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.info("cek interceptor {}", request.getRequestURL());
		
		String tokenId = request.getHeader(HEADER_AUTHENTICATION);

		if(StringUtils.isEmpty(tokenId)){
			//allow POST to user without authentication
			if(request.getRequestURI().equals("/users") && request.getMethod().equals(HttpMethod.POST)){
				return true;
			}else{
				//AuthToken authToken = authService
			}
		}

		
		
		return true;
		
	}
}

/**
 * 
 * package id.co.xl.rbt.interceptor;


import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import id.co.xl.rbt.model.session.SignedMsisdnAuth;
import id.co.xl.rbt.service.AuthService;

@Component
public class MsisdnHeaderAuthInterceptor extends HandlerInterceptorAdapter{
	private static final Logger logger = LoggerFactory.getLogger(MsisdnHeaderAuthInterceptor.class);
	
	private static final String HEADER_MSISDN = "User-Identity-Forward-msisdn";
	private static final String COOKIE_AUTH_MSISDN_KEY = "m";
	private static final String COOKIE_AUTH_MSISDN_SEPARATOR = ";";

	@Autowired AuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String msisdnFromHeader = request.getHeader(HEADER_MSISDN);

		SignedMsisdnAuth signedMsisdnAuthToBeVerified = null;
		Cookie signedMsisdnAuthCookie = null;
		
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(COOKIE_AUTH_MSISDN_KEY)) {
					signedMsisdnAuthCookie = cookie;
					//if header msisdn is empty, try to check the cookie
					if (StringUtils.isEmpty(msisdnFromHeader)) {
						signedMsisdnAuthToBeVerified = new SignedMsisdnAuth();
						try {
							String myDecryptText = new String(Base64.getDecoder().decode(cookie.getValue()));
							if (!StringUtils.isEmpty(myDecryptText)) {
								String[] tokens = myDecryptText.split(COOKIE_AUTH_MSISDN_SEPARATOR);
								if (tokens != null && tokens.length == 3) {
									signedMsisdnAuthToBeVerified.setMsisdn(tokens[0]);
									signedMsisdnAuthToBeVerified.setTimestamp(Long.parseLong(tokens[1]));
									signedMsisdnAuthToBeVerified.setSign(tokens[2]);
								}
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		}		
	
		String msisdn = authService.validateMsisdn(request.getRemoteAddr(), msisdnFromHeader, signedMsisdnAuthToBeVerified);
		if(!StringUtils.isEmpty(msisdn)){
			request.getSession().setAttribute(AuthService.AUTH_MSISDN, msisdn);
			
			if(authService.isSignedMsisdnAuthNeedEviction(msisdn,signedMsisdnAuthToBeVerified)){
				SignedMsisdnAuth signedMsisdnAuth = authService.signMsisdn(msisdn);
				String signedMsisdn = signedMsisdnAuth.getMsisdn()+ COOKIE_AUTH_MSISDN_SEPARATOR
						+ signedMsisdnAuth.getTimestamp()+ COOKIE_AUTH_MSISDN_SEPARATOR
						+ signedMsisdnAuth.getSign();
				
				response.addCookie(
						new Cookie(COOKIE_AUTH_MSISDN_KEY, Base64.getEncoder().encodeToString(signedMsisdn.getBytes())));
				
				logger.debug("setting new cookie {}, old cookie is m={}", signedMsisdnAuth, signedMsisdnAuthCookie==null?"":signedMsisdnAuthCookie.getValue());
			}			
		}else{
			//msisdn is not valid, remove the cookie if it exists
			if(signedMsisdnAuthCookie != null){
				signedMsisdnAuthCookie.setValue("");
				signedMsisdnAuthCookie.setMaxAge(0);
				logger.debug("Remove old cookie from request ip:{} header:{} cookie:{}",request.getRemoteAddr(), msisdnFromHeader, signedMsisdnAuthToBeVerified);
			}
		}
//	logger.debug("testing {}", whitelistIps.getIps());
		
		
		
		
//		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date ());
//        String thiskey = h_msisdn+"|"+timestamp;
//        String Reshashkey = KeyEncode.getInstance().getValue(thiskey);
//        String encodeString = h_msisdn+"|"+timestamp+"|"+Reshashkey;
//
//        String myEncryptedText = Base64.getEncoder().encodeToString(encodeString.getBytes());
//        
//		Cookie cookie = new Cookie("crypt", myEncryptedText);
//		response.addCookie(cookie);
//		
//
//		byte[] decodedBytes = Base64.getDecoder().decode(crypt);
//		String myDecryptText = new String(decodedBytes);
//	    String[] splitMsisdn = myDecryptText.split("\\|");
//
//	    String logDate = splitMsisdn[1];
//	    Date ceklogDate;
//	    Date currDate;
//		try {
//			ceklogDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(logDate);
//			currDate = new Date();
//		    long diff = (currDate.getTime() - ceklogDate.getTime()) / (1000*60*60*24);
//		    
//		    if(diff <= 0){
//		        boolean cekUrl = KeyEncode.getInstance().cekValue(myDecryptText);
//		        if(cekUrl){
//		            logger.info("url valid");
//		        }else{
//		            logger.info("url not valid");
//		            diff = 999;
//		        }
//		    }
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return true;
	}
	
}

 */
