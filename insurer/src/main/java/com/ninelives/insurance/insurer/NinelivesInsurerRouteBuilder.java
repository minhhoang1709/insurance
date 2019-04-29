package com.ninelives.insurance.insurer;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.insurer.processor.SuccessPaymentProcessor;
import com.ninelives.insurance.route.EndPointRef;

@Component
public class NinelivesInsurerRouteBuilder extends SpringRouteBuilder{

	@Autowired
	CamelContext camelContext;
	
	@Autowired SuccessPaymentProcessor successPaymentProcessor;
	
	@Override
	public void configure() throws Exception {
		from(EndPointRef.QUEUE_SUCCESS_PAYMENT_TO_INSURER).bean(successPaymentProcessor,"process");
		from(EndPointRef.QUEUE_SUCCESS_PAYMENT_TO_INSURER_BY_ORDERID).bean(successPaymentProcessor,"processByOrderId");
	}
	
	
}
