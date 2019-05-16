package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.SignupVerificationStatus;
import com.ninelives.insurance.ref.SignupVerificationType;
import com.ninelives.insurance.ref.UserRegisterChannel;
import com.ninelives.insurance.ref.UserSource;

public class SignupVerification implements Serializable{
	private static final long serialVersionUID = -1086825150112370110L;

	private Long id;

    private String email;

    private String password;

    private String fcmToken;
    
    private String verificationCode;

    private SignupVerificationType verificationType;

    private SignupVerificationStatus status;
    
    private UserSource regSource;
    
    private UserRegisterChannel regChannel;
    
    private String languageCode;

    private LocalDateTime createdDate;

    private LocalDateTime verifyDate;
    
    private String verificationToken; //transient

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

    public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
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

	public UserSource getRegSource() {
		return regSource;
	}

	public void setRegSource(UserSource regSource) {
		this.regSource = regSource;
	}

	public UserRegisterChannel getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(UserRegisterChannel regChannel) {
		this.regChannel = regChannel;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(LocalDateTime verifyDate) {
        this.verifyDate = verifyDate;
    }

	public String getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}

	@Override
	public String toString() {
		return "SignupVerification [email=" + email + ", verificationCode=" + verificationCode
				+ ", verificationType=" + verificationType + ", status=" + status + ", regSource=" + regSource
				+ ", regChannel=" + regChannel + ", createdDate=" + createdDate + ", verifyDate=" + verifyDate
				+ ", verificationToken=" + verificationToken + "]";
	}
    
    
}