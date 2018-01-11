package com.ninelives.insurance.provider.payment.midtrans.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MidtransNotificationDto {
	@JsonProperty("transaction_time")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	public LocalDateTime transactionTime;
	
	@JsonProperty("transaction_status")
	public String transactionStatus;
	
	@JsonProperty("transaction_id")
	public String transactionId;
	
//	@JsonProperty("store")
//	public String store;
	
	@JsonProperty("status_message")
	public String statusMessage;
	
	@JsonProperty("status_code")
	public String statusCode;
	
	@JsonProperty("signature_key")
	public String signatureKey;
	
	@JsonProperty("payment_type")
	public String paymentType;
	
	@JsonProperty("payment_code")
	public String paymentCode;
	
	@JsonProperty("order_id")
	public String orderId;
	
	@JsonProperty("gross_amount")
	public String grossAmount;

	@JsonProperty("fraud_status")
	public String fraudStatus;

	@JsonProperty("custom_field2")
	public Integer paymentSeq;

//	@JsonProperty("payment_amounts")
//	public List<Object> paymentAmounts;
	
	public Map<String, Object> other = new HashMap<>();
	
	public LocalDateTime getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(LocalDateTime transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

//	public String getStore() {
//		return store;
//	}
//
//	public void setStore(String store) {
//		this.store = store;
//	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getSignatureKey() {
		return signatureKey;
	}

	public void setSignatureKey(String signatureKey) {
		this.signatureKey = signatureKey;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	
//	public List<Object> getPaymentAmounts() {
//		return paymentAmounts;
//	}
//
//	public void setPaymentAmounts(List<Object> paymentAmounts) {
//		this.paymentAmounts = paymentAmounts;
//	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(String grossAmount) {
		this.grossAmount = grossAmount;
	}
	
	public String getFraudStatus() {
		return fraudStatus;
	}

	public void setFraudStatus(String fraudStatus) {
		this.fraudStatus = fraudStatus;
	}

	public Integer getPaymentSeq() {
		return paymentSeq;
	}

	public void setPaymentSeq(Integer paymentSeq) {
		this.paymentSeq = paymentSeq;
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
		return "MidtransNotificationDto ["
				+ (transactionTime != null ? "transactionTime=" + transactionTime + ", " : "")
				+ (transactionStatus != null ? "transactionStatus=" + transactionStatus + ", " : "")
				+ (transactionId != null ? "transactionId=" + transactionId + ", " : "")
				+ (statusMessage != null ? "statusMessage=" + statusMessage + ", " : "")
				+ (statusCode != null ? "statusCode=" + statusCode + ", " : "")
				+ (signatureKey != null ? "signatureKey=" + signatureKey + ", " : "")
				+ (paymentType != null ? "paymentType=" + paymentType + ", " : "")
				+ (paymentCode != null ? "paymentCode=" + paymentCode + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (grossAmount != null ? "grossAmount=" + grossAmount + ", " : "")
				+ (fraudStatus != null ? "fraudStatus=" + fraudStatus + ", " : "")
				+ (paymentSeq != null ? "paymentSeq=" + paymentSeq + ", " : "")
				+ (other != null ? "other=" + other : "") + "]";
	}	
}
