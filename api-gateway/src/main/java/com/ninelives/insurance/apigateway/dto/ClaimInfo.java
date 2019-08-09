package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimInfo {
	private String nineliveClaimId;

    private String ninelivesOrderId;

    private String userRefNo;

    private String userEmail;
    
    private String userName;
    
    private String requestTime;
    
    private String policyNumber;
    
    private String phone;
    
    private String dateOfLoss;
    
    private String lossDescription;
    
    private String lossLocationCity;
    
    private String lossLocationProvince;
    
    private String lossLocationCountry;

	public String getNineliveClaimId() {
		return nineliveClaimId;
	}

	public void setNineliveClaimId(String nineliveClaimId) {
		this.nineliveClaimId = nineliveClaimId;
	}

	public String getNinelivesOrderId() {
		return ninelivesOrderId;
	}

	public void setNinelivesOrderId(String ninelivesOrderId) {
		this.ninelivesOrderId = ninelivesOrderId;
	}

	public String getUserRefNo() {
		return userRefNo;
	}

	public void setUserRefNo(String userRefNo) {
		this.userRefNo = userRefNo;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDateOfLoss() {
		return dateOfLoss;
	}

	public void setDateOfLoss(String dateOfLoss) {
		this.dateOfLoss = dateOfLoss;
	}

	public String getLossDescription() {
		return lossDescription;
	}

	public void setLossDescription(String lossDescription) {
		this.lossDescription = lossDescription;
	}

	public String getLossLocationCity() {
		return lossLocationCity;
	}

	public void setLossLocationCity(String lossLocationCity) {
		this.lossLocationCity = lossLocationCity;
	}

	public String getLossLocationProvince() {
		return lossLocationProvince;
	}

	public void setLossLocationProvince(String lossLocationProvince) {
		this.lossLocationProvince = lossLocationProvince;
	}

	public String getLossLocationCountry() {
		return lossLocationCountry;
	}

	public void setLossLocationCountry(String lossLocationCountry) {
		this.lossLocationCountry = lossLocationCountry;
	}

	@Override
	public String toString() {
		return "ClaimInfo [nineliveClaimId=" + nineliveClaimId + ", ninelivesOrderId=" + ninelivesOrderId
				+ ", userRefNo=" + userRefNo + ", userEmail=" + userEmail + ", userName=" + userName + ", requestTime="
				+ requestTime + ", policyNumber=" + policyNumber + ", phone=" + phone + ", dateOfLoss=" + dateOfLoss
				+ ", lossDescription=" + lossDescription + ", lossLocationCity=" + lossLocationCity
				+ ", lossLocationProvince=" + lossLocationProvince + ", lossLocationCountry=" + lossLocationCountry
				+ "]";
	}

   
	
	
	
}
