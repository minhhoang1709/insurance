package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import com.ninelives.insurance.ref.Gender;
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

    private UserStatus status;

    private Boolean isSyncGmailEnabled;

    private Boolean isNotificationEnabled;

    private Date createdDate;

    private Date updateDate;

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

    public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
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

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=***" + ", email=" + email + ", googleName=" + googleName
				+ ", googleRefreshToken=***" + ", googleAuthCode=***"
				+ ", googleAccessToken=***" + ", googleUserId=" + googleUserId + ", fcmToken=***" 
				+ ", name=" + name + ", gender=" + gender + ", birthDate=" + birthDate + ", birthPlace="
				+ birthPlace + ", phone=" + phone + ", address=" + address + ", idCardFileId=" + idCardFileId
				+ ", status=" + status + ", isSyncGmailEnabled=" + isSyncGmailEnabled + ", isNotificationEnabled="
				+ isNotificationEnabled + ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}	
    
}