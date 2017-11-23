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

import com.ninelives.insurance.api.model.UserFile;
import com.ninelives.insurance.api.mybatis.mapper.UserFileMapper;
import com.ninelives.insurance.api.provider.storage.StorageProvider;
import com.ninelives.insurance.api.ref.FileUseType;
import com.ninelives.insurance.api.ref.UserFileStatus;

@Service
public class FileUploadService {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
	
	@Autowired UserFileMapper userFileMapper;
	@Autowired StorageProvider storageProvider;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
	//TODO return fileobj
	public void saveIdCard(String userId, MultipartFile file) {
		save(userId, file, FileUseType.ID);
	}
	
	protected UserFile save(String userId, MultipartFile file, FileUseType fileUseType){
		//check limit content type allowed
		//check limit extension also?
		//v generate the id
		//v generate file obj
		//v get the contenttype
		//v save to file with info where to save
		//v storage service check if dir belom ada maka creaet?
		//mapper to save the fileobj
		//refactor, userservice ygn panggil fileuploadservice
		//update reference in user		
		//test
		//throw error 500, internal server error kalo fail to save
		//throw error 400, bad request kalo file nya null
		//return userfiledto to controller
		
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
			
			storageProvider.store(file, userFile);
			
			userFileMapper.insert(userFile);
		}
		return userFile;
	}
	
	private Path generateFileDirectoryPath(LocalDate today, String userId, FileUseType fileUseType){		
		Path path = Paths.get(fileUseType.toStr(), today.format(formatter), userId);
		return path;
	}
	
	private Long generateFileId(){
		return userFileMapper.selectNextFileId();
	}
}
