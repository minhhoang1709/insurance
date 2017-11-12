package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
	ERR1001_GENERIC_ERROR("ERR1001"),
	ERR2001_NOT_AUTHORIZED("ERR2001")
	;
	
	static final Map<String, ErrorCode> lookup = new HashMap<>();
	static {
		for (ErrorCode c:ErrorCode.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ErrorCode(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ErrorCode toEnum(String value){
    	ErrorCode result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
