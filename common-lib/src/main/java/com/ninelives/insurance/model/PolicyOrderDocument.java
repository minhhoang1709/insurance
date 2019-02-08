package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PolicyOrderDocument implements Serializable{
	private static final long serialVersionUID = 6214898216336093601L;

	private Long orderDocumentId;

    private String orderId;

    private String orderDocTypeId;
    
    private OrderDocType orderDocType;

    private Long fileId;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private UserFile userFile;

    public Long getOrderDocumentId() {
        return orderDocumentId;
    }

    public void setOrderDocumentId(Long orderDocumentId) {
        this.orderDocumentId = orderDocumentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDocTypeId() {
        return orderDocTypeId;
    }

    public void setOrderDocTypeId(String orderDocTypeId) {
        this.orderDocTypeId = orderDocTypeId;
    }

    public OrderDocType getOrderDocType() {
		return orderDocType;
	}

	public void setOrderDocType(OrderDocType orderDocType) {
		this.orderDocType = orderDocType;
	}

	public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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

	public UserFile getUserFile() {
		return userFile;
	}

	public void setUserFile(UserFile userFile) {
		this.userFile = userFile;
	}
    
}