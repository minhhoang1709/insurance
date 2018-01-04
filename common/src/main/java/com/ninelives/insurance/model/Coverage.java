package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Coverage implements Serializable{
    private static final long serialVersionUID = -6140177440706720507L;

	private String coverageId;

    private String coverageCategoryId;

    private String name;

    private String recommendation;

    private String description;

    private Long maxLimit;

    private Boolean hasBeneficiary;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private String status;
    
    private int displayRank;
    
    private Boolean isRecommended;
    
    private List<CoverageClaimDocType> coverageClaimDocTypes;
    
    private CoverageCategory coverageCategory;

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }

    public Boolean getHasBeneficiary() {
        return hasBeneficiary;
    }

    public void setHasBeneficiary(Boolean hasBeneficiary) {
        this.hasBeneficiary = hasBeneficiary;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public int getDisplayRank() {
		return displayRank;
	}

	public void setDisplayRank(int displayRank) {
		this.displayRank = displayRank;
	}

	public Boolean getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(Boolean isRecommended) {
		this.isRecommended = isRecommended;
	}

	public List<CoverageClaimDocType> getCoverageClaimDocTypes() {
		return coverageClaimDocTypes;
	}

	public void setCoverageClaimDocTypes(List<CoverageClaimDocType> coverageClaimDocTypes) {
		this.coverageClaimDocTypes = coverageClaimDocTypes;
	}

	public CoverageCategory getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategory coverageCategory) {
		this.coverageCategory = coverageCategory;
	}

	@Override
	public String toString() {
		return "Coverage [" + (coverageId != null ? "coverageId=" + coverageId + ", " : "")
				+ (coverageCategoryId != null ? "coverageCategoryId=" + coverageCategoryId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (recommendation != null ? "recommendation=" + recommendation + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (maxLimit != null ? "maxLimit=" + maxLimit + ", " : "")
				+ (hasBeneficiary != null ? "hasBeneficiary=" + hasBeneficiary + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate + ", " : "")
				+ (status != null ? "status=" + status + ", " : "") + "displayRank=" + displayRank + ", "
				+ (isRecommended != null ? "isRecommended=" + isRecommended + ", " : "")
				+ (coverageClaimDocTypes != null ? "coverageClaimDocType=" + coverageClaimDocTypes + ", " : "")
				+ (coverageCategory != null ? "coverageCategory=" + coverageCategory : "") + "]";
	}

	
}