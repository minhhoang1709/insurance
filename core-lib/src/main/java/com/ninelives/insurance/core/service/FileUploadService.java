package com.ninelives.insurance.core.service;

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

import com.ninelives.insurance.core.config.NinelivesConfigProperties;
import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.exception.AppInternalServerErrorException;
import com.ninelives.insurance.core.mybatis.mapper.UserFileMapper;
import com.ninelives.insurance.core.provider.storage.StorageException;
import com.ninelives.insurance.core.provider.storage.StorageProvider;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.UserFileStatus;

@Service
public class FileUploadService {
	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
	
	@Autowired UserFileMapper userFileMapper;
	@Autowired StorageProvider storageProvider;
	@Autowired NinelivesConfigProperties config;
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
	public UserFile moveTemp(final UserFile userFileTemp, final FileUseType newFileUseType) throws AppException{
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
				throw new AppInternalServerErrorException(ErrorCode.ERR6002_UPLOAD_SYSTEM_ERROR, "Permintaan tidak dapat diproses, terjadi kesalahan pada sistem"); 
			}
			
			userFileMapper.updateUseTypeAndPathByFileId(userFileDst);
		}
		
		return userFileDst;
	}
	public UserFile save(String userId, MultipartFile file, FileUseType fileUseType) throws AppException{
		//TODO save id card should check allowed content type
		//check limit extension also?
		
		if(file==null||file.getSize()<=0){
			throw new AppBadRequestException(ErrorCode.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, periksa kembali berkas yang akan Anda unggah");
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
				throw new AppInternalServerErrorException(ErrorCode.ERR6002_UPLOAD_SYSTEM_ERROR, "Permintaan tidak dapat diproses, terjadi error pada sistem"); 
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
	
	public UserFile selectUserFileForPhotoByUserId(String userId){
		return userFileMapper.selectForPhotoByUserId(userId);
	}
	
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
		Path path = Paths.get(config.getStorage().getUserFilePath(), fileUseType.toStr(), today.format(formatter), userId);
		return path;
	}	
	
	
	private Long generateFileId(){
		return userFileMapper.selectNextFileId();
	}

	public UserFile fetchUserFileById(Long fileId) {
		return  userFileMapper.selectByPrimaryKey(fileId);
	}
	
	public UserFile featchUploadedTempFileById(Long fileId) {
		UserFile userFile = userFileMapper.selectByPrimaryKey(fileId);
		if (userFile != null && UserFileStatus.UPLOADED.equals(userFile.getStatus())
				&& FileUseType.TEMP.equals(userFile.getFileUseType())) {
			return userFile;
		}
		return null;
	}	
}
