package com.ninelives.insurance.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ninelives.insurance.api.ref.PaymentStatus;

public class PolicyPayment {
    private String id;

    private LocalDate paymentStartDate;

    private String orderId;

    private String userId;

    private Integer totalAmount;

    private PaymentStatus status;

    private String paymentType;

    private String providerTransactionId;

    private String providerTransactionStatus;

    private Integer providerStatusCode;

    private LocalDateTime chargeDate;

    private LocalDateTime pendingNotifDate;

    private LocalDateTime successNotifDate;

    private LocalDateTime failedNotifDate;

    private LocalDateTime expiredNotifDate;

    private Integer cnt;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getPaymentStartDate() {
        return paymentStartDate;
    }

    public void setPaymentStartDate(LocalDate paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
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

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
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

    public LocalDateTime getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDateTime chargeDate) {
        this.chargeDate = chargeDate;
    }

    public LocalDateTime getPendingNotifDate() {
        return pendingNotifDate;
    }

    public void setPendingNotifDate(LocalDateTime pendingNotifDate) {
        this.pendingNotifDate = pendingNotifDate;
    }

    public LocalDateTime getSuccessNotifDate() {
        return successNotifDate;
    }

    public void setSuccessNotifDate(LocalDateTime successNotifDate) {
        this.successNotifDate = successNotifDate;
    }

    public LocalDateTime getFailedNotifDate() {
        return failedNotifDate;
    }

    public void setFailedNotifDate(LocalDateTime failedNotifDate) {
        this.failedNotifDate = failedNotifDate;
    }

    public LocalDateTime getExpiredNotifDate() {
        return expiredNotifDate;
    }

    public void setExpiredNotifDate(LocalDateTime expiredNotifDate) {
        this.expiredNotifDate = expiredNotifDate;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
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
}