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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((claimDocTypeId == null) ? 0 : claimDocTypeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClaimDocTypeDto other = (ClaimDocTypeDto) obj;
		if (claimDocTypeId == null) {
			if (other.claimDocTypeId != null)
				return false;
		} else if (!claimDocTypeId.equals(other.claimDocTypeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClaimDocTypeDto [claimDocTypeId=" + claimDocTypeId + ", name=" + name + "]";
	}
    
}