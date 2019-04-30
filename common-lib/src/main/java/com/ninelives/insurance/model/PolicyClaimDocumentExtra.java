package com.ninelives.insurance.model;

import java.io.Serializable;

public class PolicyClaimDocumentExtra implements Serializable{
	private static final long serialVersionUID = 4767079867430627200L;
	
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
