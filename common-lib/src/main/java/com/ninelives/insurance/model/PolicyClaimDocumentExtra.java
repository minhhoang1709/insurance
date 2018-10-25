package com.ninelives.insurance.model;

public class PolicyClaimDocumentExtra {
	PolicyClaimFamily family;

	public PolicyClaimFamily getFamily() {
		return family;
	}

	public void setFamily(PolicyClaimFamily family) {
		this.family = family;
	}

	@Override
	public String toString() {
		return "PolicyClaimDocumentExtra [family=" + family + "]";
	}
	
}
