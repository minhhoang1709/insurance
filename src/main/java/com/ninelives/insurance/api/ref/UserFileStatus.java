package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserFileStatus {
	NONE("NONE"),
	UPLOADED("UPLOADED"),
	DELETED("DELETED")
	;
	
	static final Map<String, UserFileStatus> lookup = new HashMap<>();
	static {
		for (UserFileStatus c:UserFileStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private UserFileStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static UserFileStatus toEnum(String value){
    	UserFileStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
