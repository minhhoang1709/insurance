package com.ninelives.insurance.apigateway.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

	private String[] errMsgs;

	public String[] getErrMsgs() {
		return errMsgs;
	}

	public void setErrMsg(String errMsg) {
		this.errMsgs = new String[]{errMsg};
	}
	
	public void setErrMsgs(String[] errMsgs) {
		this.errMsgs = errMsgs;
	}
	
	@Override
	public String toString() {
		return "BaseResponse [errMsgs=" + Arrays.toString(errMsgs) + "]";
	}
	
}
