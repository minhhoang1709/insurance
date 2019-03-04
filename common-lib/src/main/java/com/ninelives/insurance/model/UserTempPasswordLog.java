package com.ninelives.insurance.model;

import java.time.LocalDateTime;

import com.ninelives.insurance.ref.UserTempPasswordStatus;

public class UserTempPasswordLog {
    private Long id;

    private String userId;

    private String email;

    private String password;

    private UserTempPasswordStatus oldStatus;

    private UserTempPasswordStatus newStatus;

    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserTempPasswordStatus getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(UserTempPasswordStatus oldStatus) {
		this.oldStatus = oldStatus;
	}

	public UserTempPasswordStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(UserTempPasswordStatus newStatus) {
		this.newStatus = newStatus;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}