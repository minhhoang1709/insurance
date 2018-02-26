package com.ninelives.insurance.insurer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.insurer.mybatis.mapper.PolicyOrderMapper;
import com.ninelives.insurance.insurer.provider.insurance.AswataInsuranceProvider;
import com.ninelives.insurance.model.PolicyOrder;
import com.ninelives.insurance.provider.insurance.aswata.dto.PaymentConfirmResponseDto;
import com.ninelives.insurance.provider.insurance.aswata.dto.ResponseDto;
import com.ninelives.insurance.ref.PolicyStatus;

@Service
public class InsuranceService {
	private static final Logger logger = LoggerFactory.getLogger(InsuranceService.class);
	
	@Autowired AswataInsuranceProvider insuranceProvider;
	@Autowired PolicyOrderMapper policyOrderMapper;
	
	public void paymentConfirm(PolicyOrder policyOrder){
		logger.info("Process payment confirm, order:<{}>", policyOrder);
		ResponseDto<PaymentConfirmResponseDto>  result = insuranceProvider.paymentConfirm(policyOrder);
		if(insuranceProvider.isSuccess(result)){
			PolicyOrder updateOrder = new PolicyOrder();
			updateOrder.setOrderId(policyOrder.getOrderId());
			updateOrder.setStatus(PolicyStatus.APPROVED);
			if(result.getResponse().getResponseParam().getDownloadUrl()!=null){
				updateOrder.setProviderDownloadUrl(result.getResponse().getResponseParam().getDownloadUrl());
			}
			if(result.getResponse().getResponseParam().getPolicyNumber()!=null){
				updateOrder.setPolicyNumber(result.getResponse().getResponseParam().getPolicyNumber());
			}
			logger.debug("Update order status to approved, order:<{}>", updateOrder);
			policyOrderMapper.updateStatusAndProviderResponseByOrderIdSelective(updateOrder);
		}
		
	}
}
