package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.ninelives.insurance.ref.ProductType;

public class Product implements Serializable{
	private static final long serialVersionUID = -1106804861994184823L;

	private String productId;

    private String coverageId;

    private String periodId;

    private String name;

    private Integer premi;
    
    private Integer basePremi;

    private String status;
    
    private ProductType productType;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
    
    private Period period;
    
    private Coverage coverage;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPremi() {
        return premi;
    }

    public void setPremi(Integer premi) {
        this.premi = premi;
    }
    
    public Integer getBasePremi() {
		return basePremi;
	}

	public void setBasePremi(Integer basePremi) {
		this.basePremi = basePremi;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
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

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Coverage getCoverage() {
		return coverage;
	}

	public void setCoverage(Coverage coverage) {
		this.coverage = coverage;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", coverageId=" + coverageId + ", periodId=" + periodId + ", name="
				+ name + ", premi=" + premi + ", basePremi=" + basePremi + ", status=" + status + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + ", period=" + period + ", coverage=" + coverage + "]";
	}


    
}