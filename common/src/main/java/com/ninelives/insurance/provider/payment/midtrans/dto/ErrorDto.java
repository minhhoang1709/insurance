package com.ninelives.insurance.provider.payment.midtrans.dto;

import com.ninelives.insurance.ref.ErrorCode;

public class ErrorDto {
	int status;
	ErrorCode code;
	String message;
	
	
	public ErrorDto(int status, ErrorCode code, String message) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	
}
