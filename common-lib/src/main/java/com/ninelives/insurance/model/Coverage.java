package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Coverage implements Serializable{
    private static final long serialVersionUID = -6140177440706720507L;

	private String coverageId;

    private String coverageCategoryId;

    private String name;

    private String recommendation;

    private String description;

    private Long maxLimit;
    
    private Long familyMaxLimit;

    private Boolean hasBeneficiary;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    private String status;
    
    private int displayRank;
    
    private Boolean isRecommended;
    
    private Boolean isIntroRecommended; //if true then display in wizard
        
    private Boolean isLumpSum; //if true then claim will be paid for all family member (no need family selection) 
    
    private String providerCode;
    
    private String coverageOptionId;
    
    private List<CoverageClaimDocType> coverageClaimDocTypes;
    
    private List<CoverageOrderDocType> coverageOrderDocTypes;
    
    private CoverageCategory coverageCategory;
    
    private CoverageOption coverageOption;

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

    public Long getFamilyMaxLimit() {
		return familyMaxLimit;
	}

	public void setFamilyMaxLimit(Long familyMaxLimit) {
		this.familyMaxLimit = familyMaxLimit;
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

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}

	public List<CoverageClaimDocType> getCoverageClaimDocTypes() {
		return coverageClaimDocTypes;
	}

	public void setCoverageClaimDocTypes(List<CoverageClaimDocType> coverageClaimDocTypes) {
		this.coverageClaimDocTypes = coverageClaimDocTypes;
	}
	
	public List<CoverageOrderDocType> getCoverageOrderDocTypes() {
		return coverageOrderDocTypes;
	}

	public void setCoverageOrderDocTypes(List<CoverageOrderDocType> coverageOrderDocTypes) {
		this.coverageOrderDocTypes = coverageOrderDocTypes;
	}

	public CoverageCategory getCoverageCategory() {
		return coverageCategory;
	}

	public void setCoverageCategory(CoverageCategory coverageCategory) {
		this.coverageCategory = coverageCategory;
	}
	
	public String getCoverageOptionId() {
		return coverageOptionId;
	}

	public void setCoverageOptionId(String coverageOptionId) {
		this.coverageOptionId = coverageOptionId;
	}

	public CoverageOption getCoverageOption() {
		return coverageOption;
	}

	public void setCoverageOption(CoverageOption coverageOption) {
		this.coverageOption = coverageOption;
	}

	@Override
	public String toString() {
		return "Coverage [coverageId=" + coverageId + ", coverageCategoryId=" + coverageCategoryId + ", name=" + name
				+ ", recommendation=" + recommendation + ", description=" + description + ", maxLimit=" + maxLimit
				+ ", familyMaxLimit=" + familyMaxLimit + ", hasBeneficiary=" + hasBeneficiary + ", createdDate="
				+ createdDate + ", updateDate=" + updateDate + ", status=" + status + ", displayRank=" + displayRank
				+ ", isRecommended=" + isRecommended + ", isIntroRecommended=" + isIntroRecommended + ", isLumpSum="
				+ isLumpSum + ", providerCode=" + providerCode + ", coverageOptionId=" + coverageOptionId
				+ ", coverageClaimDocTypes=" + coverageClaimDocTypes + ", coverageCategory=" + coverageCategory
				+ ", coverageOption=" + coverageOption + "]";
	}

	
}