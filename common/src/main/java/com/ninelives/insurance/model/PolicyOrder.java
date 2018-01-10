package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ninelives.insurance.ref.PolicyStatus;

public class PolicyOrder implements Serializable{
	private static final long serialVersionUID = 592941652403498857L;

	private String orderId;

    private LocalDate orderDate;
	
    private LocalDateTime orderTime;

    private String userId;

    private String coverageCategoryId;

    private Boolean hasBeneficiary;

    private String periodId;

    private String policyNumber;

    private LocalDate policyStartDate;

    private LocalDate policyEndDate;

    private Integer basePremi;
    
    private Integer totalPremi;

    private Integer productCount;
    
    private Boolean hasVoucher;
    
    private String providerOrderNumber;

    private PolicyStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
        
    private CoverageCategory coverageCategory;
    
    private PolicyOrderUsers policyOrderUsers;
    
    private List<PolicyOrderProduct> policyOrderProducts;
    
    private PolicyOrderBeneficiary policyOrderBeneficiary;
    
    private Period period;

    private PolicyPayment payment;
    
    private PolicyOrderVoucher policyOrderVoucher;
    
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

	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
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
	
	public Integer getBasePremi() {
		return basePremi;
	}

	public void setBasePremi(Integer basePremi) {
		this.basePremi = basePremi;
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
	
	public Boolean getHasVoucher() {
		return hasVoucher;
	}

	public void setHasVoucher(Boolean hasVoucher) {
		this.hasVoucher = hasVoucher;
	}

	public String getProviderOrderNumber() {
		return providerOrderNumber;
	}

	public void setProviderOrderNumber(String providerOrderNumber) {
		this.providerOrderNumber = providerOrderNumber;
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

	public PolicyPayment getPayment() {
		return payment;
	}

	public void setPayment(PolicyPayment payment) {
		this.payment = payment;
	}

	public PolicyOrderVoucher getPolicyOrderVoucher() {
		return policyOrderVoucher;
	}

	public void setPolicyOrderVoucher(PolicyOrderVoucher policyOrderVoucher) {
		this.policyOrderVoucher = policyOrderVoucher;
	}

	@Override
	public String toString() {
		return "PolicyOrder [orderId=" + orderId + ", orderDate=" + orderDate + ", orderTime=" + orderTime + ", userId="
				+ userId + ", coverageCategoryId=" + coverageCategoryId + ", hasBeneficiary=" + hasBeneficiary
				+ ", periodId=" + periodId + ", policyNumber=" + policyNumber + ", policyStartDate=" + policyStartDate
				+ ", policyEndDate=" + policyEndDate + ", basePremi=" + basePremi + ", totalPremi=" + totalPremi
				+ ", productCount=" + productCount + ", hasVoucher=" + hasVoucher + ", providerOrderNumber="
				+ providerOrderNumber + ", status=" + status + ", createdDate=" + createdDate + ", updateDate="
				+ updateDate + ", coverageCategory=" + coverageCategory + ", policyOrderUsers=" + policyOrderUsers
				+ ", policyOrderProducts=" + policyOrderProducts + ", policyOrderBeneficiary=" + policyOrderBeneficiary
				+ ", period=" + period + ", payment=" + payment + ", policyOrderVoucher=" + policyOrderVoucher + "]";
	}
}