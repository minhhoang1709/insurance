package com.ninelives.insurance.core.service;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ninelives.insurance.core.exception.AppBadRequestException;
import com.ninelives.insurance.core.exception.AppException;
import com.ninelives.insurance.core.mybatis.mapper.UserBeneficiaryMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserMapper;
import com.ninelives.insurance.model.User;
import com.ninelives.insurance.model.UserBeneficiary;
import com.ninelives.insurance.model.UserFile;
import com.ninelives.insurance.ref.ErrorCode;
import com.ninelives.insurance.ref.FileUseType;

@Service
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired UserMapper userMapper;
	@Autowired UserBeneficiaryMapper userBeneficiaryMapper;
	@Autowired FileUploadService fileUploadService;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	public User fetchByUserId(String userId){
		return userMapper.selectByUserId(userId);
	}
	
	public User fetchByEmail(String email){
		return userMapper.selectByEmail(email);
	}
	
	public User fetchByIdCardNumber(String idCardNumber){
		return userMapper.selectByIdCardNumber(idCardNumber);
	}
	
	public int updateProfileInfo(User user) {
		return userMapper.updateProfileAndConfigByUserIdSelective(user);
	}
	
	public int updatePassword(String userId, String password){
		return userMapper.updatePasswordByUserId(userId, DigestUtils.sha1Hex(password));
	}

	public int updatePhoneInfo(String userId, String phone) {
		return userMapper.updatePhoneByUserId(userId, phone);
	}
	
	public int updateSyncGmailEnabled(User user){
		return userMapper.updateSyncGmailEnabledByUserId(user);
	}
	
	public int insertUser(User user){
		return userMapper.insertSelective(user);
	}
	
	public UserBeneficiary fetchUserBeneficiaryByUserId(String userId){
		return userBeneficiaryMapper.selectByUserId(userId);
	}
	
	public int registerUserBeneficiary(UserBeneficiary userBeneficiary){
		return userBeneficiaryMapper.insert(userBeneficiary);
	}
	
	public int updateUserBeneficiaryFromOrder(UserBeneficiary userBeneficiary) {
		return userBeneficiaryMapper.updateByUserBeneficiaryId(userBeneficiary);
	}
	
	public boolean isPasswordEquals(User user, String password){
		if(user!=null && user.getPassword()!=null && password!=null){
			return user.getPassword().equals(DigestUtils.sha1Hex(password));
		}else{
			return false;
		}
	}
	
	public UserFile updatePassportFile(String userId, MultipartFile file) throws AppException {
		if(file==null){
			logger.debug("Update idcard for user {} with empty file", userId);
			throw new AppBadRequestException(ErrorCode.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, periksa kembali berkas yang akan Anda unggah");
		}
		logger.info("Update idcard for user {} with content-type {} and size {}", userId, file.getContentType(), file.getSize());
		
		UserFile userFile =  fileUploadService.save(userId, file, FileUseType.PASSPORT);
		if(userFile!=null && userFile.getFileId()!=null){
			userMapper.updatePassportFileIdByUserId(userId, userFile.getFileId());
		}
		return userFile;
	}
	
	public UserFile updateIdCardFile(String userId, MultipartFile file) throws AppException {
		if(file==null){
			logger.debug("Update idcard for user {} with empty file", userId);
			throw new AppBadRequestException(ErrorCode.ERR6001_UPLOAD_EMPTY, "Permintaan tidak dapat diproses, periksa kembali berkas yang akan Anda unggah");
		}
		logger.info("Update idcard for user {} with content-type {} and size {}", userId, file.getContentType(), file.getSize());
		
		UserFile userFile =  fileUploadService.save(userId, file, FileUseType.IDT);
		if(userFile!=null && userFile.getFileId()!=null){
			userMapper.updateIdCardFileIdByUserId(userId, userFile.getFileId());
		}
		return userFile;
	}
	
	
	public Boolean isUserProfileComplete(User user){
		boolean result = true;
		if(user == null 
				|| StringUtils.isEmpty(user.getName()) 
				|| user.getGender()==null
				|| user.getBirthDate()==null
				|| StringUtils.isEmpty(user.getBirthPlace())
				|| StringUtils.isEmpty(user.getPhone())
				){
			result = false;
		}
		return result;
	}

	public String generateUserId(){
		return UUID.randomUUID().toString().replace("-", "");
	}



}
