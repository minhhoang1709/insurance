package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ClaimDocUsageType {
	FAMILY_CARD("familyCard"),
	REGULAR("regular"),
	FAMILY_ID_CARD("familyIdCard"),
	FAMILY_PASSPORT("familyPassport"),
	SELFIE("selfie")
	;
	
	static final Map<String, ClaimDocUsageType> lookup = new HashMap<>();
	static {
		for (ClaimDocUsageType c:ClaimDocUsageType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ClaimDocUsageType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ClaimDocUsageType toEnum(String value){
    	ClaimDocUsageType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
