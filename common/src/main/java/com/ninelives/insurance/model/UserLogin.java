package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class UserLogin {
    private String tokenId;

    private String userId;

    private LocalDateTime createdDate;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

	@Override
	public String toString() {
		return "UserLogin [tokenId=" + tokenId + ", userId=" + userId + ", createdDate=" + createdDate + "]";
	}


}