package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.ClaimStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccidentClaimDto {
	private String claimId;

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime claimDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime accidentDate;

    @Size(max=2500)
    private String accidentSummary;
    
    @Valid
    private ClaimDetailAccidentAddressDto accidentAddress;
   
    private List<ClaimCoverageDto> claimCoverages;
    
    @Valid
    private ClaimBankAccountDto claimBankAccount;
    
    private List<ClaimDocumentDto> claimDocuments;
    
    private CoverageCategoryDto coverageCategory;
    
    private OrderDto order;

    private ClaimStatus status;

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public LocalDateTime getClaimDate() {
		return claimDate;
	}

	public void setClaimDate(LocalDateTime claimDate) {
		this.claimDate = claimDate;
	}

	public LocalDateTime getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(LocalDateTime accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getAccidentSummary() {
		return accidentSummary;
	}

	public void setAccidentSummary(String accidentSummary) {
		this.accidentSummary = accidentSummary;
	}

	public ClaimDetailAccidentAddressDto getAccidentAddress() {
		return accidentAddress;
	}

	public void setAccidentAddress(ClaimDetailAccidentAddressDto accidentAddress) {
		this.accidentAddress = accidentAddress;
	}

	public List<ClaimCoverageDto> getClaimCoverages() {
		return claimCoverages;
	}

	public void setClaimCoverages(List<ClaimCoverageDto> claimCoverages) {
		this.claimCoverages = claimCoverages;
	}

	public ClaimBankAccountDto getClaimBankAccount() {
		return claimBankAccount;
	}

	public void setClaimBankAccount(ClaimBankAccountDto claimBankAccount) {
		this.claimBankAccount = claimBankAccount;
	}

	public List<ClaimDocumentDto> getClaimDocuments() {
		return claimDocuments;
	}

	public void setClaimDocuments(List<ClaimDocumentDto> claimDocuments) {
		this.claimDocuments = claimDocuments;
	}

	public CoverageCategoryDto getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategoryDto coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	public OrderDto getOrder() {
		return order;
	}

	public void setOrder(OrderDto order) {
		this.order = order;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AccidentClaimDto [" + (claimId != null ? "claimId=" + claimId + ", " : "")
				+ (claimDate != null ? "claimDate=" + claimDate + ", " : "")
				+ (accidentDate != null ? "accidentDate=" + accidentDate + ", " : "")
				+ (accidentSummary != null ? "accidentSummary=" + accidentSummary + ", " : "")
				+ (accidentAddress != null ? "accidentAddress=" + accidentAddress + ", " : "")
				+ (claimCoverages != null ? "claimCoverages=" + claimCoverages + ", " : "")
				+ (claimBankAccount != null ? "claimBankAccount=" + claimBankAccount + ", " : "")
				+ (claimDocuments != null ? "claimDocuments=" + claimDocuments + ", " : "")
				+ (coverageCategory != null ? "coverageCategory=" + coverageCategory + ", " : "")
				+ (order != null ? "order=" + order + ", " : "") + (status != null ? "status=" + status : "") + "]";
	}

}
