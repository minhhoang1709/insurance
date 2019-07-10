package com.ninelives.insurance.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class VoucherProduct implements Serializable{
	private static final long serialVersionUID = -5288164944287308416L;

	private Long id;

    private Integer voucherId;
    
    private String productId;
    
    private Integer premi;
    
    private Timestamp createdDate;
    
    private Timestamp updateDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getPremi() {
		return premi;
	}

	public void setPremi(Integer premi) {
		this.premi = premi;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "VoucherProduct [id=" + id + ", voucherId=" + voucherId + ", productId=" + productId + ", premi=" + premi
				+ ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}
    
    
    
    
}
