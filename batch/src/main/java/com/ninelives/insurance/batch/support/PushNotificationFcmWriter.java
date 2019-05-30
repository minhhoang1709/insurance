package com.ninelives.insurance.batch.support;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.MessageSource;

import com.ninelives.insurance.batch.model.PushNotificationData;
import com.ninelives.insurance.core.service.LocaleService;
import com.ninelives.insurance.core.service.NotificationService;
import com.ninelives.insurance.provider.notification.fcm.dto.FcmNotifMessageDto;
import com.ninelives.insurance.provider.notification.fcm.ref.FcmNotifAction;

public class PushNotificationFcmWriter implements ItemWriter<PushNotificationData> {
	private static final Logger logger = LoggerFactory.getLogger(PushNotificationFcmWriter.class);
	
	private static final String PREFIX_MESSAGE_TITLE = "message.notification.push.title.";
	private static final String PREFIX_MESSAGE_BODY = "message.notification.push.body.";
	
	private NotificationService notificationService;
	private MessageSource messageSource;
	private LocaleService localeService;

	public PushNotificationFcmWriter(NotificationService notificationService, MessageSource messageSource,
			LocaleService localeService) {
        this.notificationService = notificationService;  
        this.messageSource = messageSource;
        this.localeService = localeService;
    }

    @Override
	public void write(List<? extends PushNotificationData> items) throws Exception {
		for (PushNotificationData item : items) {
			if (!StringUtils.isEmpty(item.getFcmToken())) {
				logger.debug("Sending for item:<{}>", item);

				FcmNotifMessageDto.Notification notifMessage = new FcmNotifMessageDto.Notification();

				notifMessage.setTitle(
						messageSource.getMessage(PREFIX_MESSAGE_TITLE + item.getPushNotificationType().toString(), null,
								localeService.getDefaultLocale()));
				notifMessage.setBody(
						messageSource.getMessage(PREFIX_MESSAGE_BODY + item.getPushNotificationType().toString(), null,
								localeService.getDefaultLocale()));

				if (!StringUtils.isEmpty(item.getOrderId())) {
					notificationService.sendFcmPushNotification(item.getUserId(), item.getFcmToken(), notifMessage,
							FcmNotifAction.order, item.getOrderId());
				} else {
					notificationService.sendFcmPushNotification(item.getUserId(), item.getFcmToken(), notifMessage);
				}
			} else {
				logger.debug("Skip empty fcm_token for item:<{}>", item);
			}
		}
	}

}
