package com.ninelives.insurance.payment.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class PaymentNotificationBadRequestException extends PaymentNotificationException{
	private static final long serialVersionUID = 1L;

	public PaymentNotificationBadRequestException(ErrorCode code, String message) {
		super(code, message);
	}
}
