package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.ninelives.insurance.ref.ClaimDocUsageType;

public class ClaimDocType implements Serializable{
    private static final long serialVersionUID = -1030025360604205678L;

	private String claimDocTypeId;

    private String name;
    
    private Integer nameTranslationId;

    private String description;
    
    private ClaimDocUsageType usageType;
    
    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getClaimDocTypeId() {
        return claimDocTypeId;
    }

    public void setClaimDocTypeId(String claimDocTypeId) {
        this.claimDocTypeId = claimDocTypeId;
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

	public ClaimDocUsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(ClaimDocUsageType usageType) {
		this.usageType = usageType;
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

	public Integer getNameTranslationId() {
		return nameTranslationId;
	}

	public void setNameTranslationId(Integer nameTranslationId) {
		this.nameTranslationId = nameTranslationId;
	}

	@Override
	public String toString() {
		return "ClaimDocType [" + (claimDocTypeId != null ? "claimDocTypeId=" + claimDocTypeId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (createdDate != null ? "createdDate=" + createdDate + ", " : "")
				+ (updateDate != null ? "updateDate=" + updateDate : "") + "]";
	}
    
}