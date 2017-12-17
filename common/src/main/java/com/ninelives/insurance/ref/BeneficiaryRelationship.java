package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BeneficiaryRelationship {
	PASANGAN("pasangan"),
	ORANGTUA("orangtua"),
	SAUDARA("saudara"),
	ANAK("anak"),
	ORANGLAIN("oranglain")
	;
	
	static final Map<String, BeneficiaryRelationship> lookup = new HashMap<>();
	static {
		for (BeneficiaryRelationship c:BeneficiaryRelationship.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private BeneficiaryRelationship(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static BeneficiaryRelationship toEnum(String value){
    	BeneficiaryRelationship result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
