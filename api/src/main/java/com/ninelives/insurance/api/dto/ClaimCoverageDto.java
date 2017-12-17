package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.ClaimCoverageStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimCoverageDto {
	private CoverageDto coverage;
	private ClaimCoverageStatus status;
	
	public CoverageDto getCoverage() {
		return coverage;
	}
	public void setCoverage(CoverageDto coverage) {
		this.coverage = coverage;
	}
	public ClaimCoverageStatus getStatus() {
		return status;
	}
	public void setStatus(ClaimCoverageStatus status) {
		this.status = status;
	}
	
}
