package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentConfirmResponseDto  implements Serializable{
	private static final long serialVersionUID = -4428024174258466906L;
	
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

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ResponseParam implements Serializable{
		private static final long serialVersionUID = 7160004215699497232L;
		
		@JsonProperty("policy_number")
		private String policyNumber;
		@JsonProperty("oder_number")
		private String oderNumber;
		@JsonProperty("download_url")
		private String downloadUrl;
		@JsonProperty("policy_document")
		private String policyDocument;
		
		public String getPolicyNumber() {
			return policyNumber;
		}
		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		public String getOderNumber() {
			return oderNumber;
		}
		public void setOderNumber(String oderNumber) {
			this.oderNumber = oderNumber;
		}
		public String getDownloadUrl() {
			return downloadUrl;
		}
		public void setDownloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}
		public String getPolicyDocument() {
			return policyDocument;
		}
		public void setPolicyDocument(String policyDocument) {
			this.policyDocument = policyDocument;
		}
		
	}
}
