package com.ninelives.insurance.api.model;

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
    
    //not-persisted
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
    
}