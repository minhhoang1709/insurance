package com.ninelives.insurance.api.model;

import java.time.LocalDateTime;

public class ClaimDocType {
    private String claimDocTypeId;

    private String name;

    private String description;

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