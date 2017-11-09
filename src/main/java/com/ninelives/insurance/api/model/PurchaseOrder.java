package com.ninelives.insurance.api.model;

import java.util.Date;

public class PurchaseOrder {
    private String orderId;

    private Date orderDate;

    private Integer userId;

    private String policyNumber;

    private Date policyStartDate;

    private Date policyEndDate;

    private String period;

    private Integer totalPremi;

    private String hasBeneficiary;

    private Integer productCount;

    private String status;

    private Date createdDate;

    private Date updateDate;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Date getPolicyStartDate() {
        return policyStartDate;
    }

    public void setPolicyStartDate(Date policyStartDate) {
        this.policyStartDate = policyStartDate;
    }

    public Date getPolicyEndDate() {
        return policyEndDate;
    }

    public void setPolicyEndDate(Date policyEndDate) {
        this.policyEndDate = policyEndDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getTotalPremi() {
        return totalPremi;
    }

    public void setTotalPremi(Integer totalPremi) {
        this.totalPremi = totalPremi;
    }

    public String getHasBeneficiary() {
        return hasBeneficiary;
    }

    public void setHasBeneficiary(String hasBeneficiary) {
        this.hasBeneficiary = hasBeneficiary;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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