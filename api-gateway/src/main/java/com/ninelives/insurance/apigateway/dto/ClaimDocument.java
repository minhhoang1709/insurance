package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocument {
	
	private String ninelivesDocumentTypeId;
	
	private String documentName;
	
	private String filePath;

	public String getNinelivesDocumentTypeId() {
		return ninelivesDocumentTypeId;
	}

	public void setNinelivesDocumentTypeId(String ninelivesDocumentTypeId) {
		this.ninelivesDocumentTypeId = ninelivesDocumentTypeId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "ClaimDocument [ninelivesDocumentTypeId=" + ninelivesDocumentTypeId + ", documentName=" + documentName
				+ ", filePath=" + filePath + "]";
	}
	
	
	
	
}
