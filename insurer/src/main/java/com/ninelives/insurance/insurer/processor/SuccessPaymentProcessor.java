package com.ninelives.insurance.insurer.processor;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.insurer.service.InsuranceService;
import com.ninelives.insurance.model.PolicyOrder;

@Component
public class SuccessPaymentProcessor {
	private static final Logger logger = LoggerFactory.getLogger(SuccessPaymentProcessor.class);
	
	@Autowired InsuranceService insuranceService;
	
	public void process(@Body PolicyOrder policyOrder){
		logger.debug("Receive success payment for insurer <{}>", policyOrder);
		try {
			insuranceService.paymentConfirm(policyOrder);
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
}
