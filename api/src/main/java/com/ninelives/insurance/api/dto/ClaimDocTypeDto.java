package com.ninelives.insurance.api.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocTypeDto {
    private String claimDocTypeId;

    private String name;

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

	@Override
	public String toString() {
		return "ClaimDocTypeDto [claimDocTypeId=" + claimDocTypeId + ", name=" + name + "]";
	}
    
}