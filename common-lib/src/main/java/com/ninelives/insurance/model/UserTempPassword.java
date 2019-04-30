package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.UserTempPasswordStatus;

public class UserTempPassword implements Serializable{
	private static final long serialVersionUID = 5771645486910643273L;

	private String userId;

    private String email;
    
    private String password;

    private UserTempPasswordStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private LocalDateTime registerDate;
    
    private LocalDateTime applyDate;

    private LocalDateTime replaceDate;

    private LocalDateTime expireDate;
            
    private String clearTextPassword; //transient

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

    public UserTempPasswordStatus getStatus() {
        return status;
    }

    public void setStatus(UserTempPasswordStatus status) {
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
    
    public LocalDateTime getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = registerDate;
	}

	public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getReplaceDate() {
        return replaceDate;
    }

    public void setReplaceDate(LocalDateTime replaceDate) {
        this.replaceDate = replaceDate;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

	public String getClearTextPassword() {
		return clearTextPassword;
	}

	public void setClearTextPassword(String clearTextPassword) {
		this.clearTextPassword = clearTextPassword;
	}

	@Override
	public String toString() {
		return "UserTempPassword [userId=" + userId + ", email=" + email + ", password=" + password + ", status="
				+ status + ", createdDate=" + createdDate + ", updateDate=" + updateDate + ", registerDate="
				+ registerDate + ", applyDate=" + applyDate + ", replaceDate=" + replaceDate + ", expireDate="
				+ expireDate + "]";
	}
    
}