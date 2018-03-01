package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
	
	public Map<String, Object> other = new HashMap<>();
	
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
	@JsonAnyGetter
	public Map<String, Object> any() {
		return other;
	}
	@JsonAnySetter
	public void set(String name, Object value) {
		other.put(name, value);
	}
	public boolean hasUnknowProperties() {
		return !other.isEmpty();
	}		
	public Map<String, Object> getOther() {
		return other;
	}
	public void setOther(Map<String, Object> other) {
		this.other = other;
	}
	@Override
	public String toString() {
		return "OrderResponseDto [serviceCode=" + serviceCode + ", userRefNo=" + userRefNo + ", clientCode="
				+ clientCode + ", responseTime=" + responseTime + ", responseCode=" + responseCode + ", errorDesc="
				+ errorDesc + ", responseParam=" + responseParam + ", authCode=" + authCode + ", other=" + other + "]";
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ResponseParam implements Serializable{
		private static final long serialVersionUID = 8119078628021585891L;
		@JsonProperty("policy_number")
		private String policyNumber;
		@JsonProperty("order_number")
		private String orderNumber;
		@JsonProperty("download_url")
		private String downloadUrl;
		
		public Map<String, Object> other = new HashMap<>();
		
		public String getPolicyNumber() {
			return policyNumber;
		}
		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getDownloadUrl() {
			return downloadUrl;
		}
		public void setDownloadUrl(String downloadUrl) {
			this.downloadUrl = downloadUrl;
		}
		@JsonAnyGetter
		public Map<String, Object> any() {
			return other;
		}
		@JsonAnySetter
		public void set(String name, Object value) {
			other.put(name, value);
		}
		public boolean hasUnknowProperties() {
			return !other.isEmpty();
		}		
		public Map<String, Object> getOther() {
			return other;
		}
		public void setOther(Map<String, Object> other) {
			this.other = other;
		}
		
		@Override
		public String toString() {
			return "ResponseParam [policyNumber=" + policyNumber + ", orderNumber=" + orderNumber + ", downloadUrl="
					+ downloadUrl + ", other=" + other + "]";
		}
		
	}
}
