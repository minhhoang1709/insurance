package com.ninelives.insurance.api.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ninelives.insurance.api.ref.PolicyStatus;

public class PolicyOrder {
    private String orderId;

    private LocalDate orderDate;

    private String userId;

    private String coverageCategoryId;

    private Boolean hasBeneficiary;

    private String periodId;

    private String policyNumber;

    private LocalDate policyStartDate;

    private LocalDate policyEndDate;

    private Integer totalPremi;

    private Integer productCount;

    private PolicyStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
        
    private CoverageCategory coverageCategory;
    
    private PolicyOrderUsers policyOrderUsers;
    
    private List<PolicyOrderProduct> policyOrderProducts;
    
    private PolicyOrderBeneficiary policyOrderBeneficiary;
    
    private Period period;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCoverageCategoryId() {
		return coverageCategoryId;
	}

	public void setCoverageCategoryId(String coverageCategoryId) {
		this.coverageCategoryId = coverageCategoryId;
	}

	public Boolean getHasBeneficiary() {
		return hasBeneficiary;
	}

	public void setHasBeneficiary(Boolean hasBeneficiary) {
		this.hasBeneficiary = hasBeneficiary;
	}

	public String getPeriodId() {
		return periodId;
	}

	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public LocalDate getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(LocalDate policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public LocalDate getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(LocalDate policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public Integer getTotalPremi() {
		return totalPremi;
	}

	public void setTotalPremi(Integer totalPremi) {
		this.totalPremi = totalPremi;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public PolicyStatus getStatus() {
		return status;
	}

	public void setStatus(PolicyStatus status) {
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
	
	public CoverageCategory getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategory coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	public PolicyOrderUsers getPolicyOrderUsers() {
		return policyOrderUsers;
	}

	public void setPolicyOrderUsers(PolicyOrderUsers policyOrderUsers) {
		this.policyOrderUsers = policyOrderUsers;
	}		

	public List<PolicyOrderProduct> getPolicyOrderProducts() {
		return policyOrderProducts;
	}

	public void setPolicyOrderProducts(List<PolicyOrderProduct> policyOrderProducts) {
		this.policyOrderProducts = policyOrderProducts;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
	
	public PolicyOrderBeneficiary getPolicyOrderBeneficiary() {
		return policyOrderBeneficiary;
	}

	public void setPolicyOrderBeneficiary(PolicyOrderBeneficiary policyOrderBeneficiary) {
		this.policyOrderBeneficiary = policyOrderBeneficiary;
	}

	@Override
	public String toString() {
		return "PolicyOrder [" + (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (orderDate != null ? "orderDate=" + orderDate + ", " : "")
				+ (userId != null ? "userId=" + userId + ", " : "")
				+ (coverageCategoryId != null ? "coverageCategoryId=" + coverageCategoryId + ", " : "")
				+ (hasBeneficiary != null ? "hasBeneficiary=" + hasBeneficiary + ", " : "")
				+ (periodId != null ? "periodId=" + periodId + ", " : "")
				+ (policyNumber != null ? "policyNumber=" + policyNumber + ", " : "")
				+ (policyStartDate != null ? "policyStartDate=" + policyStartDate + ", " : "")
				+ (policyEndDate != null ? "policyEndDate=" + policyEndDate + ", " : "")
				+ (totalPremi != null ? "totalPremi=" + totalPremi + ", " : "")
				+ (productCount != null ? "productCount=" + productCount + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate + ", " : "")
				+ (policyOrderUsers != null ? "policyOrderUsers=" + policyOrderUsers + ", " : "")
				+ (policyOrderProducts != null ? "policyOrderProducts=" + policyOrderProducts + ", " : "")
				+ (policyOrderBeneficiary != null ? "policyOrderBeneficiary=" + policyOrderBeneficiary + ", " : "")
				+ (period != null ? "period=" + period : "") + "]";
	}
	

}