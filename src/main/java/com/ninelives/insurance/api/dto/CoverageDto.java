package com.ninelives.insurance.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageDto {
	private String coverageId;

    private String name;

    private String recommendation;

    private String description;

    private Boolean hasBeneficiary;
    
    private Long maxLimit;

    private Boolean isRecommended;
    
    private List<ClaimDocTypeDto> claimDocTypes;
    
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

	public Boolean getHasBeneficiary() {
		return hasBeneficiary;
	}

	public void setHasBeneficiary(Boolean hasBeneficiary) {
		this.hasBeneficiary = hasBeneficiary;
	}

	public Boolean getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(Boolean isRecommended) {
		this.isRecommended = isRecommended;
	}

	public List<ClaimDocTypeDto> getClaimDocTypes() {
		return claimDocTypes;
	}

	public void setClaimDocTypes(List<ClaimDocTypeDto> claimDocTypes) {
		this.claimDocTypes = claimDocTypes;
	}


}
