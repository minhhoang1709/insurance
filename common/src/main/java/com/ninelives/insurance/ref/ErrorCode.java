package com.ninelives.insurance.ref;

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
	ERR4010_ORDER_PROFILE_INVALID("ERR4010"), //empty or incomplete profile
	
	ERR4101_BENEFICIARY_INVALID("ERR4101"), //empty or invalid beneficiary data
	ERR4102_BENEFICIARY_EXISTS("ERR4102"), //cannot insert another beneficiary
	ERR4103_BENEFICIARY_NOTACCEPTED("ERR4103"), //order hasbeneficiary is false
	ERR4104_BENEFICIARY_ORDER_STATUS("ERR4104"), //order status is not valid for beneficiary update
	
	ERR5001_ORDER_NOT_FOUND("ERR5001"), //get order with given id not found
	ERR6001_UPLOAD_EMPTY("ERR6001"), //upload with empty file
	ERR6002_UPLOAD_SYSTEM_ERROR("ERR6002"), //system error
	
	ERR7000_CLAIM_INVALID("ERR7000"), //get claim with given id not found
	ERR7001_CLAIM_NOT_FOUND("ERR7001"),//get claim with given id not found
	ERR7002_CLAIM_ORDER_INVALID("ERR7002"), //order status is not valid for claim
	ERR7003_CLAIM_COVERAGE_INVALID("ERR7003"), //selected coverage is empty or not valid
	ERR7004_CLAIM_DOCUMENT_EMPTY("ERR7004"), //submitted document is empty
	ERR7005_CLAIM_DOCUMENT_INVALID("ERR7005"), //submitted document is invalid
	ERR7006_CLAIM_DOCUMENT_MANDATORY("ERR7006"), //mandatory document missing
	ERR7007_CLAIM_DOCUMENT_FILE_INVALID("ERR7007"), //file is empty or not uploaded
	ERR7008_CLAIM_DETAIL_INVALID("ERR7008"), //detail info (e.g. address) is invalid
	ERR7009_CLAIM_BANK_ACCOUNT_INVALID("ERR7009"), //bank account info is invalid
	
	ERR8001_CHARGE_API_ERROR("ERR8001"), //error from api call to midtrans
	ERR8002_CHARGE_INVALID("ERR8002"), //empty or invalid charge data
	ERR8003_ORDER_NOT_FOUND("ERR8003"), //empty or invalid order data
	
	ERR8200_PAYMENT_NOTIF_GENERIC_ERROR("ERR8200"), //
	ERR8201_PAYMENT_NOTIF_SIGNATURE_INVALID("ERR8201"), //
	ERR8202_PAYMENT_NOTIF_ORDER_NOT_FOUND("ERR8202")
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