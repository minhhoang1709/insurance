package com.ninelives.insurance.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ninelives.insurance.api.ref.PaymentChargeStatus;

public class PaymentChargeLog {
    private Long id;

    private LocalDate chargeDate;

    private String policyPaymentId;

    private String orderId;

    private String userId;

    private Integer totalAmount;

    private String providerTransactionId;

    private String responseHttpStatus;

    private String responseErrorMessage;

    private LocalDateTime responseDate;

    private PaymentChargeStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(LocalDate chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getPolicyPaymentId() {
        return policyPaymentId;
    }

    public void setPolicyPaymentId(String policyPaymentId) {
        this.policyPaymentId = policyPaymentId;
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

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getResponseHttpStatus() {
        return responseHttpStatus;
    }

    public void setResponseHttpStatus(String responseHttpStatus) {
        this.responseHttpStatus = responseHttpStatus;
    }

    public String getResponseErrorMessage() {
        return responseErrorMessage;
    }

    public void setResponseErrorMessage(String responseErrorMessage) {
        this.responseErrorMessage = responseErrorMessage;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public PaymentChargeStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentChargeStatus status) {
        this.status = status;
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