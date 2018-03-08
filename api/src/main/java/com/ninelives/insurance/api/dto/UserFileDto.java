package com.ninelives.insurance.api.dto;

public class UserFileDto {
	private Long fileId;
	
	public UserFileDto(){
		
	}
	public UserFileDto(Long fileId) {
		this.fileId = fileId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	
}
