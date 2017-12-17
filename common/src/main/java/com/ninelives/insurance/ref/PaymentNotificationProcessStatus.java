package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentNotificationProcessStatus {
	LATE_NOTIF("LATE_NOTIF"),
	DUPLICATE("DUPLICATE"),
	OUT_OF_ORDER("OUT_OF_ORDER"),
	CHALLENGE("CHALLENGE"), //receive capture/challenge, log only
	UNKNOWN("UNKNOWN") //receive unhandled cases, log only
	;
	
	static final Map<String, PaymentNotificationProcessStatus> lookup = new HashMap<>();
	static {
		for (PaymentNotificationProcessStatus c:PaymentNotificationProcessStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private PaymentNotificationProcessStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static PaymentNotificationProcessStatus toEnum(String value){
    	PaymentNotificationProcessStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
