package com.ninelives.insurance.api.dto;

import java.util.List;

public class SubmitOrderDto {
	List<String> products;
	int totalPremi;
	String policyStartDate;
	
	public List<String> getProducts() {
		return products;
	}
	public void setProducts(List<String> products) {
		this.products = products;
	}
	public int getTotalPremi() {
		return totalPremi;
	}
	public void setTotalPremi(int totalPremi) {
		this.totalPremi = totalPremi;
	}
	public String getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(String policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	
}
