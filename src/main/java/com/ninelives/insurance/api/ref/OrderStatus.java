package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
	SUBMITTED("SUBMITTED"),
	INPAYMENT("INPAYMENT"),
	OVERDUE("OVERDUE"),
	PAID("PAID"),
	APPROVED("APPROVED"),
	ACTIVE("ACTIVE"),
	EXPIRED("EXPIRED"),
	TERMINATED("TERMINATED")
	;
	
	static final Map<String, OrderStatus> lookup = new HashMap<>();
	static {
		for (OrderStatus c:OrderStatus.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private OrderStatus(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static OrderStatus toEnum(String value){
    	OrderStatus result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
