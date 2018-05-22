package com.ninelives.insurance.api.dto;

import com.ninelives.insurance.ref.CoverageCategoryType;

public class CoverageCategoryDto {
	private String coverageCategoryId;

    private String name;
    
    private String imageUrl;
    
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

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public CoverageCategoryType getType() {
		return type;
	}

	public void setType(CoverageCategoryType type) {
		this.type = type;
	}
    
	
    
}
