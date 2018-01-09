package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

public class ResponseDto <T> implements Serializable{
	private static final long serialVersionUID = 7253186965430231957L;
	
	private int httpStatus;
	private String errorMessage;
	private T response;

	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
	@Override
	public String toString() {
		return "ResponseDto [httpStatus=" + httpStatus + ", errorMessage=" + errorMessage + ", response=" + response
				+ "]";
	}
	
}
