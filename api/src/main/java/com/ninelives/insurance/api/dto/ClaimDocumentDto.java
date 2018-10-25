package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocumentDto {
	private Long claimDocumentId;
	private UserFileDto file;
	private ClaimDocTypeDto claimDocType;	
	private ClaimDocumentExtraDto extra;	

	public Long getClaimDocumentId() {
		return claimDocumentId;
	}
	public void setClaimDocumentId(Long claimDocumentId) {
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
	public ClaimDocumentExtraDto getExtra() {
		return extra;
	}
	public void setExtra(ClaimDocumentExtraDto extra) {
		this.extra = extra;
	}
	@Override
	public String toString() {
		return "ClaimDocumentDto [claimDocumentId=" + claimDocumentId + ", file=" + file + ", claimDocType="
				+ claimDocType + ", extra=" + extra + "]";
	}
	
}
