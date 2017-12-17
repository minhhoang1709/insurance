package com.ninelives.insurance.payment.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class PaymentNotificationNotAuthorizedException extends PaymentNotificationException{
	private static final long serialVersionUID = 1L;

	public PaymentNotificationNotAuthorizedException(ErrorCode code, String message) {
		super(code, message);
	}

}
