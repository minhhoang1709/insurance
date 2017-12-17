package com.ninelives.insurance.api.exception;

import com.ninelives.insurance.ref.ErrorCode;

public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	
	ErrorCode code;
	String message;

	public ApiException(ErrorCode code, String message) {
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
		return "ApiException [code=" + code + ", message=" + message + "]";
	}
	
}
