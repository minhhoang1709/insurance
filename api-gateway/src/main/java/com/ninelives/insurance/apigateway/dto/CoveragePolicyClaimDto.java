package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoveragePolicyClaimDto {
	
	
	private String coverageId;
	
	private String coverageClaimName;
	
	private String coverageClaimStatus;

	
	public String getCoverageId() {
		return coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}

	public String getCoverageClaimName() {
		return coverageClaimName;
	}

	public void setCoverageClaimName(String coverageClaimName) {
		this.coverageClaimName = coverageClaimName;
	}

	public String getCoverageClaimStatus() {
		return coverageClaimStatus;
	}

	public void setCoverageClaimStatus(String coverageClaimStatus) {
		this.coverageClaimStatus = coverageClaimStatus;
	}

	@Override
	public String toString() {
		return "CoveragePolicyClaimDto [coverageId=" + coverageId + ", coverageClaimName=" + coverageClaimName
				+ ", coverageClaimStatus=" + coverageClaimStatus + "]";
	}
	

	
    
}
