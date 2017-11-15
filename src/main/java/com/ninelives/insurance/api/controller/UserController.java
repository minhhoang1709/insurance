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
import com.ninelives.insurance.api.dto.UsersDto;
import com.ninelives.insurance.api.exception.BadRequestException;
import com.ninelives.insurance.api.exception.NotAuthorizedException;
import com.ninelives.insurance.api.model.RegisterUsersResult;
import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.service.StorageService;
import com.ninelives.insurance.api.service.UsersService;

@Controller
@Produces({ MediaType.APPLICATION_JSON })
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired UsersService usersService;
	@Autowired StorageService storageService;
	
	@RequestMapping(value="/users",
			method=RequestMethod.POST)
	@ResponseBody
	public UsersDto registerUser( @RequestBody RegistrationDto registrationDto , HttpServletResponse response ) throws BadRequestException{
		
		//String registerSource = registerData.get("source");		
		//check jika users empty maka this is new, 
		logger.debug("register with {}", registrationDto);
		
		RegisterUsersResult registerResult = usersService.registerUserByGoogleAccount(registrationDto);
		
		if(!registerResult.getIsNew()){
			response.setStatus(HttpStatus.CONFLICT.value());
		}		
		
//		UsersDto result = new UsersDto();
//		//TODO some logic to check to if conflict case happen
//		if(!StringUtils.isEmpty(user.getStatus())){
//			result.setUserId(user.getUserId());
//			result.setEmail(user.getEmail());
//			result.setName(user.getName());
//			result.setPhone(user.getPhone());
//			
//		}else{
//			//result = new UsersDto();
//			result.setEmail(user.getEmail());
//			result.setUserId(user.getUserId());			
//		}
//		
//		
//		Map<String, Object> userConfig = new HashMap<>();
//		userConfig.put("isNotificationEnabled", new Boolean(true));
//		userConfig.put("isSyncGmailEnabled", new Boolean(true));
//		userConfig.put("string", "testvalue");
//		userConfig.put("number", new Integer(54));
//		
//		result.setConfig(userConfig);
//		
//		logger.debug("Terima /users POST");
//		if( registerData!=null&&registerData.size()>0 ){
//			registerData.forEach((k,v)->logger.debug("Param : " + k + " | Value : " + v));
//		}
//		logger.debug("---");
		
		return registerResult.getUserDto();
	}
	
	@RequestMapping(value="/users/{userId}",
			method={ RequestMethod.PATCH, RequestMethod.PUT })
	@ResponseBody
	public UsersDto updateUsers (@RequestAttribute ("authUserId") String authUserId, @PathVariable("userId") String userId, @RequestBody UsersDto usersDto){
		logger.debug("Terima /users PUT untuk authuser {} and user {}", authUserId, userId);
		logger.debug("put data: {}", usersDto);
		logger.debug("---");
		UsersDto result = usersService.getUserDto(authUserId);
		if(result==null){
			result = usersDto;
		}
		return result;
	}
	
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
