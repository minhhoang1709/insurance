package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoverageCategory implements Serializable{
	private static final long serialVersionUID = 2339420457405989204L;

	private String coverageCategoryId;

    private String name;

    private String description;

    @JsonIgnore
    private LocalDateTime createdDate;

    @JsonIgnore
    private LocalDateTime updateDate;

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

	@Override
	public String toString() {
		return "CoverageCategory [coverageCategoryId=" + coverageCategoryId + ", name=" + name + ", description="
				+ description + ", createdDate=" + createdDate + ", updateDate=" + updateDate + "]";
	}


}