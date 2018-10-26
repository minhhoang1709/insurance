package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocumentDto {	
	private UserFileDto file;
	private ClaimDocTypeDto claimDocType;	
	private Boolean isMandatory;
	private ClaimDocumentExtraDto extra;
	
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
	public Boolean getIsMandatory() {
		return isMandatory;
	}
	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((claimDocType == null) ? 0 : claimDocType.hashCode());
		result = prime * result + ((extra == null) ? 0 : extra.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((isMandatory == null) ? 0 : isMandatory.hashCode());
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
		ClaimDocumentDto other = (ClaimDocumentDto) obj;
		if (claimDocType == null) {
			if (other.claimDocType != null)
				return false;
		} else if (!claimDocType.equals(other.claimDocType))
			return false;
		if (extra == null) {
			if (other.extra != null)
				return false;
		} else if (!extra.equals(other.extra))
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (isMandatory == null) {
			if (other.isMandatory != null)
				return false;
		} else if (!isMandatory.equals(other.isMandatory))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ClaimDocumentDto [file=" + file + ", claimDocType=" + claimDocType + ", isMandatory=" + isMandatory
				+ ", extra=" + extra + "]";
	}
	
}
