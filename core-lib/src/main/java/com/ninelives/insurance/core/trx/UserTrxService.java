package com.ninelives.insurance.core.trx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.core.mybatis.mapper.UserMapper;
import com.ninelives.insurance.core.mybatis.mapper.UserTempPasswordMapper;
import com.ninelives.insurance.model.UserTempPassword;

@Service
public class UserTrxService {
	@Autowired UserTempPasswordMapper tempPasswordMapper;
	@Autowired UserMapper userMapper;
	
	@Transactional
	public void registerTempPassword(UserTempPassword tempPassword) {
		tempPasswordMapper.insert(tempPassword);
		userMapper.updateHasTempPasswordByUserId(tempPassword.getUserId(), true);
	}
	@Transactional
	public void updateTempPassword(UserTempPassword tempPassword) {
		tempPasswordMapper.updateByUserId(tempPassword);
		userMapper.updateHasTempPasswordByUserId(tempPassword.getUserId(), true);
	}
	
	@Transactional
	public void updatePasswordAndTempPasswordStatus(UserTempPassword tempPassword, String newPassword) {
		tempPasswordMapper.updateByUserIdSelective(tempPassword);
		userMapper.updatePasswordAndHasTempPasswordByUserId(tempPassword.getUserId(), newPassword, false);
	}
}
