package com.ninelives.insurance.api.controller;

import java.util.Map;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.api.dto.UsersDto;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.service.UsersService;

@Controller
@Produces({ MediaType.APPLICATION_JSON })
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired UsersService usersService;
	
	@RequestMapping(value="/users",
			method=RequestMethod.POST)
	@ResponseBody
	public UsersDto registerUser( @RequestBody Map<String, String> registerData ){		
		Users user = usersService.registerUserByGoogleAccount(registerData.get("googleEmail"), 
				registerData.get("googleId"),
				registerData.get("googleAuthCode"),
				registerData.get("googleIdToken"),
				registerData.get("name"),
				registerData.get("password"));
		
		UsersDto result = new UsersDto();
		result.setEmail(user.getEmail());
		result.setUserId(user.getUserId());
		
		logger.debug("Terima /users POST");
		if( registerData!=null&&registerData.size()>0 ){
			registerData.forEach((k,v)->logger.debug("Param : " + k + " | Value : " + v));
		}
		logger.debug("---");
		
		return result;
	}
}
