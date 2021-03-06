package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ninelives.insurance.ref.PolicyStatus;

public class PolicyOrder implements Serializable{
	private static final long serialVersionUID = 6041526120240739297L;

	private String orderId;
	
	private String orderIdMap;

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
    
    private String providerDownloadUrl;

    private PolicyStatus status;
    
    private Boolean isFamily;
    
    private Boolean isHide;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
        
    private CoverageCategory coverageCategory;
    
    private PolicyOrderUsers policyOrderUsers;
    
    private List<PolicyOrderProduct> policyOrderProducts;
    
    private PolicyOrderBeneficiary policyOrderBeneficiary;
    
    private List<PolicyOrderFamily> policyOrderFamilies;
    
    private Period period;    

    private PolicyPayment payment;
    
    private PolicyOrderVoucher policyOrderVoucher;
    
    private List<PolicyOrderDocument> policyOrderDocuments;
    
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
	
	public String getProviderDownloadUrl() {
		return providerDownloadUrl;
	}

	public void setProviderDownloadUrl(String providerDownloadUrl) {
		this.providerDownloadUrl = providerDownloadUrl;
	}

	
	public Boolean getIsFamily() {
		return isFamily;
	}

	public void setIsFamily(Boolean isFamily) {
		this.isFamily = isFamily;
	}		

	public Boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(Boolean isHide) {
		this.isHide = isHide;
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

	public List<PolicyOrderFamily> getPolicyOrderFamilies() {
		return policyOrderFamilies;
	}

	public void setPolicyOrderFamilies(List<PolicyOrderFamily> policyOrderFamilies) {
		this.policyOrderFamilies = policyOrderFamilies;
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

	public List<PolicyOrderDocument> getPolicyOrderDocuments() {
		return policyOrderDocuments;
	}

	public void setPolicyOrderDocuments(List<PolicyOrderDocument> policyOrderDocuments) {
		this.policyOrderDocuments = policyOrderDocuments;
	}

	@Override
	public String toString() {
		return "PolicyOrder [orderId=" + orderId + ", orderIdMap=" + orderIdMap + ", orderDate=" + orderDate
				+ ", orderTime=" + orderTime + ", userId=" + userId + ", coverageCategoryId=" + coverageCategoryId
				+ ", hasBeneficiary=" + hasBeneficiary + ", periodId=" + periodId + ", policyNumber=" + policyNumber
				+ ", policyStartDate=" + policyStartDate + ", policyEndDate=" + policyEndDate + ", basePremi="
				+ basePremi + ", totalPremi=" + totalPremi + ", productCount=" + productCount + ", hasVoucher="
				+ hasVoucher + ", providerOrderNumber=" + providerOrderNumber + ", providerDownloadUrl="
				+ providerDownloadUrl + ", status=" + status + ", isFamily=" + isFamily + ", isHide=" + isHide
				+ ", createdDate=" + createdDate + ", updateDate=" + updateDate + ", coverageCategory="
				+ coverageCategory + ", policyOrderUsers=" + policyOrderUsers + ", policyOrderProducts="
				+ policyOrderProducts + ", policyOrderBeneficiary=" + policyOrderBeneficiary + ", policyOrderFamilies="
				+ policyOrderFamilies + ", period=" + period + ", payment=" + payment + ", policyOrderVoucher="
				+ policyOrderVoucher + ", policyOrderDocuments=" + policyOrderDocuments + "]";
	}

	public String getOrderIdMap() {
		return orderIdMap;
	}

	public void setOrderIdMap(String orderIdMap) {
		this.orderIdMap = orderIdMap;
	}

	
//	@Override
//	public String toString() {
//		return "PolicyOrder [orderId=" + orderId + ", orderDate=" + orderDate + ", orderTime=" + orderTime + ", userId="
//				+ userId + ", coverageCategoryId=" + coverageCategoryId + ", hasBeneficiary=" + hasBeneficiary
//				+ ", periodId=" + periodId + ", policyNumber=" + policyNumber + ", policyStartDate=" + policyStartDate
//				+ ", policyEndDate=" + policyEndDate + ", basePremi=" + basePremi + ", totalPremi=" + totalPremi
//				+ ", productCount=" + productCount + ", hasVoucher=" + hasVoucher + ", providerOrderNumber="
//				+ providerOrderNumber + ", status=" + status + ", createdDate=" + createdDate + ", updateDate="
//				+ updateDate + ", coverageCategory=" + coverageCategory + ", policyOrderUsers=" + policyOrderUsers
//				+ ", policyOrderProducts=" + policyOrderProducts + ", policyOrderBeneficiary=" + policyOrderBeneficiary
//				+ ", period=" + period + ", payment=" + payment + ", policyOrderVoucher=" + policyOrderVoucher + "]";
//	}
}