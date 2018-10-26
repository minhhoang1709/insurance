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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((family == null) ? 0 : family.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClaimDocumentExtraDto other = (ClaimDocumentExtraDto) obj;
		if (family == null) {
			if (other.family != null)
				return false;
		} else if (!family.equals(other.family))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ClaimDocumentExtraDto [family=" + family + "]";
	}
	
}
