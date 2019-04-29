package com.ninelives.insurance.insurer.processor;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.core.service.OrderService;
import com.ninelives.insurance.insurer.service.InsuranceService;
import com.ninelives.insurance.model.PolicyOrder;

@Component
public class SuccessPaymentProcessor {
	private static final Logger logger = LoggerFactory.getLogger(SuccessPaymentProcessor.class);
	
	@Autowired OrderService orderService;
	
	public void process(@Body PolicyOrder policyOrder){
		logger.debug("Receive success payment for insurer <{}>", policyOrder);
		try {
			orderService.paymentConfirm(policyOrder);
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
	
	public void processByOrderId(@Body String orderId){
		logger.debug("Receive success payment by orderId <{}>", orderId);		
		try {
			PolicyOrder policyOrder = orderService.fetchOrderByOrderId(orderId);
			orderService.paymentConfirm(policyOrder);
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
}
