package com.ninelives.insurance.api.service;

import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.api.route.DirectEndPointRef;
import com.ninelives.insurance.provider.notification.message.FcmNotifMessageDto;

@Service
public class NotificationService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
	
	@Autowired FluentProducerTemplate producerTemplate;
	
	public void sendFcmNotification(String fcmToken, FcmNotifMessageDto.Notification notifMessage) throws Exception{
		sendFcmNotification(fcmToken, notifMessage, null, null);
	}
	public void sendFcmNotification(String fcmToken, FcmNotifMessageDto.Notification notifMessage, String action, String actionData) throws Exception{
		if(fcmToken==null||notifMessage==null){
			throw new Exception("token and notification is required");
		}
		FcmNotifMessageDto messageDto = new FcmNotifMessageDto();
		messageDto.setMessage(new FcmNotifMessageDto.Message());
		messageDto.getMessage().setToken(fcmToken);
		messageDto.getMessage().setNotification(notifMessage);
				
		if(action!=null){
			messageDto.getMessage().setData(new FcmNotifMessageDto.Data());
			messageDto.getMessage().getData().setAction(action);
			if(actionData!=null){
				messageDto.getMessage().getData().setActionData(actionData);
			}
		}	
		logger.debug("sending notif for active order <{}>", messageDto);
		producerTemplate.to(DirectEndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
	}
}
