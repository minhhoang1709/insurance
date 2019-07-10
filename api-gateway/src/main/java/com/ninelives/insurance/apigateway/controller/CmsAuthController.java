package com.ninelives.insurance.apigateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.LoginCmsDto;
import com.ninelives.insurance.apigateway.model.AuthTokenCms;
import com.ninelives.insurance.apigateway.service.ApiUserService;
import com.ninelives.insurance.core.exception.AppNotAuthorizedException;

@Controller
@RequestMapping("/api-gateway")
public class CmsAuthController {
	
	private static final Logger logger = LoggerFactory.getLogger(CmsAuthController.class);
	
	@Autowired 
	ApiUserService apiUserService;
	
	@RequestMapping(value="/login", method=RequestMethod.POST)	
	@ResponseBody
	public Map<String, String> login(
			@RequestBody LoginCmsDto loginCmsDto
			) throws AppNotAuthorizedException{
		
		AuthTokenCms authToken = apiUserService.loginCms(loginCmsDto.getUserId(), loginCmsDto.getPassword()); 
		Map<String, String> result = null;
		
		if(authToken!=null){
			result = new HashMap<>();
			result.put("accessToken", authToken.getTokenId());
			result.put("userId", authToken.getUserCms().getUserId());
			result.put("userName", authToken.getUserCms().getUserName());
			result.put("role", authToken.getUserCms().getRoleId());
			result.put("email", authToken.getUserCms().getEmail());
		}
		
		if(logger.isTraceEnabled()){
			logger.trace("Terima /login POST");
			if( loginCmsDto!=null ){
				logger.trace("userId : " + loginCmsDto.toString());
			}
			logger.trace("---");
		}
		
		return result;
	}

}
