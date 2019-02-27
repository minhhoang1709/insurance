package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.ninelives.insurance.ref.Gender;
import com.ninelives.insurance.ref.UserRegisterChannel;
import com.ninelives.insurance.ref.UserSource;
import com.ninelives.insurance.ref.UserStatus;

public class User implements Serializable{
	private static final long serialVersionUID = 134746234530187692L;

	private String userId;

    private String password;

    private String email;

    private String googleName;
    
    private String googleRefreshToken;

    private String googleAuthCode;

    private String googleAccessToken;

    private String googleUserId;    

    private String fcmToken;

    private String name;

    private Gender gender;

    private LocalDate birthDate;

    private String birthPlace;

    private String phone;

    private String address;

    private Long idCardFileId;
    
    private Long passportFileId;

    private UserStatus status;

    private Boolean isSyncGmailEnabled;

    private Boolean isNotificationEnabled;
    
    private String idCardNo;
    
    private UserSource regSource;
    
    private UserRegisterChannel regChannel;
    
    private UserSource verifySource;
    
    private Boolean isEmailVerified;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private LocalDateTime verifyDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }        

    public String getGoogleName() {
		return googleName;
	}

	public void setGoogleName(String googleName) {
		this.googleName = googleName;
	}

	public String getGoogleRefreshToken() {
        return googleRefreshToken;
    }

    public void setGoogleRefreshToken(String googleRefreshToken) {
        this.googleRefreshToken = googleRefreshToken;
    }

    public String getGoogleAuthCode() {
        return googleAuthCode;
    }

    public void setGoogleAuthCode(String googleAuthCode) {
        this.googleAuthCode = googleAuthCode;
    }

    public String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public void setGoogleAccessToken(String googleAccessToken) {
        this.googleAccessToken = googleAccessToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getIdCardFileId() {
        return idCardFileId;
    }

    public void setIdCardFileId(Long idCardFileId) {
        this.idCardFileId = idCardFileId;
    }

    public Long getPassportFileId() {
		return passportFileId;
	}

	public void setPassportFileId(Long passportFileId) {
		this.passportFileId = passportFileId;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
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

	public String getGoogleUserId() {
        return googleUserId;
    }

    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }

	public Boolean getIsSyncGmailEnabled() {
		return isSyncGmailEnabled;
	}

	public void setIsSyncGmailEnabled(Boolean isSyncGmailEnabled) {
		this.isSyncGmailEnabled = isSyncGmailEnabled;
	}

	public Boolean getIsNotificationEnabled() {
		return isNotificationEnabled;
	}

	public void setIsNotificationEnabled(Boolean isNotificationEnabled) {
		this.isNotificationEnabled = isNotificationEnabled;
	}
	
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public UserSource getRegSource() {
		return regSource;
	}

	public void setRegSource(UserSource regSource) {
		this.regSource = regSource;
	}

	public UserSource getVerifySource() {
		return verifySource;
	}

	public void setVerifySource(UserSource verifySource) {
		this.verifySource = verifySource;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public LocalDateTime getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(LocalDateTime verifyDate) {
		this.verifyDate = verifyDate;
	}

	public UserRegisterChannel getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(UserRegisterChannel regChannel) {
		this.regChannel = regChannel;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=***" + ", email=" + email + ", googleName=" + googleName
				+ ", googleRefreshToken=***" + ", googleAuthCode=***"
				+ ", googleAccessToken=***" + ", googleUserId=" + googleUserId + ", fcmToken=***" 
				+ ", name=" + name + ", gender=" + gender + ", birthDate=" + birthDate + ", birthPlace="
				+ birthPlace + ", phone=" + phone + ", address=" + address + ", idCardFileId=" + idCardFileId
				+ ", passportFileId=" + passportFileId + ", status=" + status + ", isSyncGmailEnabled="
				+ isSyncGmailEnabled + ", isNotificationEnabled=" + isNotificationEnabled + ", idCardNo=" + idCardNo
				+ ", regSource=" + regSource + ", regChannel=" + regChannel + ", createdDate=" + createdDate
				+ ", updateDate=" + updateDate + "]";
	}

}