package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimResponseDto  implements Serializable{
	private static final long serialVersionUID = -7645753993892074153L;

	@JsonProperty("service_code")
	private String serviceCode;
	@JsonProperty("user_ref_no")
	private String userRefNo;
	@JsonProperty("client_code")
	private String clientCode;
	@JsonProperty("response_time")
	private String responseTime;
	@JsonProperty("response_code")
	private String responseCode;
	@JsonProperty("error_desc")
	private String errorDesc;
	@JsonProperty("response_param")
	private ResponseParam responseParam;
	@JsonProperty("auth_code")
	private String authCode;
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getUserRefNo() {
		return userRefNo;
	}
	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}
	public String getClientCode() {
		return clientCode;
	}
	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getErrorDesc() {
		return errorDesc;
	}
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	public ResponseParam getResponseParam() {
		return responseParam;
	}
	public void setResponseParam(ResponseParam responseParam) {
		this.responseParam = responseParam;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	@Override
	public String toString() {
		return "ClaimResponseDto [serviceCode=" + serviceCode + ", userRefNo=" + userRefNo + ", clientCode="
				+ clientCode + ", responseTime=" + responseTime + ", responseCode=" + responseCode + ", errorDesc="
				+ errorDesc + ", responseParam=" + responseParam + ", authCode=" + authCode + "]";
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ResponseParam implements Serializable{
		private static final long serialVersionUID = -3191051849343831968L;

		@JsonProperty("claim_reg_number")
		private String claimRegNumber;

		public String getClaimRegNumber() {
			return claimRegNumber;
		}

		public void setClaimRegNumber(String claimRegNumber) {
			this.claimRegNumber = claimRegNumber;
		}

		@Override
		public String toString() {
			return "ResponseParam [claimRegNumber=" + claimRegNumber + "]";
		}		
	}
}
