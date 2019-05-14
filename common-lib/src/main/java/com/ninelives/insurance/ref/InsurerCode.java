package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InsurerCode {
	ASWATA("aswata"), 
	PTI("pti")
	;
	
	static final Map<String, InsurerCode> lookup = new HashMap<>();
	static {
		for (InsurerCode c:InsurerCode.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private InsurerCode(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static InsurerCode toEnum(String value){
    	InsurerCode result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
