package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserSource {
	GOOGLE ("google"),
	EMAIL("email"),
	BATCH_B2B("batch_b2b")
	;
	
	static final Map<String, UserSource> lookup = new HashMap<>();
	static {
		for (UserSource c:UserSource.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private UserSource(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static UserSource toEnum(String value){
    	UserSource result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
