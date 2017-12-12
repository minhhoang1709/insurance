package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentChargeStatus {
	CHARGE("CHARGE"),
	ERRSYS("ERRSYS"), //Internal system error
	ERRMID("ERRMID") //Error response from Midtrans
	;
	
	static final Map<String, PaymentChargeStatus> lookup = new HashMap<>();
	static {
		for (PaymentChargeStatus c:PaymentChargeStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PaymentChargeStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PaymentChargeStatus toEnum(String value){
    	PaymentChargeStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
