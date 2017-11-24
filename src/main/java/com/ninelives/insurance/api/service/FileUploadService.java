package com.ninelives.insurance.api.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.dto.UserFileDto;
import com.ninelives.insurance.api.exception.ApiBadRequestException;
import com.ninelives.insurance.api.exception.ApiException;
import com.ninelives.insurance.api.exception.ApiInternalServerErrorException;
import com.ninelives.insurance.api.model.UserFile;
import com.ninelives.insurance.api.mybatis.mapper.UserFileMapper;
import com.ninelives.insurance.api.provider.storage.StorageException;
import com.ninelives.insurance.api.provider.storage.StorageProvider;
import com.ninelives.insurance.api.ref.ErrorCode;
import com.ninelives.insurance.api.ref.FileUseType;
import com.ninelives.insurance.api.ref.UserFileStatus;

@Service
public class FileUploadService {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
	
	@Autowired UserFileMapper userFileMapper;
	@Autowired StorageProvider storageProvider;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
	protected UserFile save(String userId, MultipartFile file, FileUseType fileUseType) throws ApiException{
		//TODO save id card should check allowed content type
		//check limit extension also?
		
		if(file==null||file.getSize()<=0){
			throw new ApiBadRequestException(ErrorCode.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, periksa kembali berkas yang akan Anda unggah");
		}
		
		LocalDate today = LocalDate.now();
		
		UserFile userFile = null;
		if(file!=null && file.getSize()>=0){
			userFile = new UserFile();
			userFile.setFileId(generateFileId());
			userFile.setUserId(userId);
			userFile.setContentType(file.getContentType());
			userFile.setFileSize(file.getSize());
			userFile.setFileUseType(fileUseType);
			userFile.setUploadDate(today);
			userFile.setStatus(UserFileStatus.UPLOADED);
			
			String directory = generateFileDirectoryPath(today, userId, fileUseType).toString();
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			if(!StringUtils.isEmpty(extension)){
				userFile.setFilePath(FilenameUtils.concat(directory, fileUseType+"-"+String.valueOf(userFile.getFileId()+"."+extension)));
			}else{
				userFile.setFilePath(FilenameUtils.concat(directory, fileUseType+"-"+String.valueOf(userFile.getFileId())));
			}
			
			try {
				storageProvider.store(file, userFile);
			} catch (StorageException e) {
				logger.error("Error saving file {} wiht message {}", userFile, e.getMessage());
				throw new ApiInternalServerErrorException(ErrorCode.ERR6002_UPLOAD_SYSTEM_ERROR, "Permintaan tidak dapat diproses, terjadi error pada sistem"); 
			}
			
			userFileMapper.insert(userFile);
		}
		return userFile;
	}
	
	
	private Path generateFileDirectoryPath(LocalDate today, String userId, FileUseType fileUseType){		
		Path path = Paths.get(fileUseType.toStr(), today.format(formatter), userId);
		return path;
	}
	
	protected UserFileDto userFileToDto(UserFile userFile) {
		UserFileDto dto = null;
		if(userFile!=null){
			dto = new UserFileDto(userFile.getFileId());			
		}
		return dto;
	}
	
	private Long generateFileId(){
		return userFileMapper.selectNextFileId();
	}
}
