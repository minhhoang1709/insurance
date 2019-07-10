package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreeVoucherFormDto {
	
	
	private String promoCode;
	
	private String quota;
	
	private String coverage;
	
	private String periodId;
	
	private String promoStartDate;
	
	private String promoEndDate;
	
	private String userName;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getPromoStartDate() {
		return promoStartDate;
	}

	public void setPromoStartDate(String promoStartDate) {
		this.promoStartDate = promoStartDate;
	}

	public String getPromoEndDate() {
		return promoEndDate;
	}

	public void setPromoEndDate(String promoEndDate) {
		this.promoEndDate = promoEndDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "FreeVoucherFormDto [promoCode=" + promoCode + ", quota=" + quota + ", coverage=" + coverage
				+ ", periodId=" + periodId + ", promoStartDate=" + promoStartDate + ", promoEndDate=" + promoEndDate
				+ ", userName=" + userName + "]";
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	
}
