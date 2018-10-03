package com.ninelives.insurance.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ninelives.insurance.ref.ClaimDocUsageType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClaimDocTypeDto {
    private String claimDocTypeId;
    private String name;
    private ClaimDocUsageType usageType;

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

	public ClaimDocUsageType getUsageType() {
		return usageType;
	}

	public void setUsageType(ClaimDocUsageType usageType) {
		this.usageType = usageType;
	}

	@Override
	public String toString() {
		return "ClaimDocTypeDto [claimDocTypeId=" + claimDocTypeId + ", name=" + name + "]";
	}
    
}