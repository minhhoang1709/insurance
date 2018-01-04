package com.ninelives.insurance.api;

import org.apache.camel.CamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.api.route.DirectEndPointRef;
import com.ninelives.insurance.route.EndPointRef;

@Component
public class NinelivesRouteBuilder extends SpringRouteBuilder{

	@Autowired
	CamelContext camelContext;
	
	@Override
	public void configure() throws Exception {
		//from(EndPointRef.QUEUE_FCM_NOTIFICATION).bean(fcmProcessor,"process");
		from(DirectEndPointRef.QUEUE_FCM_NOTIFICATION).marshal().json(JsonLibrary.Jackson).to(EndPointRef.QUEUE_FCM_NOTIFICATION);
	}
	
	
}
