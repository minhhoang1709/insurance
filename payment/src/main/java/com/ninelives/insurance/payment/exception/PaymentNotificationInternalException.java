package com.ninelives.insurance.payment.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class PaymentNotificationInternalException extends PaymentNotificationException {
	private static final long serialVersionUID = 1L;

	public PaymentNotificationInternalException(ErrorCode code, String message) {
		super(code, message);
	}

}
