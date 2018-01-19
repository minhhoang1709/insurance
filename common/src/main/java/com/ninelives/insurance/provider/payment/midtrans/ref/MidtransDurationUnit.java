package com.ninelives.insurance.provider.payment.midtrans.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MidtransDurationUnit {
	HOURS("hours");
	
	static final Map<String, MidtransDurationUnit> lookup = new HashMap<>();
	static {
		for (MidtransDurationUnit c:MidtransDurationUnit.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private MidtransDurationUnit(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static MidtransDurationUnit toEnum(String value){
    	MidtransDurationUnit result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
