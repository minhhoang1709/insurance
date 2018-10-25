package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocumentExtraDto {
	private PolicyClaimFamilyDto family;

	public PolicyClaimFamilyDto getFamily() {
		return family;
	}
	public void setFamily(PolicyClaimFamilyDto family) {
		this.family = family;
	}	
}
