package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.api.model.Coverage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDto {
	private String claimId;

    private OrderDto order;

    private String claimDateTime;

    private String accidentDateTime;

    private String accidentSummary;

    private ClaimAccidentAddressDto accidentAddress;

    private List<CoverageDto> coverages;

    private ClaimAccountDto claimAccount;
    
    private List<ClaimDocumentDto> claimDocuments;
    
    private String status;

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}



	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public String getClaimDateTime() {
		return claimDateTime;
	}

	public void setClaimDateTime(String claimDateTime) {
		this.claimDateTime = claimDateTime;
	}

	public String getAccidentDateTime() {
		return accidentDateTime;
	}

	public void setAccidentDateTime(String accidentDateTime) {
		this.accidentDateTime = accidentDateTime;
	}

	public String getAccidentSummary() {
		return accidentSummary;
	}

	public void setAccidentSummary(String accidentSummary) {
		this.accidentSummary = accidentSummary;
	}

	public ClaimAccidentAddressDto getAccidentAddress() {
		return accidentAddress;
	}

	public void setAccidentAddress(ClaimAccidentAddressDto accidentAddress) {
		this.accidentAddress = accidentAddress;
	}

	public List<CoverageDto> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<CoverageDto> coverages) {
		this.coverages = coverages;
	}

	public ClaimAccountDto getClaimAccount() {
		return claimAccount;
	}

	public void setClaimAccount(ClaimAccountDto claimAccount) {
		this.claimAccount = claimAccount;
	}

	public List<ClaimDocumentDto> getClaimDocuments() {
		return claimDocuments;
	}

	public void setClaimDocuments(List<ClaimDocumentDto> claimDocuments) {
		this.claimDocuments = claimDocuments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClaimDto [" + (claimId != null ? "claimId=" + claimId + ", " : "")
				+ (order != null ? "order=" + order + ", " : "")
				+ (claimDateTime != null ? "claimDateTime=" + claimDateTime + ", " : "")
				+ (accidentDateTime != null ? "accidentDateTime=" + accidentDateTime + ", " : "")
				+ (accidentSummary != null ? "accidentSummary=" + accidentSummary + ", " : "")
				+ (accidentAddress != null ? "accidentAddress=" + accidentAddress + ", " : "")
				+ (coverages != null ? "coverages=" + coverages + ", " : "")
				+ (claimAccount != null ? "claimAccount=" + claimAccount + ", " : "")
				+ (claimDocuments != null ? "claimDocuments=" + claimDocuments + ", " : "")
				+ (status != null ? "status=" + status : "") + "]";
	}
	
    
}
