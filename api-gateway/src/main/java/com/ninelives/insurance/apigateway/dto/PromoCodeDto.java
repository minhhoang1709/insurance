package com.ninelives.insurance.apigateway.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromoCodeDto {
	
	private String promoCode;
	
	private String coverage;
	
	private String coverageId;
	
	private String period;
	
	private String periodId;
	
	private String promoStartDate;
	
	private String promoEndDate;
	
	private String quota;
	
	private String used;

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getCoverageId() {
		return coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
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

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	@Override
	public String toString() {
		return "PromoCodeDto [promoCode=" + promoCode + ", coverage=" + coverage + ", coverageId=" + coverageId
				+ ", period=" + period + ", periodId=" + periodId + ", promoStartDate=" + promoStartDate
				+ ", promoEndDate=" + promoEndDate + ", quota=" + quota + ", used=" + used + "]";
	}
	
	
	
	
	
}
