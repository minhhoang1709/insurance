package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyRegisterDto {
	
	private String corporateClientId;
	  
	private String companyName;
	  
	private String phoneNumber;
	  
	private String email;
	  
	private String address;
	  
	private String provider;
	
	private String providerId;
	
	private String registerDate;
	
	private String modifiedDate;
	
	public String getCorporateClientId() {
		return corporateClientId;
	}
	
	public void setCorporateClientId(String corporateClientId) {
		this.corporateClientId = corporateClientId;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getProvider() {
		return provider;
	}
	
	public void setProvider(String provider) {
		this.provider = provider;
	}


	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	@Override
	public String toString() {
		return "CompanyRegisterDto [corporateClientId=" + corporateClientId + ", companyName=" + companyName
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", address=" + address + ", provider="
				+ provider + ", providerId=" + providerId + ", registerDate=" + registerDate + ", modifiedDate="
				+ modifiedDate + "]";
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	  
	  
  
  
 

}
