package com.ninelives.insurance.apigateway.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserManagementDto {
	
	private String userId;
	
	private String name;
	
	private String email;
	
	private String gender;
	
	private String birthDate;
	
	private String birthPlace;
	
	private String phone;
	
	private String address;
	
	private String idCardNumber;
	
	private String status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
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

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserManagementDto [userId=" + userId + ", name=" + name + ", email=" + email + ", gender=" + gender
				+ ", birthDate=" + birthDate + ", birthPlace=" + birthPlace + ", phone=" + phone + ", address="
				+ address + ", idCardNumber=" + idCardNumber + ", status=" + status + "]";
	}
	
	
	
}
