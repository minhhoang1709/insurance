package com.ninelives.insurance.notif;

import org.apache.camel.CamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.notif.processor.FcmProcessor;
import com.ninelives.insurance.provider.notification.message.FcmNotifMessageDto;
import com.ninelives.insurance.route.EndPointRef;

@Component
public class NinelivesNotifRouteBuilder extends SpringRouteBuilder{

	@Autowired
	CamelContext camelContext;
	
	@Autowired
	FcmProcessor fcmProcessor;
	
	//@Autowired
	//JmsComponent camelJmsComponent;
	
	//@Autowired
	//ActiveMQComponent activeMQComponent;
	 
//	@Bean
//	RoutesBuilder myRouter() {
//		return new RouteBuilder() {
//
//			@Override
//			public void configure() throws Exception {
//				from(EndPointRef.QUEUE_FCM_NOTIFICATION).to("file:/invoices");
//			}
//
//		};
//	}

	@Override
	public void configure() throws Exception {
		from(EndPointRef.QUEUE_FCM_NOTIFICATION).unmarshal().json(JsonLibrary.Jackson,FcmNotifMessageDto.class).bean(fcmProcessor,"process");
	}
	
	
}
