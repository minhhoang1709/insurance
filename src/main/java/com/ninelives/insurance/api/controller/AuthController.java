package com.ninelives.insurance.api.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.exception.NotFoundException;
import com.ninelives.insurance.api.model.AuthToken;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.service.AuthService;
import com.ninelives.insurance.api.service.UsersService;

@Controller
@SessionAttributes("authUserId")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired AuthService authService;
	
	@RequestMapping(value="/login",
			method=RequestMethod.POST)	
	@ResponseBody
	public Map<String, String> login( @RequestBody Map<String, String> loginData, HttpServletResponse response) throws NotAuthorizedException{
		
		AuthToken authToken = authService.loginByEmail(loginData.get("email"), 
				loginData.get("password"), 
				loginData.get("fcmToken"));
		
		Map<String, String> result = null;
		
		if(authToken!=null){
			result = new HashMap<>();
			result.put("accessToken", authToken.getTokenId());
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("Terima /login POST");
			if( loginData!=null&&loginData.size()>0 ){
				loginData.forEach((k,v)->logger.debug("Param : " + k + " | Value : " + v));
			}
			logger.debug("---");
		}
		
		return result;
	}
	
	@RequestMapping(value="/logout")	
	@ResponseBody
	public void logout(@RequestHeader("Authorization") String tokenId,
			HttpServletResponse response){
		logger.info("tokenid is {}", tokenId);

		authService.logout(tokenId);
				
	}
}
