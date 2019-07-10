package com.ninelives.insurance.apigateway.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bTransactionListDto {
	
    private int recordID;
    
    private String orderID;
    
    private String orderDate;
    
    private String email;
    
    private String name;
    
    private String coverageType;
    
    private String period;
    
    private String policyStartDate;
    
    private String policyEndDate;
    
    private String totalPremi;
    
    private int productCount;
    
    private String status;
    
    private String policyNumber;
    
    private String providerPolicyUrl;
    
    private List<B2bTransactionDto> orders;

	public int getRecordID() {
		return recordID;
	}

	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPolicyEndDate() {
		return policyEndDate;
	}

	public void setPolicyEndDate(String policyEndDate) {
		this.policyEndDate = policyEndDate;
	}

	public String getTotalPremi() {
		return totalPremi;
	}

	public void setTotalPremi(String totalPremi) {
		this.totalPremi = totalPremi;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPolicyNumber() {
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber) {
		this.policyNumber = policyNumber;
	}

	public String getProviderPolicyUrl() {
		return providerPolicyUrl;
	}

	public void setProviderPolicyUrl(String providerPolicyUrl) {
		this.providerPolicyUrl = providerPolicyUrl;
	}

	public List<B2bTransactionDto> getOrders() {
		return orders;
	}

	public void setOrders(List<B2bTransactionDto> orders) {
		this.orders = orders;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	@Override
	public String toString() {
		return "B2bTransactionListDto [recordID=" + recordID + ", orderID=" + orderID + ", orderDate=" + orderDate
				+ ", email=" + email + ", name=" + name + ", coverageType=" + coverageType + ", period=" + period
				+ ", policyStartDate=" + policyStartDate + ", policyEndDate=" + policyEndDate + ", totalPremi="
				+ totalPremi + ", productCount=" + productCount + ", status=" + status + ", policyNumber="
				+ policyNumber + ", providerPolicyUrl=" + providerPolicyUrl + ", orders=" + orders + "]";
	}

	

}
