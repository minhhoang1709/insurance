package com.ninelives.insurance.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ninelives.insurance.api.ref.PaymentStatus;

public class PolicyPayment {
    private Long paymentId;

    private LocalDate chargeDate;

    private String orderId;

    private String userId;

    private Integer totalAmount;

    private PaymentStatus status;

    private String paymentType;

    private String providerPaymentToken;

    private String providerTransactionStatus;

    private Integer providerStatusCode;

    private LocalDateTime chargeResponseDate;

    private LocalDateTime settlementDate;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProviderPaymentToken() {
        return providerPaymentToken;
    }

    public void setProviderPaymentToken(String providerPaymentToken) {
        this.providerPaymentToken = providerPaymentToken;
    }

    public String getProviderTransactionStatus() {
        return providerTransactionStatus;
    }

    public void setProviderTransactionStatus(String providerTransactionStatus) {
        this.providerTransactionStatus = providerTransactionStatus;
    }

    public Integer getProviderStatusCode() {
        return providerStatusCode;
    }

    public void setProviderStatusCode(Integer providerStatusCode) {
        this.providerStatusCode = providerStatusCode;
    }

    public LocalDateTime getChargeResponseDate() {
        return chargeResponseDate;
    }

    public void setChargeResponseDate(LocalDateTime chargeResponseDate) {
        this.chargeResponseDate = chargeResponseDate;
    }

    public LocalDateTime getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDateTime settlementDate) {
        this.settlementDate = settlementDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

	@Override
	public String toString() {
		return "PolicyPayment [" + (paymentId != null ? "paymentId=" + paymentId + ", " : "")
				+ (chargeDate != null ? "chargeDate=" + chargeDate + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (paymentType != null ? "paymentType=" + paymentType + ", " : "")
				+ (providerPaymentToken != null ? "providerPaymentToken=" + providerPaymentToken + ", " : "")
				+ (providerTransactionStatus != null ? "providerTransactionStatus=" + providerTransactionStatus + ", "
						: "")
				+ (providerStatusCode != null ? "providerStatusCode=" + providerStatusCode + ", " : "")
				+ (chargeResponseDate != null ? "chargeResponseDate=" + chargeResponseDate + ", " : "")
				+ (settlementDate != null ? "settlementDate=" + settlementDate + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "") + "]";
	}
    
}