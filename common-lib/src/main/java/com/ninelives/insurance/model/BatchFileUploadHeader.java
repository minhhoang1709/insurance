package com.ninelives.insurance.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class BatchFileUploadHeader implements Serializable{
	private static final long serialVersionUID = -1479117497582010023L;

	private Long id;

    private String batchNumber;
    
    private String fileName;
    
   	private String rowValid;
    
    private String rowInvalid;
    
    private String totalRow;
    
    private String status;
    
    private Timestamp uploadBegin;
    
    private Timestamp uploadEnd;
    
    private Timestamp createdDate;

    private String createdBy;
    
    private Timestamp modifiedDate;
    
    private String modifiedBy; 
    
    private int voucherId;

	

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getRowValid() {
		return rowValid;
	}

	public void setRowValid(String rowValid) {
		this.rowValid = rowValid;
	}

	public String getRowInvalid() {
		return rowInvalid;
	}

	public void setRowInvalid(String rowInvalid) {
		this.rowInvalid = rowInvalid;
	}

	public String getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(String totalRow) {
		this.totalRow = totalRow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUploadBegin() {
		return uploadBegin;
	}

	public void setUploadBegin(Timestamp uploadBegin) {
		this.uploadBegin = uploadBegin;
	}

	public Timestamp getUploadEnd() {
		return uploadEnd;
	}

	public void setUploadEnd(Timestamp uploadEnd) {
		this.uploadEnd = uploadEnd;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "BatchFileUploadHeader [id=" + id + ", batchNumber=" + batchNumber + ", fileName=" + fileName
				+ ", rowValid=" + rowValid + ", rowInvalid=" + rowInvalid + ", totalRow=" + totalRow + ", status="
				+ status + ", uploadBegin=" + uploadBegin + ", uploadEnd=" + uploadEnd + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedDate=" + modifiedDate + ", modifiedBy=" + modifiedBy + "]";
	}

	public int getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(int voucherId) {
		this.voucherId = voucherId;
	}

	
    
	    
    	
    
}
