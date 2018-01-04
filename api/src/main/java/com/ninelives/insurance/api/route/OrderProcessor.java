package com.ninelives.insurance.api.route;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.service.OrderService;
import com.ninelives.insurance.model.PolicyOrder;

@Service
public class OrderProcessor {
	private static final Logger logger = LoggerFactory.getLogger(OrderProcessor.class);
	
	@Autowired OrderService orderService;
	
//	public void process(@Body String order){
//		logger.debug("async process <{}>", order);
//	}
	public void process(@Body PolicyOrder order){
		try {
			orderService.registerOrderForInviter(order);
		} catch (Exception e) {
			logger.error("Error on process register order for inviter: <"+order+">", e);
		}
	}
}
