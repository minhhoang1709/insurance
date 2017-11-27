package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocumentDto {
	private String claimDocumentId;
	private UserFileDto file;
	private ClaimDocTypeDto claimDocType;
	
	public String getClaimDocumentId() {
		return claimDocumentId;
	}
	public void setClaimDocumentId(String claimDocumentId) {
		this.claimDocumentId = claimDocumentId;
	}
	public UserFileDto getFile() {
		return file;
	}
	public void setFile(UserFileDto file) {
		this.file = file;
	}
	public ClaimDocTypeDto getClaimDocType() {
		return claimDocType;
	}
	public void setClaimDocType(ClaimDocTypeDto claimDocType) {
		this.claimDocType = claimDocType;
	}
	
}
