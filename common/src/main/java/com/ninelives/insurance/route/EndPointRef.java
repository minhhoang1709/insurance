package com.ninelives.insurance.route;

public class EndPointRef {
	public static final String QUEUE_FCM_NOTIFICATION="activemq:queue:ninelives.ninelives-notif.fcm-message";
	public static final String QUEUE_ORDER="activemq:queue:ninelives.ninelives-api.order";
	public static final String QUEUE_SUCCESS_PAYMENT_TO_INSURER="activemq:queue:ninelives.ninelives-insurer.success-payment-order";
}
