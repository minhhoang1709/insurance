package com.ninelives.insurance.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.service.UsersService;

@Controller
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired UsersService usersService;
	
	@RequestMapping(value="/login",
			method=RequestMethod.POST)	
	@ResponseBody
	public Map<String, String> login( @RequestBody Map<String, String> loginData, HttpServletResponse response){
		
		Users user = usersService.loginByEmail(loginData.get("email"), 
				loginData.get("password"), 
				loginData.get("googleFcmRegId"));		
		
		Map<String, String> result = null;
		
		if(user==null){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else{
			result = new HashMap<>();
			result.put("accessToken", String.valueOf(System.currentTimeMillis()));
		}
		
		logger.debug("Terima /login POST");
		if( loginData!=null&&loginData.size()>0 ){
			loginData.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
		}
		logger.debug("---");
		
		return result;
	}
	
	@RequestMapping(value="/logout")	
	@ResponseBody
	public void logout( @RequestBody(required=false) Map<String, String> loginData, HttpServletResponse response){
		
		logger.debug("Terima /logout GET");
		if( loginData!=null&&loginData.size()>0 ){
			loginData.forEach((k,v)->logger.info("Param : " + k + " | Value : " + v));
		}
		logger.debug("---");
				
	}
}
