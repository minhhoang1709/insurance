package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PolicyStatus {
	SUBMITTED("SUBMITTED"),
	INPAYMENT("INPAYMENT"),
	OVERDUE("OVERDUE"),
	PAID("PAID"),
	APPROVED("APPROVED"),
	ACTIVE("ACTIVE"),
	EXPIRED("EXPIRED"),
	TERMINATED("TERMINATED")
	;
	
	static final Map<String, PolicyStatus> lookup = new HashMap<>();
	static {
		for (PolicyStatus c:PolicyStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PolicyStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PolicyStatus toEnum(String value){
    	PolicyStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
