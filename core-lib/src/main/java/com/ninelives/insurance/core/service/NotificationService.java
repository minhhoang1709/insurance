package com.ninelives.insurance.core.service;

import java.util.List;
import java.util.UUID;

import org.apache.camel.FluentProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ninelives.insurance.core.mybatis.mapper.UserNotificationMapper;
import com.ninelives.insurance.model.UserNotification;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.route.EndPointRef;

@Service
public class NotificationService {
	private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
		
	@Autowired UserNotificationMapper userNotificationMapper;
	@Autowired FluentProducerTemplate producerTemplate;
	
	public void sendFcmNotification(String userId, String fcmToken, FcmNotifMessageDto.Notification notifMessage) throws Exception{
		sendFcmNotification(userId, fcmToken, notifMessage, null, null);
	}
	
	public void sendFcmPushNotification(String userId, String fcmToken, FcmNotifMessageDto.Notification notifMessage) throws Exception{
		sendFcmPushNotification(userId, fcmToken, notifMessage, null, null);
	}
	
	public void sendFcmPushNotification(String userId, String fcmToken, FcmNotifMessageDto.Notification notifMessage, String action, String actionData) throws Exception{
		FcmNotifMessageDto messageDto = buildMessageDto(fcmToken, notifMessage, action, actionData);		
		logger.debug("sending push notif, message:<{}>", messageDto);
		
		//producerTemplate.to(EndPointRef.QUEUE_FCM_PUSH_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		
		sendFcmNotification(userId, messageDto, EndPointRef.QUEUE_FCM_PUSH_NOTIFICATION);
	}
	
	public void sendFcmNotification(String userId, String fcmToken, FcmNotifMessageDto.Notification notifMessage, String action, String actionData) throws Exception{
		FcmNotifMessageDto messageDto = buildMessageDto(fcmToken, notifMessage, action, actionData);
		logger.debug("sending notif, message:<{}>", messageDto);
		//producerTemplate.to(DirectEndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		//producerTemplate.to(EndPointRef.QUEUE_FCM_NOTIFICATION).withBodyAs(messageDto, FcmNotifMessageDto.class).send();
		sendFcmNotification(userId, messageDto, EndPointRef.QUEUE_FCM_NOTIFICATION);
	}
	
	public void sendFcmNotification(String userId, FcmNotifMessageDto messageDto, String endPoint) throws Exception{		
		UserNotification userNotif = new UserNotification();
		userNotif.setId(generateNotificationId());
		userNotif.setUserId(userId);	
		if(messageDto!=null  && messageDto.getMessage()!=null){
			if (messageDto.getMessage().getNotification()!=null){
				userNotif.setTitle(messageDto.getMessage().getNotification().getTitle());
				userNotif.setBody(messageDto.getMessage().getNotification().getBody());				
			}
			if(messageDto.getMessage().getData()!=null){
				userNotif.setAction(messageDto.getMessage().getData().getAction());
				userNotif.setActionData(messageDto.getMessage().getData().getActionData());			
			}
		}			
		userNotificationMapper.insertSelective(userNotif);
		
		producerTemplate.to(endPoint).withBodyAs(messageDto, FcmNotifMessageDto.class).send();	
	}
	
	private FcmNotifMessageDto buildMessageDto(String fcmToken, FcmNotifMessageDto.Notification notifMessage, String action, String actionData) throws Exception{
		if(fcmToken==null||notifMessage==null){
			throw new Exception("token and notification is required");
		}
		FcmNotifMessageDto messageDto = new FcmNotifMessageDto();
		messageDto.setMessage(new FcmNotifMessageDto.Message());
		messageDto.getMessage().setToken(fcmToken);
		messageDto.getMessage().setNotification(notifMessage);
			
		messageDto.getMessage().setData(new FcmNotifMessageDto.Data());
		messageDto.getMessage().getData().setTitle(notifMessage.getTitle());
		messageDto.getMessage().getData().setBody(notifMessage.getBody());
		
		if(action!=null){			
			messageDto.getMessage().getData().setAction(action);
			if(actionData!=null){
				messageDto.getMessage().getData().setActionData(actionData);
			}
		}
		return messageDto;
	}
	
	public List<UserNotification> fetchNotification(String userId, int limit, int offset){
		return userNotificationMapper.selectByUserId(userId, limit, offset);
	}
	
	public int insertNotification(UserNotification userNotification){
		return userNotificationMapper.insertSelective(userNotification);
	}
	
	private String generateNotificationId(){
		return UUID.randomUUID().toString();
	}
}
