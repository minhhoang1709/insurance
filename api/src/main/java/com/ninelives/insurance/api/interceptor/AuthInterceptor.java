package com.ninelives.insurance.api.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ninelives.insurance.api.model.ApiSessionData;
import com.ninelives.insurance.api.service.AuthService;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;
import com.ninelives.insurance.ref.ErrorCode;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{	
	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
	
	private static final String HEADER_AUTHENTICATION = "Authorization";
	
	@Autowired AuthService authService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(logger.isTraceEnabled()){
			logger.trace("cek interceptor {} {} {}", request.getRequestURL(), request.getMethod(), request.getRequestURI());
			
			Enumeration<String> e = request.getHeaderNames();
			while(e.hasMoreElements()){
				String header = e.nextElement();
				logger.trace("Header {}: {}", header, request.getHeader(header));
				
			}
			logger.trace("--- interceptor");
		}
		
		String tokenId = request.getHeader(HEADER_AUTHENTICATION);

		if(StringUtils.isEmpty(tokenId)){
			//allow POST to user without authentication
			if (request.getRequestURI().equals("/api/users")
					&& request.getMethod().equals(HttpMethod.POST.toString())) {
				return true;
			}else{
				throw new AppNotAuthorizedException(ErrorCode.ERR2002_NOT_AUTHORIZED, "Authentication is required");
			}
		}else{
			ApiSessionData sessionData = authService.validateAuthToken(tokenId);
			if(sessionData!=null){
				if(logger.isTraceEnabled()){
					logger.trace("sessionData {}", sessionData);
				}
				request.setAttribute(AuthService.AUTH_USER_ID, sessionData.getUserId());
			}else{
				return false;
			}
		}
		
		return true;
		
	}
}
