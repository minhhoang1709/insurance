package com.ninelives.insurance.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class B2bTransactionDto {
	
	
	private String productId;
	
	private String coverageName;
	
	private String premi;
	
	private String coverageMaxLimit;

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

	public String getPremi() {
		return premi;
	}

	public void setPremi(String premi) {
		this.premi = premi;
	}

	public String getCoverageMaxLimit() {
		return coverageMaxLimit;
	}

	public void setCoverageMaxLimit(String coverageMaxLimit) {
		this.coverageMaxLimit = coverageMaxLimit;
	}

	@Override
	public String toString() {
		return "B2bTransactionDto [productId=" + productId + ", coverageName=" + coverageName + ", premi=" + premi
				+ ", coverageMaxLimit=" + coverageMaxLimit + "]";
	}
	
	
    
}
