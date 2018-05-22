package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CoverageCategoryType {
	PA("PA"),
	TRAVEL("TRAVEL")
	;
	
	static final Map<String, CoverageCategoryType> lookup = new HashMap<>();
	static {
		for (CoverageCategoryType c:CoverageCategoryType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private CoverageCategoryType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static CoverageCategoryType toEnum(String value){
    	CoverageCategoryType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
