package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SignupVerificationType {
	EMAIL_LINK("email_link")
	;
	
	static final Map<String, SignupVerificationType> lookup = new HashMap<>();
	static {
		for (SignupVerificationType c:SignupVerificationType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private SignupVerificationType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static SignupVerificationType toEnum(String value){
    	SignupVerificationType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
