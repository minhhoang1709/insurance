package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderDocUsageType {
	SELFIE("selfie")
	;
	
	static final Map<String, OrderDocUsageType> lookup = new HashMap<>();
	static {
		for (OrderDocUsageType c:OrderDocUsageType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private OrderDocUsageType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static OrderDocUsageType toEnum(String value){
    	OrderDocUsageType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
