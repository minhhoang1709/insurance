package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.ninelives.insurance.ref.SignupVerificationStatus;
import com.ninelives.insurance.ref.SignupVerificationType;

public class SignupVerification implements Serializable{
	private static final long serialVersionUID = -1086825150112370110L;

	private Long id;

    private String email;

    private String password;

    private String verificationCode;

    private SignupVerificationType verificationType;

    private SignupVerificationStatus status;

    private LocalDateTime createdDate;

    private Date verifyDate;
    
    private String verificationToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public SignupVerificationType getVerificationType() {
		return verificationType;
	}

	public void setVerificationType(SignupVerificationType verificationType) {
		this.verificationType = verificationType;
	}

	public SignupVerificationStatus getStatus() {
		return status;
	}

	public void setStatus(SignupVerificationStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
    
    
}