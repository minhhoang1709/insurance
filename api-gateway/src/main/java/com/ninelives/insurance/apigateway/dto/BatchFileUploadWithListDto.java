package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchFileUploadWithListDto {
	
	private String codeB2B;
	
	private String batchNumber;
	
	private String status;
	
	private List<String> listRow;
	
	private String userName;
	
	private String fileName;

	public String getCodeB2B() {
		return codeB2B;
	}

	public void setCodeB2B(String codeB2B) {
		this.codeB2B = codeB2B;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getListRow() {
		return listRow;
	}

	public void setListRow(List<String> listRow) {
		this.listRow = listRow;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "BatchFileUploadWithListDto [codeB2B=" + codeB2B + ", batchNumber=" + batchNumber + ", status=" + status
				+ ", listRow=" + listRow + ", userName=" + userName + ", fileName=" + fileName + "]";
	}
	
	
	
    
    }
