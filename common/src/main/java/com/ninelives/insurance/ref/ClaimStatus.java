package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClaimStatus {
	SUBMITTED("SUBMITTED"),
	INREVIEW("INREVIEW"),
	APPROVED("APPROVED"),
	DECLINED("DECLINED"),
	PAID("PAID")
	;
	
	static final Map<String, ClaimStatus> lookup = new HashMap<>();
	static {
		for (ClaimStatus c:ClaimStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ClaimStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ClaimStatus toEnum(String value){
    	ClaimStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
