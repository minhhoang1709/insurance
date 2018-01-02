package com.ninelives.insurance.model;

import java.time.LocalDateTime;

import com.ninelives.insurance.ref.InviteVoucherStatus;

public class UserInviteVoucher {
    private String userId;

    private String code;

    private Integer voucherId;

    private InviteVoucherStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private Integer inviterRewardCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public InviteVoucherStatus getStatus() {
        return status;
    }

    public void setStatus(InviteVoucherStatus status) {
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

    public Integer getInviterRewardCount() {
        return inviterRewardCount;
    }

    public void setInviterRewardCount(Integer inviterRewardCount) {
        this.inviterRewardCount = inviterRewardCount;
    }
}