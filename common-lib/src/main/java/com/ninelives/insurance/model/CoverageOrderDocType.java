package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CoverageOrderDocType implements Serializable{
	private static final long serialVersionUID = -7530892863242118523L;

	private Integer coverageOrderDocTypeId;

    private String coverageId;

    private String orderDocTypeId;
    
    private OrderDocType orderDocType;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

	public Integer getCoverageOrderDocTypeId() {
		return coverageOrderDocTypeId;
	}

	public void setCoverageOrderDocTypeId(Integer coverageOrderDocTypeId) {
		this.coverageOrderDocTypeId = coverageOrderDocTypeId;
	}

	public String getCoverageId() {
		return coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
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
