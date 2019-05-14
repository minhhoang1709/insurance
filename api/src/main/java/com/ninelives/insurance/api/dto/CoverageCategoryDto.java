package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.CoverageCategoryType;
import com.ninelives.insurance.ref.InsurerCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageCategoryDto {
	private String coverageCategoryId;

    private String name;
    
    private String recommendation;
    
    private String imageUrl;
    
    private String recommendationImageUrl;
    
    private InsurerCode providerCode;
    
    private CoverageCategoryType type;

	public String getCoverageCategoryId() {
		return coverageCategoryId;
	}

	public void setCoverageCategoryId(String coverageCategoryId) {
		this.coverageCategoryId = coverageCategoryId;
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getRecommendationImageUrl() {
		return recommendationImageUrl;
	}

	public void setRecommendationImageUrl(String recommendationImageUrl) {
		this.recommendationImageUrl = recommendationImageUrl;
	}

	public CoverageCategoryType getType() {
		return type;
	}

	public void setType(CoverageCategoryType type) {
		this.type = type;
	}

	public InsurerCode getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(InsurerCode providerCode) {
		this.providerCode = providerCode;
	}
    
}
