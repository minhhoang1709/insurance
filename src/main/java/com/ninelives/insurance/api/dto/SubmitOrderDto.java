package com.ninelives.insurance.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SubmitOrderDto {
	List<String> products;
	int totalPremi;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	LocalDate policyStartDate;
	
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
	public LocalDate getPolicyStartDate() {
		return policyStartDate;
	}
	public void setPolicyStartDate(LocalDate policyStartDate) {
		this.policyStartDate = policyStartDate;
	}
	@Override
	public String toString() {
		return "SubmitOrderDto [products=" + products + ", totalPremi=" + totalPremi + ", policyStartDate="
				+ policyStartDate + "]";
	}
	
	
}
