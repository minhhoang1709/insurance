package com.ninelives.insurance.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CoverageOptionGroup implements Serializable{
    private static final long serialVersionUID = 1304782511020044418L;

	private String id;

    private String coverageOptionGroupName;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverageOptionGroupName() {
        return coverageOptionGroupName;
    }

    public void setCoverageOptionGroupName(String coverageOptionGroupName) {
        this.coverageOptionGroupName = coverageOptionGroupName;
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