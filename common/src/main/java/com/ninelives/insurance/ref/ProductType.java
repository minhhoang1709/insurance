package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductType {
	NORMAL("NORMAL"),
	FREE("FREE")
	;
	
	static final Map<String, ProductType> lookup = new HashMap<>();
	static {
		for (ProductType c:ProductType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ProductType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ProductType toEnum(String value){
    	ProductType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
