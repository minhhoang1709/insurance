package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolicyCoverageItem {
	
	private String coverageId;
	

	private String coverageStatus;


	public String getCoverageId() {
		return coverageId;
	}


	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}


	public String getCoverageStatus() {
		return coverageStatus;
	}


	public void setCoverageStatus(String coverageStatus) {
		this.coverageStatus = coverageStatus;
	}


	@Override
	public String toString() {
		return "PolicyCoverageItem [coverageId=" + coverageId + ", coverageStatus=" + coverageStatus + "]";
	}

	
	
	
}
