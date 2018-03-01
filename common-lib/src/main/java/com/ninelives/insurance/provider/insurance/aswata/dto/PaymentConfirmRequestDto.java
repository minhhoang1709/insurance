package com.ninelives.insurance.provider.insurance.aswata.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentConfirmRequestDto  implements Serializable{
	private static final long serialVersionUID = -1292040413229552546L;
	
	@JsonProperty("service_code")
	private String serviceCode;
	@JsonProperty("user_ref_no")
	private String userRefNo;
	@JsonProperty("client_code")
	private String clientCode;
	@JsonProperty("request_time")
	private String requestTime;
	@JsonProperty("request_param")
	private RequestParam requestParam;
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
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public RequestParam getRequestParam() {
		return requestParam;
	}
	public void setRequestParam(RequestParam requestParam) {
		this.requestParam = requestParam;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	
	@Override
	public String toString() {
		return "PaymentConfirmRequestDto [serviceCode=" + serviceCode + ", userRefNo=" + userRefNo + ", clientCode="
				+ clientCode + ", requestTime=" + requestTime + ", requestParam=" + requestParam + ", authCode="
				+ authCode + "]";
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class RequestParam implements Serializable{
		private static final long serialVersionUID = 4781765521146485870L;

		@JsonProperty("order_number")
		private String orderNumber;
		@JsonProperty("payment_token")
		private String paymentToken;
		@JsonProperty("payment_amount")
		private String paymentAmount;
		
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getPaymentToken() {
			return paymentToken;
		}
		public void setPaymentToken(String paymentToken) {
			this.paymentToken = paymentToken;
		}
		public String getPaymentAmount() {
			return paymentAmount;
		}
		public void setPaymentAmount(String paymentAmount) {
			this.paymentAmount = paymentAmount;
		}
		@Override
		public String toString() {
			return "RequestParam [orderNumber=" + orderNumber + ", paymentToken=" + paymentToken + ", paymentAmount="
					+ paymentAmount + "]";
		}		
	}
}
