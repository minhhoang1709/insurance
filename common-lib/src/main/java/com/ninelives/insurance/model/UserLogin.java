package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class UserLogin {
    private String tokenId;

    private String userId;
    
    private User user;
    
    private Boolean requirePasswordChange;

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
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getRequirePasswordChange() {
		return requirePasswordChange;
	}

	public void setRequirePasswordChange(Boolean requirePasswordChange) {
		this.requirePasswordChange = requirePasswordChange;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

	@Override
	public String toString() {
		return "UserLogin [tokenId=" + tokenId + ", userId=" + userId + ", requirePasswordChange="
				+ requirePasswordChange + ", createdDate=" + createdDate + "]";
	}


}