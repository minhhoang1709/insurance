package com.ninelives.insurance.batch.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PushNotificationType {
	ACTIVE_ORDER("ACTIVE_ORDER"),
	EXPIRE_ORDER("EXPIRE_ORDER"),
	TOBE_EXPIRE_ORDER("TOBE_EXPIRE_ORDER")
	;
	
	static final Map<String, PushNotificationType> lookup = new HashMap<>();
	static {
		for (PushNotificationType c:PushNotificationType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PushNotificationType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PushNotificationType toEnum(String value){
    	PushNotificationType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
