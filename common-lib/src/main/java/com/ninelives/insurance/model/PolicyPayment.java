package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.PaymentStatus;

public class PolicyPayment implements Serializable{
	private static final long serialVersionUID = 8935081305897219608L;

	private String id;

    private String orderId;

    private String userId;

    private Integer totalAmount;

    private String paymentType;

    private String providerTransactionId;

    private LocalDateTime providerTransactionTime;

    private String providerStatusCode;

    private String providerTransactionStatus;

    private Integer paymentSeq;

    private Integer notifPaymentSeq;

    private LocalDateTime startTime;

    private LocalDateTime chargeTime;
    
    private LocalDateTime chargeExpiryTime;

    private LocalDateTime notifPendingTime;

    private LocalDateTime notifSuccessTime;

    private LocalDateTime notifFailedTime;

    private LocalDateTime notifExpiredTime;

    private PaymentStatus status;

    private LocalDateTime updateDate;
    
    private String paymentProviderCode;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public LocalDateTime getProviderTransactionTime() {
        return providerTransactionTime;
    }

    public void setProviderTransactionTime(LocalDateTime providerTransactionTime) {
        this.providerTransactionTime = providerTransactionTime;
    }

    public String getProviderStatusCode() {
        return providerStatusCode;
    }

    public void setProviderStatusCode(String providerStatusCode) {
        this.providerStatusCode = providerStatusCode;
    }

    public String getProviderTransactionStatus() {
        return providerTransactionStatus;
    }

    public void setProviderTransactionStatus(String providerTransactionStatus) {
        this.providerTransactionStatus = providerTransactionStatus;
    }

    public Integer getPaymentSeq() {
        return paymentSeq;
    }

    public void setPaymentSeq(Integer paymentSeq) {
        this.paymentSeq = paymentSeq;
    }

    public Integer getNotifPaymentSeq() {
        return notifPaymentSeq;
    }

    public void setNotifPaymentSeq(Integer notifPaymentSeq) {
        this.notifPaymentSeq = notifPaymentSeq;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(LocalDateTime chargeTime) {
        this.chargeTime = chargeTime;
    }

    public LocalDateTime getChargeExpiryTime() {
		return chargeExpiryTime;
	}

	public void setChargeExpiryTime(LocalDateTime chargeExpiryTime) {
		this.chargeExpiryTime = chargeExpiryTime;
	}

	public LocalDateTime getNotifPendingTime() {
        return notifPendingTime;
    }

    public void setNotifPendingTime(LocalDateTime notifPendingTime) {
        this.notifPendingTime = notifPendingTime;
    }

    public LocalDateTime getNotifSuccessTime() {
        return notifSuccessTime;
    }

    public void setNotifSuccessTime(LocalDateTime notifSuccessTime) {
        this.notifSuccessTime = notifSuccessTime;
    }

    public LocalDateTime getNotifFailedTime() {
        return notifFailedTime;
    }

    public void setNotifFailedTime(LocalDateTime notifFailedTime) {
        this.notifFailedTime = notifFailedTime;
    }

    public LocalDateTime getNotifExpiredTime() {
        return notifExpiredTime;
    }

    public void setNotifExpiredTime(LocalDateTime notifExpiredTime) {
        this.notifExpiredTime = notifExpiredTime;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

	@Override
	public String toString() {
		return "PolicyPayment [" + (id != null ? "id=" + id + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "")
				+ (paymentType != null ? "paymentType=" + paymentType + ", " : "")
				+ (providerTransactionId != null ? "providerTransactionId=" + providerTransactionId + ", " : "")
				+ (providerTransactionTime != null ? "providerTransactionTime=" + providerTransactionTime + ", " : "")
				+ (providerStatusCode != null ? "providerStatusCode=" + providerStatusCode + ", " : "")
				+ (providerTransactionStatus != null ? "providerTransactionStatus=" + providerTransactionStatus + ", "
						: "")
				+ (paymentSeq != null ? "paymentSeq=" + paymentSeq + ", " : "")
				+ (notifPaymentSeq != null ? "notifPaymentSeq=" + notifPaymentSeq + ", " : "")
				+ (startTime != null ? "startTime=" + startTime + ", " : "")
				+ (chargeTime != null ? "chargeTime=" + chargeTime + ", " : "")
				+ (notifPendingTime != null ? "notifPendingTime=" + notifPendingTime + ", " : "")
				+ (notifSuccessTime != null ? "notifSuccessTime=" + notifSuccessTime + ", " : "")
				+ (notifFailedTime != null ? "notifFailedTime=" + notifFailedTime + ", " : "")
				+ (notifExpiredTime != null ? "notifExpiredTime=" + notifExpiredTime + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "")
				+ (paymentProviderCode != null ? "paymentProviderCode=" + paymentProviderCode : "")+ "]";
	}

	public String getPaymentProviderCode() {
		return paymentProviderCode;
	}

	public void setPaymentProviderCode(String paymentProviderCode) {
		this.paymentProviderCode = paymentProviderCode;
	}
    
}