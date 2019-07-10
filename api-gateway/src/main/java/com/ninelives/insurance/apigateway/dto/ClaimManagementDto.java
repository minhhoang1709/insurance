package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimManagementDto {
	
	
	private String claimId;
	
	private String claimUserName;
	
	private String claimUserEmail;
	
	private String claimType;
	
	private String insuranceType;
	
	private String claimStatus;
	
	private String claimDate;
		
	private String incidentDateTime;
	
	private String incidentDate;
	
	private String incidentTime;
	
	private String incidentSummary;
	
	private String accidentCountry;
	
	private String accidentCity;
	
	private String accountName;
	
	private String bankName;
	
	private String bankCode;
	
	private String bankAccountNumber;
	
	private String coverage;
	
	private String claimApplicant;
	
	private String documentPath;
	
	private String documentTitle;
	
	private String coveragePolicyClaims;
	
	private String policyNumber;
	
	List<CoveragePolicyClaimDto> coveragePolicyClaimDto;

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public String getClaimUserName() {
		return claimUserName;
	}

	public void setClaimUserName(String claimUserName) {
		this.claimUserName = claimUserName;
	}

	public String getClaimUserEmail() {
		return claimUserEmail;
	}

	public void setClaimUserEmail(String claimUserEmail) {
		this.claimUserEmail = claimUserEmail;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getClaimStatus() {
		return claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getIncidentDateTime() {
		return incidentDateTime;
	}

	public void setIncidentDateTime(String incidentDateTime) {
		this.incidentDateTime = incidentDateTime;
	}

	public String getIncidentSummary() {
		return incidentSummary;
	}

	public void setIncidentSummary(String incidentSummary) {
		this.incidentSummary = incidentSummary;
	}

	public String getAccidentCountry() {
		return accidentCountry;
	}

	public void setAccidentCountry(String accidentCountry) {
		this.accidentCountry = accidentCountry;
	}

	public String getAccidentCity() {
		return accidentCity;
	}

	public void setAccidentCity(String accidentCity) {
		this.accidentCity = accidentCity;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
	}

	public String getClaimApplicant() {
		return claimApplicant;
	}

	public void setClaimApplicant(String claimApplicant) {
		this.claimApplicant = claimApplicant;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	
	public String getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(String claimDate) {
		this.claimDate = claimDate;
	}

	public String getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getIncidentTime() {
		return incidentTime;
	}

	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}

	public String getInsuranceType() {
		return insuranceType;
	}

	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}

	public String getDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(String documentTitle) {
		this.documentTitle = documentTitle;
	}

	public List<CoveragePolicyClaimDto> getCoveragePolicyClaimDto() {
		return coveragePolicyClaimDto;
	}

	public void setCoveragePolicyClaimDto(List<CoveragePolicyClaimDto> coveragePolicyClaimDto) {
		this.coveragePolicyClaimDto = coveragePolicyClaimDto;
	}

	@Override
	public String toString() {
		return "ClaimManagementDto [claimId=" + claimId + ", claimUserName=" + claimUserName + ", claimUserEmail="
				+ claimUserEmail + ", claimType=" + claimType + ", insuranceType=" + insuranceType + ", claimStatus="
				+ claimStatus + ", claimDate=" + claimDate + ", incidentDateTime=" + incidentDateTime
				+ ", incidentDate=" + incidentDate + ", incidentTime=" + incidentTime + ", incidentSummary="
				+ incidentSummary + ", accidentCountry=" + accidentCountry + ", accidentCity=" + accidentCity
				+ ", accountName=" + accountName + ", bankName=" + bankName + ", bankCode=" + bankCode
				+ ", bankAccountNumber=" + bankAccountNumber + ", coverage=" + coverage + ", claimApplicant="
				+ claimApplicant + ", documentPath=" + documentPath + ", documentTitle=" + documentTitle
				+ ", coveragePolicyClaims=" + coveragePolicyClaims + ", policyNumber=" + policyNumber
				+ ", coveragePolicyClaimDto=" + coveragePolicyClaimDto + "]";
	}

	public String getCoveragePolicyClaims() {
		return coveragePolicyClaims;
	}

	public void setCoveragePolicyClaims(String coveragePolicyClaims) {
		this.coveragePolicyClaims = coveragePolicyClaims;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}
	
	

	
    
}
