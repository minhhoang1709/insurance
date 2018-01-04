package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PolicyClaimDocument implements Serializable{
	private static final long serialVersionUID = 3491295335883450965L;

	private Long claimDocumentId;

    private String claimId;

    private String claimDocTypeId;
    
    private Long fileId;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private ClaimDocType claimDocType;
    
    private UserFile userFile;

    public Long getClaimDocumentId() {
        return claimDocumentId;
    }

    public void setClaimDocumentId(Long claimDocumentId) {
        this.claimDocumentId = claimDocumentId;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getClaimDocTypeId() {
        return claimDocTypeId;
    }

    public void setClaimDocTypeId(String claimDocTypeId) {
        this.claimDocTypeId = claimDocTypeId;
    }

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public ClaimDocType getClaimDocType() {
		return claimDocType;
	}

	public void setClaimDocType(ClaimDocType claimDocType) {
		this.claimDocType = claimDocType;
	}
	
	public UserFile getUserFile() {
		return userFile;
	}

	public void setUserFile(UserFile userFile) {
		this.userFile = userFile;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}