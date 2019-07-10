package com.ninelives.insurance.apigateway.dto;

import java.util.HashMap;

public class RowFile {
	
	public RowFile(){
	}
	
	private String email;
	
	private String name;
	
	private String gender;
	
	private String birthDate;
	
	private String birthPlace;
	
	private String phone;
	
	private String ktpNo;
	
	private String isValid;
	
	private HashMap<String, String> message;

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

	public String getKtpNo() {
		return ktpNo;
	}

	public void setKtpNo(String ktpNo) {
		this.ktpNo = ktpNo;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public HashMap<String, String> getMessage() {
		return message;
	}

	public void setMessage(HashMap<String, String> message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RowFile [email=" + email + ", name=" + name + ", gender=" + gender + ", birthDate=" + birthDate
				+ ", birthPlace=" + birthPlace + ", phone=" + phone + ", ktpNo=" + ktpNo + ", isValid=" + isValid
				+ ", message=" + message + "]";
	}

	

}
