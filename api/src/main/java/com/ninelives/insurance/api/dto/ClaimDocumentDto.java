package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocumentDto {
	private Long claimDocumentId;
	private UserFileDto file;
	private ClaimDocTypeDto claimDocType;
	
	private Object extra;	

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
	
	@JsonRawValue
	public String getExtra() {
		return extra == null ? null : extra.toString();
	}	
	
	public void setExtra(JsonNode extra) {
		this.extra = extra;
	}
	
	public void setExtraAsString(String extra) {
		this.extra = extra;
	}
	
	@Override
	public String toString() {
		return "ClaimDocumentDto [claimDocumentId=" + claimDocumentId + ", file=" + file + ", claimDocType="
				+ claimDocType + ", extra=" + extra + "]";
	}
	
}
