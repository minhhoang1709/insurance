package com.ninelives.insurance.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.model.CoverageCategory;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageDto {
	private String coverageId;

    private String name;

    private String recommendation;

    private String description;

    private Boolean hasBeneficiary;
    
    private Long maxLimit;
    
    private Long familyMaxLimit;

    private Boolean isRecommended;
    
    private Boolean isIntroRecommended;
    
    private Boolean isLumpSum;
    
    private List<CoverageClaimDocTypeDto> coverageClaimDocTypes;
    
    private CoverageCategoryDto coverageCategory;
    
    private CoverageOptionDto coverageOption;
    
	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}
	
	public Long getFamilyMaxLimit() {
		return familyMaxLimit;
	}

	public void setFamilyMaxLimit(Long familyMaxLimit) {
		this.familyMaxLimit = familyMaxLimit;
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

	public Boolean getIsIntroRecommended() {
		return isIntroRecommended;
	}

	public void setIsIntroRecommended(Boolean isIntroRecommended) {
		this.isIntroRecommended = isIntroRecommended;
	}

	public Boolean getIsLumpSum() {
		return isLumpSum;
	}

	public void setIsLumpSum(Boolean isLumpSum) {
		this.isLumpSum = isLumpSum;
	}

	public List<CoverageClaimDocTypeDto> getCoverageClaimDocTypes() {
		return coverageClaimDocTypes;
	}

	public void setCoverageClaimDocTypes(List<CoverageClaimDocTypeDto> coverageClaimDocTypes) {
		this.coverageClaimDocTypes = coverageClaimDocTypes;
	}

	public CoverageCategoryDto getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategoryDto coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	public CoverageOptionDto getCoverageOption() {
		return coverageOption;
	}

	public void setCoverageOption(CoverageOptionDto coverageOption) {
		this.coverageOption = coverageOption;
	}

	@Override
	public String toString() {
		return "CoverageDto [coverageId=" + coverageId + ", name=" + name + "]";
	}
	
}
