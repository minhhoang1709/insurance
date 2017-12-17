package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FileUseType {
	IDT("IDT"), //Identity Card
	CLAIM("CLA"), //CLAIM
	TEMP("TMP") //TEMP
	;
	
	static final Map<String, FileUseType> lookup = new HashMap<>();
	static {
		for (FileUseType c:FileUseType.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private FileUseType(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static FileUseType toEnum(String value){
    	FileUseType result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
