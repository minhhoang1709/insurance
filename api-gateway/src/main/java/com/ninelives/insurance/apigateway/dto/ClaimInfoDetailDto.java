package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimInfoDetailDto {
	
	
	private ClaimInfo claimInfo;
	
	private List<ClaimSelectedCoverage> claimSelectedCoverage;
    
    private ClaimBankAccount claimBankAccount;
    
    private List<ClaimDocument> claimDocument;

	public ClaimInfo getClaimInfo() {
		return claimInfo;
	}

	public void setClaimInfo(ClaimInfo claimInfo) {
		this.claimInfo = claimInfo;
	}

	public List<ClaimSelectedCoverage> getClaimSelectedCoverage() {
		return claimSelectedCoverage;
	}

	public void setClaimSelectedCoverage(List<ClaimSelectedCoverage> claimSelectedCoverage) {
		this.claimSelectedCoverage = claimSelectedCoverage;
	}

	public ClaimBankAccount getClaimBankAccount() {
		return claimBankAccount;
	}

	public void setClaimBankAccount(ClaimBankAccount claimBankAccount) {
		this.claimBankAccount = claimBankAccount;
	}

	public List<ClaimDocument> getClaimDocument() {
		return claimDocument;
	}

	public void setClaimDocument(List<ClaimDocument> claimDocument) {
		this.claimDocument = claimDocument;
	}

	@Override
	public String toString() {
		return "ClaimInfoDetailDto [claimInfo=" + claimInfo + ", claimSelectedCoverage=" + claimSelectedCoverage
				+ ", claimBankAccount=" + claimBankAccount + ", claimDocument=" + claimDocument + "]";
	}
    
	
	
	
}
