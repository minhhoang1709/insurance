package com.ninelives.insurance.api.model;

import java.util.List;

public class PolicyOrderProduct {
    private Long orderProductId;

    private String orderId;

    private String coverageId;

    private String periodId;

    private String productId;

    private String coverageName;

    private Long coverageMaxLimit;

    private Boolean coverageHasBeneficiary;

    private Integer premi;
    
    private Period period;
    
    //transient
    private List<ClaimDocType> claimDocTypes;
    
    //transient
    private int coverageDisplayRank;

    public Long getOrderProductId() {
        return orderProductId;
    }

    public void setOrderProductId(Long orderProductId) {
        this.orderProductId = orderProductId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCoverageName() {
        return coverageName;
    }

    public void setCoverageName(String coverageName) {
        this.coverageName = coverageName;
    }

    public Long getCoverageMaxLimit() {
        return coverageMaxLimit;
    }

    public void setCoverageMaxLimit(Long coverageMaxLimit) {
        this.coverageMaxLimit = coverageMaxLimit;
    }

    public Boolean getCoverageHasBeneficiary() {
        return coverageHasBeneficiary;
    }

    public void setCoverageHasBeneficiary(Boolean coverageHasBeneficiary) {
        this.coverageHasBeneficiary = coverageHasBeneficiary;
    }

    public Integer getPremi() {
        return premi;
    }

    public void setPremi(Integer premi) {
        this.premi = premi;
    }

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public int getCoverageDisplayRank() {
		return coverageDisplayRank;
	}

	public void setCoverageDisplayRank(int coverageDisplayRank) {
		this.coverageDisplayRank = coverageDisplayRank;
	}
	
	public List<ClaimDocType> getClaimDocTypes() {
		return claimDocTypes;
	}

	public void setClaimDocTypes(List<ClaimDocType> claimDocTypes) {
		this.claimDocTypes = claimDocTypes;
	}

	@Override
	public String toString() {
		return "PolicyOrderProduct [" + (orderProductId != null ? "orderProductId=" + orderProductId + ", " : "")
				+ (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (coverageId != null ? "coverageId=" + coverageId + ", " : "")
				+ (periodId != null ? "periodId=" + periodId + ", " : "")
				+ (productId != null ? "productId=" + productId + ", " : "")
				+ (coverageName != null ? "coverageName=" + coverageName + ", " : "")
				+ (coverageMaxLimit != null ? "coverageMaxLimit=" + coverageMaxLimit + ", " : "")
				+ (coverageHasBeneficiary != null ? "coverageHasBeneficiary=" + coverageHasBeneficiary + ", " : "")
				+ (premi != null ? "premi=" + premi + ", " : "") + (period != null ? "period=" + period + ", " : "")
				+ (claimDocTypes != null ? "claimDocTypes=" + claimDocTypes + ", " : "") + "coverageDisplayRank="
				+ coverageDisplayRank + "]";
	}
	
	
    
}