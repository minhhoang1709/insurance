package com.ninelives.insurance.provider.insurance.pti.dto;

import java.io.Serializable;

public class PtiResponseDto implements Serializable {

	private static final long serialVersionUID = -7922145528081835863L;

	public static final int HTTP_STATUS_SUCCESS = 200;
	public static final int RESPONSE_CODE_SUCCESS = 1;

	private int httpStatus;
	private String errorMessage;
	private PtiResponeResultDto responseResult;

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

	public PtiResponeResultDto getResponseResult() {
		return responseResult;
	}

	public void setResponseResult(PtiResponeResultDto responseResult) {
		this.responseResult = responseResult;
	}

	public boolean isSuccess() {
		if (httpStatus == HTTP_STATUS_SUCCESS && responseResult != null
				&& responseResult.getResultCode() == RESPONSE_CODE_SUCCESS) {
			return true;
		} else {
			return false;
		}
	}

}
