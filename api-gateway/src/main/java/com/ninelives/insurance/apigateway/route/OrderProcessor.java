package com.ninelives.insurance.apigateway.route;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.apigateway.service.ApiOrderService;
import com.ninelives.insurance.model.PolicyOrder;

@Service
public class OrderProcessor {
	private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
	
	@Autowired ApiOrderService apiOrderService;

	public void process(@Body PolicyOrder order){
		try {
			apiOrderService.registerOrderForInviter(order);
		} catch (Exception e) {
			logger.error("Error on process register order for inviter: <"+order+">", e);
		}
	}
}
