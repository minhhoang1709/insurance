package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.CoverageCategoryType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageCategory implements Serializable{
	private static final long serialVersionUID = 2339420457405989204L;

	private String coverageCategoryId;

    private String name;

    private String description;
    
    private CoverageCategoryType type;

    @JsonIgnore
    private LocalDateTime createdDate;

    @JsonIgnore
    private LocalDateTime updateDate;
    
    private String recommendation;
    
    private Insurer insurer;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public CoverageCategoryType getType() {
		return type;
	}

	public void setType(CoverageCategoryType type) {
		this.type = type;
	}
	
	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
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

	public Insurer getInsurer() {
		return insurer;
	}

	public void setInsurer(Insurer insurer) {
		this.insurer = insurer;
	}

	@Override
	public String toString() {
		return "CoverageCategory [coverageCategoryId=" + coverageCategoryId + ", name=" + name + ", description="
				+ description + ", type=" + type + ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}


}