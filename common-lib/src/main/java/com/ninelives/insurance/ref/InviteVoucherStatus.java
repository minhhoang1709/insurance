package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum InviteVoucherStatus {
	ACTIVE("ACTIVE"),
	DEACTIVE("DEACTIVE")
	;
	
	static final Map<String, InviteVoucherStatus> lookup = new HashMap<>();
	static {
		for (InviteVoucherStatus c:InviteVoucherStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private InviteVoucherStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static InviteVoucherStatus toEnum(String value){
    	InviteVoucherStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
