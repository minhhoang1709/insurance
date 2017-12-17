package com.ninelives.insurance.model;

import java.time.LocalDateTime;

import com.ninelives.insurance.ref.ClaimCoverageStatus;

public class PolicyClaimCoverage {
    private Long claimCoverageId;

    private String claimId;

    private String coverageId;

    private ClaimCoverageStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private Coverage coverage;

    public Long getClaimCoverageId() {
        return claimCoverageId;
    }

    public void setClaimCoverageId(Long claimCoverageId) {
        this.claimCoverageId = claimCoverageId;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public ClaimCoverageStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimCoverageStatus status) {
		this.status = status;
	}
	
	public Coverage getCoverage() {
		return coverage;
	}

	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}