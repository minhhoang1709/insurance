package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

public class ResponseDto <T extends IAswataResponsePayload> implements Serializable{
	private static final long serialVersionUID = 7253186965430231957L;
	
	public static final String RESPONSE_CODE_SUCCESS="000000";
	public static final int HTTP_STATUS_SUCCESS=200;
	
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
	public boolean isSuccess(){
		if(httpStatus==HTTP_STATUS_SUCCESS && response!=null && response.isSuccess()){
			return true;
		}else{
			return false;
		}
	}
	@Override
	public String toString() {
		return "ResponseDto [httpStatus=" + httpStatus + ", errorMessage=" + errorMessage + ", response=" + response
				+ "]";
	}
	
}
