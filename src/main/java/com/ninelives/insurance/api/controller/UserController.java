package com.ninelives.insurance.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiNotAuthorizedException;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.model.User;
import com.ninelives.insurance.api.service.StorageService;
import com.ninelives.insurance.api.service.UserService;

@Controller
@Produces({ MediaType.APPLICATION_JSON })
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired UserService userService;
	@Autowired StorageService storageService;
	
	@RequestMapping(value="/users",
			method=RequestMethod.POST)
	@ResponseBody
	public UserDto registerUser( @RequestBody RegistrationDto registrationDto , HttpServletResponse response ) throws ApiBadRequestException{
		
		//String registerSource = registerData.get("source");		
		//check jika users empty maka this is new, 
		logger.debug("register with {}", registrationDto);
				
		
		RegisterUsersResult registerResult = userService.registerUserByGoogleAccount(registrationDto);
		
		if(!registerResult.getIsNew()){
			response.setStatus(HttpStatus.CONFLICT.value());
		}		
		
		return registerResult.getUserDto();
	}
	//dummy
	@RequestMapping(value="/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers (@RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId, @RequestBody UserDto usersDto){
		logger.debug("Terima /users PUT untuk authuser {} and user {}", authUserId, userId);
		logger.debug("put data: {}", usersDto);
		logger.debug("---");
		UserDto result = userService.getUserDto(authUserId);
		if(result==null){
			result = usersDto;
		}
		return result;
	}
	
	//dummy
	@RequestMapping(value="/users/{userId}/idCardFiles",
			method=RequestMethod.PUT)
	@ResponseBody
	public Map<String, String> updateIdCardFile (@RequestParam("file") MultipartFile file){
		logger.debug("Terima /users PUT");
		logger.debug("---");
		
		storageService.store(file);
		
		Map<String, String> result = new HashMap<>();
		result.put("fileId", "12323123123");
		
		return result;
	}
}
