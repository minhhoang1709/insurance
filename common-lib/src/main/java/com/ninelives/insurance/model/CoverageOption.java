package com.ninelives.insurance.model;

import java.time.LocalDateTime;

public class CoverageOption {
    private String id;

    private String coverageOptionName;

    private String coverageOptionGroupId;
    
    private CoverageOptionGroup coverageOptionGroup;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverageOptionName() {
        return coverageOptionName;
    }

    public void setCoverageOptionName(String coverageOptionName) {
        this.coverageOptionName = coverageOptionName;
    }

    public String getCoverageOptionGroupId() {
        return coverageOptionGroupId;
    }

    public void setCoverageOptionGroupId(String coverageOptionGroupId) {
        this.coverageOptionGroupId = coverageOptionGroupId;
    }
    
    public CoverageOptionGroup getCoverageOptionGroup() {
		return coverageOptionGroup;
	}

	public void setCoverageOptionGroup(CoverageOptionGroup coverageOptionGroup) {
		this.coverageOptionGroup = coverageOptionGroup;
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
}