package com.ninelives.insurance.api.dto;

public class CoverageClaimDocTypeDto {
    private Boolean isMandatory;
    
    private ClaimDocTypeDto claimDocType;

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public ClaimDocTypeDto getClaimDocType() {
		return claimDocType;
	}

	public void setClaimDocType(ClaimDocTypeDto claimDocType) {
		this.claimDocType = claimDocType;
	}

	    
}
