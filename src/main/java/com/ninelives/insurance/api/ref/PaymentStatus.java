package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
	CHARGE("CHARGE"),
	PENDING("PENDING"),
	EXPIRE("EXPIRE"),
	FAIL("FAIL"),	
	SUCCESS("SUCCESS")
	;
	
	static final Map<String, PaymentStatus> lookup = new HashMap<>();
	static {
		for (PaymentStatus c:PaymentStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PaymentStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PaymentStatus toEnum(String value){
    	PaymentStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
