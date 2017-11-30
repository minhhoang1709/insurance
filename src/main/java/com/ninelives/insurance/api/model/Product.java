package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Product {
    private String productId;

    private String coverageId;

    private String periodId;

    private String name;

    private Integer premi;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
		return "Product [" + (productId != null ? "productId=" + productId + ", " : "")
				+ (coverageId != null ? "coverageId=" + coverageId + ", " : "")
				+ (periodId != null ? "periodId=" + periodId + ", " : "") + (name != null ? "name=" + name + ", " : "")
				+ (premi != null ? "premi=" + premi + ", " : "") + (status != null ? "status=" + status + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate + ", " : "")
				+ (period != null ? "period=" + period + ", " : "") + (coverage != null ? "coverage=" + coverage : "")
				+ "]";
	}
    
}