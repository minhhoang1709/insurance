package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String productId;

    private CoverageDto coverage;
    
    private PeriodDto period;

    private String name;

    private Integer premi;
    
    private Integer familyPremi;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public CoverageDto getCoverage() {
		return coverage;
	}

	public void setCoverage(CoverageDto coverage) {
		this.coverage = coverage;
	}

	public PeriodDto getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDto period) {
		this.period = period;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getPremi() {
		return premi;
	}

	public void setPremi(Integer premi) {
		this.premi = premi;
	}

	public Integer getFamilyPremi() {
		return familyPremi;
	}

	public void setFamilyPremi(Integer familyPremi) {
		this.familyPremi = familyPremi;
	}

	@Override
	public String toString() {
		return "ProductDto [productId=" + productId + ", coverage=" + coverage + ", period=" + period + ", name=" + name
				+ ", premi=" + premi + ", familyPremi=" + familyPremi + "]";
	}

	
}
