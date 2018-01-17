package com.ninelives.insurance.api.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.service.UserService;

@Controller
@RequestMapping("/api")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired UserService userService;
	
	@RequestMapping(value="/users",
			method=RequestMethod.POST)
	@ResponseBody
	public UserDto registerUser( @RequestBody RegistrationDto registrationDto , HttpServletResponse response ) throws ApiBadRequestException{		
		logger.debug("Register with dto:<{}>", registrationDto);
		
		RegisterUsersResult registerResult = userService.registerUserByGoogleAccount(registrationDto);
		
		if(!registerResult.getIsNew()){
			response.setStatus(HttpStatus.CONFLICT.value());
		}		
		
		return registerResult.getUserDto();
	}
	
	@RequestMapping(value="/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers(@RequestAttribute("authUserId") String authUserId, @PathVariable("userId") String userId,
			@RequestBody @Valid UserDto userDto) throws ApiException {

		UserDto result = userService.updateUser(authUserId, userDto);
		
		return result;
	}
	
	@RequestMapping(value="/users/{userId}",
			method=RequestMethod.GET)
	@ResponseBody
	public UserDto getUser( @RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId) throws ApiException{		
		//logger.debug("register with {}", registrationDto);
		UserDto userDto = userService.getUserDto(authUserId);
		
		return userDto;
	}
	
	@RequestMapping(value="/users/{userId}/idCardFiles",
			method=RequestMethod.PUT)
	@ResponseBody
	public UserFileDto updateIdCardFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws ApiException{

		return userService.updateIdCardFile(authUserId, file); 
	}
	
//	@RequestMapping(value="/users/{userId}/idCardFiles",
//			method=RequestMethod.GET)
//	@ResponseBody
//	public UserFileDto downloadIdCardFile (@RequestAttribute ("authUserId") String authUserId) throws ApiException{
//
//		return userService.updateIdCardFile(authUserId, file); 
//	}
}
