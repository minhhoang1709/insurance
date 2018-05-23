package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FamilyRelationship {
	PASANGAN("pasangan"),
	ANAK("anak"),
	ORANGTUA("orangtua"),
	SAUDARA("saudara")	
	;
	
	static final Map<String, FamilyRelationship> lookup = new HashMap<>();
	static {
		for (FamilyRelationship c:FamilyRelationship.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private FamilyRelationship(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static FamilyRelationship toEnum(String value){
    	FamilyRelationship result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
