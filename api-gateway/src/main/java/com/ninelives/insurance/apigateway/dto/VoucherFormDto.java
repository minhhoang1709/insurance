package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoucherFormDto {
	
	private String corporateClientId;
	
	private String promoCode;
	
	private String insuranceType;
	
	private String title;
	
	private String subtitle;
	
	private String description;
	
	private String coverage;
	
	private String period;
	
	private String policyStartDate;
	
	private String useEndDate;
	
	private String useStartDate;
	
	private String userName;

	public String getCorporateClientId() {
		return corporateClientId;
	}

	public void setCorporateClientId(String corporateClientId) {
		this.corporateClientId = corporateClientId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(String useEndDate) {
		this.useEndDate = useEndDate;
	}

	public String getUseStartDate() {
		return useStartDate;
	}

	public void setUseStartDate(String useStartDate) {
		this.useStartDate = useStartDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
