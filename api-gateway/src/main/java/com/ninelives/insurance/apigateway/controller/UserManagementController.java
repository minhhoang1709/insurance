package com.ninelives.insurance.apigateway.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ninelives.insurance.apigateway.dto.UserDetailDto;
import com.ninelives.insurance.apigateway.dto.UserManagementDto;
import com.ninelives.insurance.apigateway.dto.UserManagementListDto;
import com.ninelives.insurance.apigateway.service.ApiCmsService;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.core.service.UserService;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;

@Controller
@RequestMapping("/api-gateway")
public class UserManagementController extends AbstractWebServiceStatusImpl {
	
	private static final Logger logger = LoggerFactory.getLogger(UserManagementController.class);
	
	@Autowired 
	ApiCmsService apiCmsService;
	
	@Autowired 
	UserService userService;
	
	@Autowired 
	StorageProvider storageService;
	
	
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
	
	@RequestMapping(value="/users/{userId}/photoFiles", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Resource>  downloadPhotoFile (
			@RequestAttribute ("authUserId") String authUserId, 
			@PathVariable("userId") String userId) throws AppException{

		checkUserIdFromPath(userId, authUserId);
		
		UserFile userFile = userService.fetchPhotoFile(userId);
		
		try {
			Resource file = storageService.loadAsResource(userFile.getFilePath());
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + userFile.getFileId() + "\"")
					.header(HttpHeaders.CONTENT_TYPE, userFile.getContentType()).body(file);
		} catch (StorageException e) {
			logger.error("Error on download photo", e);
			throw new AppInternalServerErrorException(ErrorCode.ERR1002_STORAGE_ERROR, "Maaf permintaan Anda belum dapat dilayani, terjadi kesalahan pada sistem");
		}		 
	}
	
	private void checkUserIdFromPath(String userId, String authUserId) throws AppBadRequestException {
		if(!StringUtils.equals(userId, authUserId)){
			throw new AppBadRequestException(ErrorCode.ERR1001_GENERIC_ERROR, "Invalid userId");
		}
	}
	
}
