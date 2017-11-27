package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;

public class CoverageClaimDocType {
    private Integer coverageClaimDocTypeId;

    private String coverageId;

    private String claimDocTypeId;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    public Integer getCoverageClaimDocTypeId() {
        return coverageClaimDocTypeId;
    }

    public void setCoverageClaimDocTypeId(Integer coverageClaimDocTypeId) {
        this.coverageClaimDocTypeId = coverageClaimDocTypeId;
    }

    public String getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(String coverageId) {
        this.coverageId = coverageId;
    }

    public String getClaimDocTypeId() {
        return claimDocTypeId;
    }

    public void setClaimDocTypeId(String claimDocTypeId) {
        this.claimDocTypeId = claimDocTypeId;
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