package com.ninelives.insurance.apigateway;

import org.apache.camel.CamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.apigateway.route.DirectEndPointRef;
import com.ninelives.insurance.apigateway.route.OrderProcessor;
import com.ninelives.insurance.route.EndPointRef;


public class NinelivesRouteBuilder extends SpringRouteBuilder {

	@Autowired
	CamelContext camelContext;

	@Autowired
	OrderProcessor orderProcessor;

	/**
	 * Configure routing
	 * Note: cannot get jackson to works with localdate, so use java serialize for order
	 */
	@Override
	public void configure() throws Exception {
		from(EndPointRef.QUEUE_ORDER).bean(orderProcessor,"process");

	}

}
