package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SignupVerificationStatus {
	ACTIVE("ACTIVE"),
	VERIFIED("VERIFIED"),
	EXPIRED("EXPIRED")
	;
	
	static final Map<String, SignupVerificationStatus> lookup = new HashMap<>();
	static {
		for (SignupVerificationStatus c:SignupVerificationStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private SignupVerificationStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static SignupVerificationStatus toEnum(String value){
    	SignupVerificationStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
