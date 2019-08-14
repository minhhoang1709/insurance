package com.ninelives.insurance.insurer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.exception.AppNotFoundException;
import com.ninelives.insurance.core.service.FileUploadService;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;

@Service
public class InsurerPhotoService {
	@Autowired
	FileUploadService fileUploadService;
	
	
	public UserFile fetchIdtPhotoFile(String orderId) throws AppNotFoundException{
		UserFile userFile =  fileUploadService.selectIdtPhotoByOrderId(orderId);
		if(userFile == null){
			throw new AppNotFoundException(ErrorCode.ERR2006_USER_FILE_NOT_FOUND, "User file not found");
		}
		return userFile;
	}
	
	public UserFile fetchClaimPhotoFile(String claimId, String claimDocTypeId) throws AppNotFoundException{
		UserFile userFile =  fileUploadService.selectClaimPhotoByClaimIdAndClaimDocType(claimId, claimDocTypeId);
		if(userFile == null){
			throw new AppNotFoundException(ErrorCode.ERR7007_CLAIM_DOCUMENT_FILE_INVALID, "Claim doc file not found");
		}
		return userFile;
	}
}
