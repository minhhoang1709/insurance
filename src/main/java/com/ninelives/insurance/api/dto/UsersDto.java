package com.ninelives.insurance.api.dto;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersDto {
	public static final String CONFIG_KEY_IS_SYNC_GMAIL_ENABLED = "isSyncGmailEnabled";
	public static final String CONFIG_KEY_IS_NOTIFICATION_ENABLED = "isNotificationEnabled";
	
    private String userId;

    private String email;

    private String name;

    private String gender;

    private Date birthDate;

    private String birthPlace;

    private String phone;

    private String address;

    private Long idCardFileId;

    private Map<String, Object> config;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
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

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	@Override
	public String toString() {
		return "UsersDto [userId=" + userId + ", email=" + email + ", name=" + name + ", gender=" + gender
				+ ", birthDate=" + birthDate + ", birthPlace=" + birthPlace + ", phone=" + phone + ", address="
				+ address + ", idCardFileId=" + idCardFileId + ", config=" + config + "]";
	}
	
    
}