package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionReportBenefitPeriodDto {
	
	
	private String insuranceTypeName;
	
	private String period;
	
	private String benefit;
	
	private String periodCode;
	
	private String coverageCode;
	
	private String unpaid;
	
	private String paid;
	
	private String notActive;
	
	private String active;
	
	private String expired;

	public String getInsuranceTypeName() {
		return insuranceTypeName;
	}

	public void setInsuranceTypeName(String insuranceTypeName) {
		this.insuranceTypeName = insuranceTypeName;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getBenefit() {
		return benefit;
	}

	public void setBenefit(String benefit) {
		this.benefit = benefit;
	}

	public String getUnpaid() {
		return unpaid;
	}

	public void setUnpaid(String unpaid) {
		this.unpaid = unpaid;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getNotActive() {
		return notActive;
	}

	public void setNotActive(String notActive) {
		this.notActive = notActive;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getExpired() {
		return expired;
	}

	public void setExpired(String expired) {
		this.expired = expired;
	}

	@Override
	public String toString() {
		return "TransactionReportBenefitPeriodDto [insuranceTypeName=" + insuranceTypeName + ", period=" + period
				+ ", benefit=" + benefit + ", periodCode=" + periodCode + ", coverageCode=" + coverageCode + ", unpaid="
				+ unpaid + ", paid=" + paid + ", notActive=" + notActive + ", active=" + active + ", expired=" + expired
				+ "]";
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public String getCoverageCode() {
		return coverageCode;
	}

	public void setCoverageCode(String coverageCode) {
		this.coverageCode = coverageCode;
	}

	
	
    
}
