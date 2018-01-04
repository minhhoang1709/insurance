package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PolicyClaimDetail implements Serializable {
	private static final long serialVersionUID = 4769512354684427110L;

	private String claimId;

	private LocalDateTime createdDate;

	private LocalDateTime updateDate;
	
	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
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
