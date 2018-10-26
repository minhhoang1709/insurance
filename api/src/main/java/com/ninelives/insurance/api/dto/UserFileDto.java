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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
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
		UserFileDto other = (UserFileDto) obj;
		if (fileId == null) {
			if (other.fileId != null)
				return false;
		} else if (!fileId.equals(other.fileId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "UserFileDto [fileId=" + fileId + "]";
	}
	
}
