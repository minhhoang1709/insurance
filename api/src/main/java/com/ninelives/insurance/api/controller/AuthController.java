package com.ninelives.insurance.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.LoginDto;
import com.ninelives.insurance.api.service.AuthService;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;

@Controller
@RequestMapping("/api")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired AuthService authService;
	
	@RequestMapping(value="/login",
			method=RequestMethod.POST)	
	@ResponseBody
	public LoginDto login( @RequestBody Map<String, String> loginData) throws AppNotAuthorizedException{
		LoginDto loginDto = authService.login(loginData.get("email"), 
				loginData.get("password"), 
				loginData.get("fcmToken"));
		
//		Map<String, String> result = null;
//		
//		if(authToken!=null){
//			result = new HashMap<>();
//			result.put("accessToken", authToken.getTokenId());
//		}
//		
//		if(logger.isTraceEnabled()){
//			logger.trace("Terima /login POST");
//			if( loginData!=null&&loginData.size()>0 ){
//				loginData.forEach((k,v)->logger.trace("Param : " + k + " | Value : " + v));
//			}
//			logger.trace("---");
//		}
		
		
		
		return loginDto;
	}
	
	@RequestMapping(value="/logout")	
	@ResponseBody
	public void logout(@RequestHeader("Authorization") String tokenId,
			HttpServletResponse response){
		logger.debug("tokenid is {}", tokenId);

		authService.logout(tokenId);				
	}
}
