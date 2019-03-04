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

import com.ninelives.insurance.api.dto.ChangePasswordDto;
import com.ninelives.insurance.api.dto.PasswordResetDto;
import com.ninelives.insurance.api.dto.RegistrationDto;
import com.ninelives.insurance.api.dto.UserDto;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.service.ApiUserService;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired ApiUserService apiUserService;
	
	@RequestMapping(value="/users",
			method=RequestMethod.POST)
	@ResponseBody
	public UserDto registerUser( @RequestBody @Valid RegistrationDto registrationDto , HttpServletResponse response ) throws AppException{		
		
		logger.debug("Register with dto:<{}>", registrationDto);
		RegisterUsersResult registerResult = apiUserService.registerUser(registrationDto);
		
		if(registerResult!=null) {
			if(!registerResult.getIsNew()){
				response.setStatus(HttpStatus.CONFLICT.value());
			}
			
			return registerResult.getUserDto();
		}
		
		return null;
		
	}
	
	@RequestMapping(value="/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UserDto updateUsers(@RequestAttribute("authUserId") String authUserId, @PathVariable("userId") String userId,
			@RequestBody @Valid UserDto userDto) throws AppException {

		UserDto result = apiUserService.updateUser(authUserId, userDto);
		
		return result;
	}
	
	@RequestMapping(value="/users/{userId}",
			method=RequestMethod.GET)
	@ResponseBody
	public UserDto getUser( @RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId) throws AppException{		
		//logger.debug("register with {}", registrationDto);
		UserDto userDto = apiUserService.getUserDto(authUserId);
		
		return userDto;
	}
	
	@RequestMapping(value="/users/{userId}/idCardFiles",
			method=RequestMethod.PUT)
	@ResponseBody
	public UserFileDto updateIdCardFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws AppException{

		return apiUserService.updateIdCardFile(authUserId, file); 
	}
	
	@RequestMapping(value="/users/{userId}/passportFiles",
			method=RequestMethod.PUT)
	@ResponseBody
	public UserFileDto updatePassportFile (@RequestAttribute ("authUserId") String authUserId, 
			@RequestParam("file") MultipartFile file) throws AppException{

		return apiUserService.updatePassportFile(authUserId, file); 
	}
	
//	@RequestMapping(value="/users/{userId}/password",
//			method=RequestMethod.POST)
//	@ResponseBody
//	public void updatePassword( @RequestAttribute ("authUserId") String authUserId, @RequestBody ChangePasswordDto changePasswordDto) throws AppException{		
//		
//		if (changePasswordDto.getReset()!=null && changePasswordDto.getReset()) {
//			apiUserService.resetPassword(authUserId, changePasswordDto);
//		}else {
//			//update password
//		}
//	}
	@RequestMapping(value="/users/passwordReset",
			method=RequestMethod.POST)
	@ResponseBody
	public void resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto) throws AppException{		
		apiUserService.resetPassword(passwordResetDto);
	}
}
