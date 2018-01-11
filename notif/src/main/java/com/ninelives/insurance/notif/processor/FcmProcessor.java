package com.ninelives.insurance.notif.processor;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ninelives.insurance.notif.provider.notification.FcmProvider;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;

@Component
public class FcmProcessor {
	private static final Logger logger = LoggerFactory.getLogger(FcmProcessor.class);
	
	@Autowired FcmProvider fcmProvider;
	
//	public void process(@Body String messageDto){
//		logger.debug("Dapet nih message nya {}", messageDto);
//	}
	
	public void process(@Body FcmNotifMessageDto messageDto){
		//logger.debug("Dapet nih message nya {}", messageDto);
		try {
			fcmProvider.sendNotification(messageDto);
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
}
