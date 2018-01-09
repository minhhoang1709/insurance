package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto implements Serializable{
	private static final long serialVersionUID = -9148121133242193040L;
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
		return "OrderResponseDto [serviceCode=" + serviceCode + ", userRefNo=" + userRefNo + ", clientCode="
				+ clientCode + ", responseTime=" + responseTime + ", responseCode=" + responseCode + ", errorDesc="
				+ errorDesc + ", responseParam=" + responseParam + ", authCode=" + authCode + "]";
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ResponseParam implements Serializable{
		private static final long serialVersionUID = 8119078628021585891L;
		@JsonProperty("policy_number")
		private String policyNumber;
		@JsonProperty("oder_number")
		private String oderNumber;
		@JsonProperty("download_url")
		private String downloadUrl;
		
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
		@Override
		public String toString() {
			return "ResponseParam [policyNumber=" + policyNumber + ", oderNumber=" + oderNumber + ", downloadUrl="
					+ downloadUrl + "]";
		}
		
	}
}
