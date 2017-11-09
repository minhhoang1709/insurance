package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageDto {
	private String coverageId;

    private String name;

    private String recommendation;

    private String description;

    private String hasBeneficiary;
    
    private Long maxLimit;

    
	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getCoverageId() {
		return coverageId;
	}

	public void setCoverageId(String coverageId) {
		this.coverageId = coverageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHasBeneficiary() {
		return hasBeneficiary;
	}

	public void setHasBeneficiary(String hasBeneficiary) {
		this.hasBeneficiary = hasBeneficiary;
	}


}
