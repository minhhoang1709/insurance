package com.ninelives.insurance.core.exception;

import java.util.Arrays;

import com.ninelives.insurance.ref.ErrorCode;

public class AppException extends Exception {
	private static final long serialVersionUID = 1L;
	
	ErrorCode code;
	String message;
	String[] messageParams = null;

	public AppException(ErrorCode code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public AppException(ErrorCode code, String message, String[] messageParams) {
		super();
		this.code = code;
		this.message = message;
		this.messageParams = messageParams;
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
	public String[] getMessageParams() {
		return messageParams;
	}
	public void setMessageParams(String[] messageParams) {
		this.messageParams = messageParams;
	}
	@Override
	public String toString() {
		return "AppException [code=" + code + ", message=" + message + ", messageParams="
				+ Arrays.toString(messageParams) + "]";
	}		
	
}
