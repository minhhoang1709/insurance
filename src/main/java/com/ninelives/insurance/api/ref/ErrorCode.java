package com.ninelives.insurance.api.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
	ERR1001_GENERIC_ERROR("ERR1001"),
	ERR2001_LOGIN_FAILURE("ERR2001"),
	ERR2002_NOT_AUTHORIZED("ERR2002"),
	ERR3001_REGISTER_GOOGLE_FAIL("ERR3001"),
	ERR3002_REGISTER_PASSWORD_CONFLICT("ERR3002"),
	ERR3003_REGISTER_MISSING_PARAMETER("ERR3003"),
	ERR4000_ORDER_INVALID("ERR4000"),
	ERR4001_ORDER_PRODUCT_EMPTY("ERR4001"),
	ERR4002_ORDER_PRODUCT_DUPLICATE("ERR4002"),
	ERR4003_ORDER_PRODUCT_NOTFOUND("ERR4003"), 
	ERR4004_ORDER_PERIOD_MISMATCH("ERR4004"), //only one period is allowed in same order
	ERR4005_ORDER_PREMI_MISMATCH("ERR4005"), //total premi from submission doesnt match with server calculated value
	ERR4006_ORDER_COVERAGE_MISMATCH("ERR4006"), // only one coverage category is allowed
	ERR4007_ORDER_STARTDATE_INVALID("ERR4007"), //policy start-date is not valid
	ERR4008_ORDER_PRODUCT_UNSUPPORTED("ERR4008"), //only day-based product is supported
	ERR4009_ORDER_PRODUCT_CONFLICT("ERR4009"), //only limited number of same coverage can be active at the same time
	ERR5001_ORDER_NOT_FOUND("ERR5001")
	;
	
	static final Map<String, ErrorCode> lookup = new HashMap<>();
	static {
		for (ErrorCode c:ErrorCode.values()){
			lookup.put(c.toString(), c);
		}
	}
	
	private final String stringValue;

    private ErrorCode(final String newValue) {
        stringValue = newValue;
    }

    @JsonValue
    public String toString() {
        return stringValue;
    }
    
    public String toStr() {
    	return stringValue;
    }
    
    public static ErrorCode toEnum(String value){
    	ErrorCode result = lookup.get(value);
    	if (result != null){
    		return result;
    	}
    	throw new IllegalArgumentException("No matching constant for [" + value + "]");
    }
}
