package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class UserAggStat {
    private String userId;

    private Integer successPaymentAmount;
    
    private Integer voucherB2bUseCnt;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSuccessPaymentAmount() {
        return successPaymentAmount;
    }

    public void setSuccessPaymentAmount(Integer successPaymentAmount) {
        this.successPaymentAmount = successPaymentAmount;
    }

    
    public Integer getVoucherB2bUseCnt() {
		return voucherB2bUseCnt;
	}

	public void setVoucherB2bUseCnt(Integer voucherB2bUseCnt) {
		this.voucherB2bUseCnt = voucherB2bUseCnt;
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