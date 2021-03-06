package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PeriodUnit {
	DAY("DAY"),
	WEEK("WEEK"),
	MONTH("MONTH"),
	YEAR("YEAR"),
	RANGE_DAY("RANGE_DAY")
	;
	
	static final Map<String, PeriodUnit> lookup = new HashMap<>();
	static {
		for (PeriodUnit c:PeriodUnit.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PeriodUnit(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PeriodUnit toEnum(String value){
    	PeriodUnit result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
