package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CorporateClient implements Serializable{
	
	private static final long serialVersionUID = -1106804861994184823L;
	
	private String corporateClientId;
	
	private String corporateClientName;
	
	private String corporateClientAddress;
	
	private String corporateClientPhoneNumber;
	
	private String corporateClientEmail;
	
	private String corporateClientProvider;
	
	private String corporateClientProviderId;
	
	
	public String getCorporateClientProviderId() {
		return corporateClientProviderId;
	}

	public void setCorporateClientProviderId(String corporateClientProviderId) {
		this.corporateClientProviderId = corporateClientProviderId;
	}

	public String getCorporateClientId() {
		return corporateClientId;
	}

	public void setCorporateClientId(String corporateClientId) {
		this.corporateClientId = corporateClientId;
	}

	public String getCorporateClientName() {
		return corporateClientName;
	}

	public void setCorporateClientName(String corporateClientName) {
		this.corporateClientName = corporateClientName;
	}

	public String getCorporateClientAddress() {
		return corporateClientAddress;
	}

	public void setCorporateClientAddress(String corporateClientAddress) {
		this.corporateClientAddress = corporateClientAddress;
	}

	public String getCorporateClientPhoneNumber() {
		return corporateClientPhoneNumber;
	}

	public void setCorporateClientPhoneNumber(String corporateClientPhoneNumber) {
		this.corporateClientPhoneNumber = corporateClientPhoneNumber;
	}

	public String getCorporateClientEmail() {
		return corporateClientEmail;
	}

	public void setCorporateClientEmail(String corporateClientEmail) {
		this.corporateClientEmail = corporateClientEmail;
	}

	public String getCorporateClientProvider() {
		return corporateClientProvider;
	}

	public void setCorporateClientProvider(String corporateClientProvider) {
		this.corporateClientProvider = corporateClientProvider;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@JsonIgnore
	private LocalDateTime createdDate;
	
	@JsonIgnore
	private String createdBy;

	@JsonIgnore
    private LocalDateTime updateDate;

	@JsonIgnore
    private String updateBy;


	@Override
	public String toString() {
		return "CorporateClient [corporateClientId=" + corporateClientId + ", corporateClientName="
				+ corporateClientName + ", corporateClientAddress=" + corporateClientAddress
				+ ", corporateClientPhoneNumber=" + corporateClientPhoneNumber + ", corporateClientEmail="
				+ corporateClientEmail + ", corporateClientProvider=" + corporateClientProvider
				+ ", corporateClientProviderId=" + corporateClientProviderId + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", updateDate=" + updateDate + ", updateBy=" + updateBy + "]";
	}
    
	
	
	
	
}
