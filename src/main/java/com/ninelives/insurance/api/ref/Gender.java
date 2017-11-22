package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	MALE("M"),
	FEMALE("F")
	;
	
	static final Map<String, Gender> lookup = new HashMap<>();
	static {
		for (Gender c:Gender.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private Gender(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static Gender toEnum(String value){
    	Gender result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
