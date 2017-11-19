package com.ninelives.insurance.api.service.trx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ninelives.insurance.api.model.PolicyOrder;
import com.ninelives.insurance.api.mybatis.mapper.OrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderProductMapper;
import com.ninelives.insurance.api.mybatis.mapper.PolicyOrderUsersMapper;

@Service
public class PolicyOrderTrxService {
	private static final Logger logger = LoggerFactory.getLogger(PolicyOrderTrxService.class);
	
	@Autowired PolicyOrderMapper policyOrderMapper;
	@Autowired PolicyOrderUsersMapper policyOrderUserMapper;
	@Autowired PolicyOrderProductMapper policyOrderProductMapper; 
	@Autowired OrderProductMapper orderProductMapper;
	
	@Transactional
	public void registerPolicyOrder(PolicyOrder policyOrder){
		policyOrderMapper.insert(policyOrder);
		policyOrderUserMapper.insert(policyOrder.getPolicyOrderUsers());
		policyOrderProductMapper.insertList(policyOrder.getPolicyOrderProducts());
	}

}
