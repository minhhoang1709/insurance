package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.UserDetailDto;
import com.ninelives.insurance.apigateway.dto.UserManagementDto;
import com.ninelives.insurance.apigateway.dto.UserManagementListDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppException;

@Controller
@RequestMapping("/api-gateway")
public class UserManagementController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	
	@RequestMapping(value="/getUserList", method=RequestMethod.GET)	
	@ResponseBody
	public UserManagementListDto getAllUserList(){
		
		UserManagementListDto listDto = new UserManagementListDto();
		List<UserManagementDto> listUser = apiCmsService.getListUser();
		
		listDto.setiTotalRecords(listUser.size());
		listDto.setiTotalDisplayRecords(11);
		listDto.setsEcho(0);
		listDto.setsColumns("");
		listDto.setAaData(listUser);
		
		logger.info("ListUser : "+ listUser.toString());
		
		return listDto;
	}
	
	
	@RequestMapping(value="/updateUserDetail",method=RequestMethod.POST)
	@ResponseBody
	public UserDetailDto updateUserDetail(
			@RequestBody UserDetailDto userDto,
			HttpServletResponse response, 
			HttpServletRequest request ) throws AppException{		
		
		logger.debug("Update User dto:<{}>", userDto.toString());
		
		UserDetailDto rValue = apiCmsService.updateUserDetail(userDto);
		
		return rValue;
	
	}
	
}
