package com.ninelives.insurance.api.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.api.adapter.ModelMapperAdapter;
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
	@Autowired ModelMapperAdapter modelMapperAdapter;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	
	public UserFileDto saveTemp(String userId, MultipartFile file) throws ApiException{
		UserFile userFile = save(userId, file, FileUseType.TEMP);
		return modelMapperAdapter.toDto(userFile);
	}
	
	public UserFile moveTemp(final UserFile userFileTemp, final FileUseType newFileUseType) throws ApiException{
		LocalDate today = LocalDate.now();
		
		UserFile userFileDst = null;
		
		if(userFileTemp!=null){
			userFileDst = new UserFile();
			userFileDst.setFileId(userFileTemp.getFileId());
			userFileDst.setUserId(userFileTemp.getUserId());
			userFileDst.setContentType(userFileTemp.getContentType());
			userFileDst.setFileSize(userFileTemp.getFileSize());
			userFileDst.setFileUseType(newFileUseType);
			userFileDst.setUploadDate(today);
			userFileDst.setStatus(userFileTemp.getStatus());
			
			setFilePath(userFileDst, FilenameUtils.getExtension(userFileTemp.getFilePath()));
			
			logger.debug("Move fileid {} from {} to {}", userFileTemp.getFileId(), userFileTemp.getFilePath(), userFileDst.getFilePath());
			
			try {
				storageProvider.move(userFileTemp, userFileDst);
			} catch (StorageException e) {
				logger.error("Error move file {} to {} with message {}", userFileTemp, userFileDst, e.getMessage());
				throw new ApiInternalServerErrorException(ErrorCode.ERR6002_UPLOAD_SYSTEM_ERROR, "Permintaan tidak dapat diproses, terjadi error pada sistem"); 
			}
			
			userFileMapper.updateUseTypeAndPathByFileId(userFileDst);
		}
		
		return userFileDst;
	}
	public UserFile save(String userId, MultipartFile file, FileUseType fileUseType) throws ApiException{
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
			
			setFilePath(userFile, FilenameUtils.getExtension(file.getOriginalFilename()));
			
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
//	public int countUploadedTempFile(String userId, List<Long> fileIds){
//		return userFileMapper.countByUserIdAndFileIdsAndStatusAndUseType(userId, fileIds, UserFileStatus.UPLOADED, FileUseType.TEMP);
//	}
	public List<UserFile> selectUploadedTempFile(String userId, List<Long> fileIds){
		return userFileMapper.selectByUserIdAndFileIdsAndStatusAndUseType(userId, fileIds, UserFileStatus.UPLOADED, FileUseType.TEMP);
	}
//	public int countUserFileByFileIdsAndStatus(String userId, List<Long> fileIds, UserFileStatus status){
//		return userFileMapper.countByUserIdAndFileIdsAndStatus(userId, fileIds, status);
//	}
	
	private void setFilePath(UserFile userFile, String fileExtension){
		String directory = generateFileDirectoryPath(userFile.getUploadDate(), userFile.getUserId(), userFile.getFileUseType()).toString();
		//String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if(!StringUtils.isEmpty(fileExtension)){
			userFile.setFilePath(FilenameUtils.concat(directory, userFile.getFileUseType()+"-"+String.valueOf(userFile.getFileId()+"."+fileExtension)));
		}else{
			userFile.setFilePath(FilenameUtils.concat(directory, userFile.getFileUseType()+"-"+String.valueOf(userFile.getFileId())));
		}
	}
	
	private Path generateFileDirectoryPath(LocalDate today, String userId, FileUseType fileUseType){		
		Path path = Paths.get(fileUseType.toStr(), today.format(formatter), userId);
		return path;
	}	
	
	
	private Long generateFileId(){
		return userFileMapper.selectNextFileId();
	}
}
