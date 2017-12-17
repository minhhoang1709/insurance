package com.ninelives.insurance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.ninelives.insurance.ref.FileUseType;
import com.ninelives.insurance.ref.UserFileStatus;

public class UserFile {
    private Long fileId;

    private String userId;

    private FileUseType fileUseType;

    private String filePath;

    private UserFileStatus status;

    private Long fileSize;

    private String contentType;

    private LocalDate uploadDate;

    private Date createDate;

    private LocalDateTime updateDate;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FileUseType getFileUseType() {
		return fileUseType;
	}

	public void setFileUseType(FileUseType fileUseType) {
		this.fileUseType = fileUseType;
	}

	public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public UserFileStatus getStatus() {
		return status;
	}

	public void setStatus(UserFileStatus status) {
		this.status = status;
	}

	public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

	@Override
	public String toString() {
		return "UserFile [" + (fileId != null ? "fileId=" + fileId + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (fileUseType != null ? "fileUseType=" + fileUseType + ", " : "")
				+ (filePath != null ? "filePath=" + filePath + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (fileSize != null ? "fileSize=" + fileSize + ", " : "")
				+ (contentType != null ? "contentType=" + contentType + ", " : "")
				+ (uploadDate != null ? "uploadDate=" + uploadDate + ", " : "")
				+ (createDate != null ? "createDate=" + createDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "") + "]";
	}
    
}