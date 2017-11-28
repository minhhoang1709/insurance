package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClaimCoverageStatus {
	SUBMITTED("SUBMITTED"),
	INREVIEW("INREVIEW"),
	APPROVED("APPROVED"),
	DECLINED("DECLINED"),
	PAID("PAID")
	;
	
	static final Map<String, ClaimCoverageStatus> lookup = new HashMap<>();
	static {
		for (ClaimCoverageStatus c:ClaimCoverageStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ClaimCoverageStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ClaimCoverageStatus toEnum(String value){
    	ClaimCoverageStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
