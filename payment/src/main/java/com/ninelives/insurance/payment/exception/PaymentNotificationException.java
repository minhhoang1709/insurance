package com.ninelives.insurance.payment.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class PaymentNotificationException extends Exception {
	private static final long serialVersionUID = 1L;

	ErrorCode code;
	String message;

	public PaymentNotificationException(ErrorCode code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "PaymentNotificationException [code=" + code + ", message=" + message + "]";
	}

}
