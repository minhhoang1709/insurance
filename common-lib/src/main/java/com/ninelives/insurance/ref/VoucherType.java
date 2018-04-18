package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VoucherType {
	INVITE("INVITE"),
	B2B("B2B"),
	FREE_PROMO_NEW_USER("FREE_PROMO_NEW_USER")
	;
	
	static final Map<String, VoucherType> lookup = new HashMap<>();
	static {
		for (VoucherType c:VoucherType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private VoucherType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static VoucherType toEnum(String value){
    	VoucherType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
