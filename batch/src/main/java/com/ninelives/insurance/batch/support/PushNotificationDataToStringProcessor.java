package com.ninelives.insurance.batch.support;

import org.springframework.batch.item.ItemProcessor;

import com.ninelives.insurance.batch.model.PushNotificationData;
import com.ninelives.insurance.batch.ref.PushNotificationType;

public class PushNotificationDataToStringProcessor implements ItemProcessor<PushNotificationData, String> {
	private static final String SEPARATOR=",";
	
	private PushNotificationType defaultPushNotificationType;
	
	public PushNotificationDataToStringProcessor(PushNotificationType defaultPushNotificationType){
		this.defaultPushNotificationType = defaultPushNotificationType;
	}
	
	@Override
	public String process(PushNotificationData item) throws Exception {
		return String.join(SEPARATOR, item.getUserId(), item.getEmail(), item.getFcmToken(), item.getCoverageCategoryId(),
				item.getCoverageCategoryName(), item.getOrderId(), item.getPushNotificationType()==null?defaultPushNotificationType.toString():item.getPushNotificationType().toString());
	}

	
}
