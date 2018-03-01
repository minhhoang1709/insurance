package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
	ACTIVE("ACTIVE"),
	DEACT("DEACT")
	;
	
	static final Map<String, UserStatus> lookup = new HashMap<>();
	static {
		for (UserStatus c:UserStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private UserStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static UserStatus toEnum(String value){
    	UserStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
