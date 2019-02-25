package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RegisterIdSource {
	GOOGLE ("google"),
	EMAIL("email")
	;
	
	static final Map<String, RegisterIdSource> lookup = new HashMap<>();
	static {
		for (RegisterIdSource c:RegisterIdSource.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private RegisterIdSource(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static RegisterIdSource toEnum(String value){
    	RegisterIdSource result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
