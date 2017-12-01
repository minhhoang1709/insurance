package com.ninelives.insurance.api.interceptor;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.service.AuthService;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	private static final String HEADER_AUTHENTICATION = "Authorization";
	
	Pattern paymentUrlPattern = Pattern.compile("/payment/.*");
	
	@Autowired AuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("cek interceptor {} {} {}", request.getRequestURL(), request.getMethod(), request.getRequestURI());
		if(logger.isDebugEnabled()){
			Enumeration<String> e = request.getHeaderNames();
			while(e.hasMoreElements()){
				String header = e.nextElement();
				logger.debug("Header {}: {}", header, request.getHeader(header));
				
			}
		}
		logger.debug("--- interceptor");
		
		String tokenId = request.getHeader(HEADER_AUTHENTICATION);

		// request.getRequestURI().equals("/payment/charge")
		//else if(paymentUrlPattern.matcher(request.getRequestURI()).matches()){
		//return true;
	
		if(StringUtils.isEmpty(tokenId)){
			//allow POST to user without authentication
			if ((request.getRequestURI().equals("/users") || request.getRequestURI().equals("/charge"))
					&& request.getMethod().equals(HttpMethod.POST.toString())) {
				return true;
			}else{
				throw new ApiNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
			}
		}else{
			ApiSessionData sessionData = authService.validateAuthToken(tokenId);
			if(sessionData!=null){
				logger.debug("sessionData {}", sessionData);
				request.setAttribute(AuthService.AUTH_USER_ID, sessionData.getUserId());
			}else{
				return false;
			}
		}
		
		return true;
		
	}
}
