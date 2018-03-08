package com.ninelives.insurance.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.FileUseType;

@Service
public class ApiFileUploadService {
	@Autowired ModelMapperAdapter modelMapperAdapter;
	@Autowired FileUploadService fileUploadService;
	
	public UserFileDto saveTemp(String userId, MultipartFile file) throws AppException{
		UserFile userFile = fileUploadService.save(userId, file, FileUseType.TEMP);
		return modelMapperAdapter.toDto(userFile);
	}
}
