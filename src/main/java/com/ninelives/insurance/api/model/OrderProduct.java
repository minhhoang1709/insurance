package com.ninelives.insurance.api.model;

import java.util.Date;

public class OrderProduct {
    private Long orderProductId;

    private String orderId;

    private String coverageId;

    private String periodId;

    private String productId;

    private String coverageName;

    private String coverageHasBeneficiary;

    private Long coverageMaxLimit;

    private Integer premi;

    private Date createdDate;

    private Date updateDate;

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

    public String getCoverageHasBeneficiary() {
        return coverageHasBeneficiary;
    }

    public void setCoverageHasBeneficiary(String coverageHasBeneficiary) {
        this.coverageHasBeneficiary = coverageHasBeneficiary;
    }

    public Long getCoverageMaxLimit() {
        return coverageMaxLimit;
    }

    public void setCoverageMaxLimit(Long coverageMaxLimit) {
        this.coverageMaxLimit = coverageMaxLimit;
    }

    public Integer getPremi() {
        return premi;
    }

    public void setPremi(Integer premi) {
        this.premi = premi;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}