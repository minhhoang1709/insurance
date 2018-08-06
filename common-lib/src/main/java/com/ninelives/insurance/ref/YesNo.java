package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum YesNo {
	YES("Y"),
	NO("N")
	;
	
	static final Map<String, YesNo> lookup = new HashMap<>();
	static {
		for (YesNo c:YesNo.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private YesNo(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static YesNo toEnum(String value){
    	YesNo result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
