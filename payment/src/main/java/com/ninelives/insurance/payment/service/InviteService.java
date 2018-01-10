package com.ninelives.insurance.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.model.UserAggStat;
import com.ninelives.insurance.payment.NinelivesPaymentConfigProperties;
import com.ninelives.insurance.payment.mybatis.mapper.UserAggStatMapper;

@Service
public class InviteService {
	@Autowired UserAggStatMapper userAggStatMapper;
	@Autowired NinelivesPaymentConfigProperties config;
	
	public void updateUserAggStatOnSuccessPayment(PolicyOrder order){
		UserAggStat stat = userAggStatMapper.selectByUserId(order.getUserId());
		UserAggStat newStat = new UserAggStat();
		newStat.setUserId(order.getUserId());
		if(stat!=null && stat.getSuccessPaymentAmount()!=null){
			newStat.setSuccessPaymentAmount(order.getTotalPremi() + stat.getSuccessPaymentAmount());
			userAggStatMapper.updateByUserId(newStat);
		}else{
			newStat.setSuccessPaymentAmount(order.getTotalPremi());
			userAggStatMapper.insert(newStat);
		}
	}
}
