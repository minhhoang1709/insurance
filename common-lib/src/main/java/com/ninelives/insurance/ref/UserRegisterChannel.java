package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRegisterChannel {
	ANDROID ("android"),
	BATCH("batch")
	;
	
	static final Map<String, UserRegisterChannel> lookup = new HashMap<>();
	static {
		for (UserRegisterChannel c:UserRegisterChannel.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private UserRegisterChannel(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static UserRegisterChannel toEnum(String value){
    	UserRegisterChannel result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
