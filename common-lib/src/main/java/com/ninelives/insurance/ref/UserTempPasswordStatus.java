package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserTempPasswordStatus {
	REGISTERED("REGISTERED"),
	APPLIED("APPLIED"),
	REPLACED("REPLACED"),
	EXPIRED("EXPIRED")
	;
	
	static final Map<String, UserTempPasswordStatus> lookup = new HashMap<>();
	static {
		for (UserTempPasswordStatus c:UserTempPasswordStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private UserTempPasswordStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static UserTempPasswordStatus toEnum(String value){
    	UserTempPasswordStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
