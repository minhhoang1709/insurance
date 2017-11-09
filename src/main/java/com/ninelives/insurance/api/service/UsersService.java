package com.ninelives.insurance.api.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.model.Users;
import com.ninelives.insurance.api.mybatis.mapper.UsersMapper;

@Service
public class UsersService {
	@Autowired UsersMapper userMapper;
	
	private long getNextUserIdSeqId(){
		return userMapper.selectNextUserIdSeqId();
	}

	public Users registerUserByGoogleAccount(String googleEmail, String googleId, String googleAuthCode, String googleIdToken, String name, String password){
		Users user = new Users();
		user.setUserId(getNextUserIdSeqId());
		user.setEmail(googleEmail);
		user.setPassword(DigestUtils.sha1Hex(password));
		user.setName(name);
		user.setGoogleAuthCode(googleAuthCode);
		//user.setGoogle
		userMapper.insert(user);
		
		return user;
	}
	
	public Users loginByEmail(String email, String password, String googleFcmRegId){
		Users user = userMapper.selectByEmail(email);
		if(!user.getPassword().equals(DigestUtils.sha1Hex(password))){
			return null;
		}
		return user;
	}
}
