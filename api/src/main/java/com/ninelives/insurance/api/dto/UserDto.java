package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.Gender;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	public static final String CONFIG_KEY_IS_SYNC_GMAIL_ENABLED = "isSyncGmailEnabled";
	public static final String CONFIG_KEY_IS_NOTIFICATION_ENABLED = "isNotificationEnabled";
	
    private String userId;

    @Email
    private String email;

    @Size(max=255)
    private String name;

    private Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthDate;

    @Size(max=255)
    private String birthPlace;

    @Size(max=50)
    private String phone;

    @Size(max=255)
    private String address;

    //private Long idCardFileId;

    private Map<String, Object> config;
    
    private UserFileDto idCardFile;
    
    private UserFileDto passportFile;
    
    private UserFileDto photoFile;

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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDateTime birthDate) {
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

//	public Long getIdCardFileId() {
//		return idCardFileId;
//	}
//
//	public void setIdCardFileId(Long idCardFileId) {
//		this.idCardFileId = idCardFileId;
//	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}
		
	public UserFileDto getIdCardFile() {
		return idCardFile;
	}

	public void setIdCardFile(UserFileDto idCardFile) {
		this.idCardFile = idCardFile;
	}

	public UserFileDto getPassportFile() {
		return passportFile;
	}

	public void setPassportFile(UserFileDto passportFile) {
		this.passportFile = passportFile;
	}

	public UserFileDto getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(UserFileDto photoFile) {
		this.photoFile = photoFile;
	}

	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", email=" + email + ", name=" + name + ", gender=" + gender
				+ ", birthDate=" + birthDate + ", birthPlace=" + birthPlace + ", phone=" + phone + ", address="
				+ address + ", config=" + config + ", idCardFile=" + idCardFile + ", passportFile=" + passportFile
				+ "]";
	}
    
}