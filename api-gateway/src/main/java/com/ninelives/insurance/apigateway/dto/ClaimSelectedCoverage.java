package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimSelectedCoverage {
	
	private String nineliveCoverageId;
	
	private String insuranceCategory;
	
	private String coverageName;
	
	private String aswataCoverageCode;

	public String getNineliveCoverageId() {
		return nineliveCoverageId;
	}

	public void setNineliveCoverageId(String nineliveCoverageId) {
		this.nineliveCoverageId = nineliveCoverageId;
	}

	public String getInsuranceCategory() {
		return insuranceCategory;
	}

	public void setInsuranceCategory(String insuranceCategory) {
		this.insuranceCategory = insuranceCategory;
	}

	public String getCoverageName() {
		return coverageName;
	}

	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}

	public String getAswataCoverageCode() {
		return aswataCoverageCode;
	}

	public void setAswataCoverageCode(String aswataCoverageCode) {
		this.aswataCoverageCode = aswataCoverageCode;
	}

	@Override
	public String toString() {
		return "ClaimSelectedCoverage [nineliveCoverageId=" + nineliveCoverageId + ", insuranceCategory="
				+ insuranceCategory + ", coverageName=" + coverageName + ", aswataCoverageCode=" + aswataCoverageCode
				+ "]";
	}

   
}
