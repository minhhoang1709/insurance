package com.ninelives.insurance.api.model;

import java.time.LocalDate;

public class PolicyOrderCoverage {
	String orderId;
	String coverageId;
	String coverageName;
	LocalDate orderDate;
	LocalDate policyStartDate;
	LocalDate policyEndDate;
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
	public String getCoverageName() {
		return coverageName;
	}
	public void setCoverageName(String coverageName) {
		this.coverageName = coverageName;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
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
	@Override
	public String toString() {
		return "PolicyOrderCoverage [orderId=" + orderId + ", coverageId=" + coverageId + ", coverageName="
				+ coverageName + ", orderDate=" + orderDate + ", policyStartDate=" + policyStartDate
				+ ", policyEndDate=" + policyEndDate + "]";
	}
	
}
