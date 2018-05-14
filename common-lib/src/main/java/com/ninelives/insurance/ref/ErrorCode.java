package com.ninelives.insurance.ref;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
	ERR1001_GENERIC_ERROR("ERR1001"),
	ERR1002_STORAGE_ERROR("ERR1002"),
	ERR1003_UPGRADE_REQUIRED("ERR1002"), //Client needs upgrade
	
	ERR2001_LOGIN_FAILURE("ERR2001"),
	ERR2002_NOT_AUTHORIZED("ERR2002"),
	ERR2003_USER_NOT_FOUND("ERR2003"),
	ERR2004_USER_EMPTY("ERR2004"), //Submitted profile info is empty
	ERR2005_USER_PROFILE_INVALID("ERR2005"), //Submitted profile info is empty
	
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
	ERR4011_ORDER_VOUCHER_NOTFOUND("ERR4011"), //voucher not found, or incase of b2b it might already expired
	ERR4012_ORDER_VOUCHER_NOTELIGIBLE("ERR4012"), //voucher is not eligible, user has non-paid trx
	ERR4013_ORDER_VOUCHER_PREMI_MISMATCH("ERR4013"), //voucher premi doesnt match order
	ERR4014_ORDER_VOUCHER_DATE_MISMATCH("ERR4014"), //voucher policy start/end date doesnt match order
	ERR4015_ORDER_VOUCHER_PRODUCT_MISMATCH("ERR4015"), //voucher product not match order
	ERR4016_ORDER_VOUCHER_REQUIRED("ERR4016"), //voucher is required for certain product (e.g. free)
	ERR4017_ORDER_IDCARD_NOTFOUND("ERR4017"), //KTP is mandatory
	ERR4018_ORDER_PROFILE_AGE_INVALID("ERR4018"), //invalid age
	ERR4019_ORDER_PAYMENT_MINIMUM("ERR4019"), //minimum payment
	
	ERR4101_BENEFICIARY_INVALID("ERR4101"), //empty or invalid beneficiary data
	ERR4102_BENEFICIARY_EXISTS("ERR4102"), //cannot insert another beneficiary
	ERR4103_BENEFICIARY_NOTACCEPTED("ERR4103"), //order hasbeneficiary is false
	ERR4104_BENEFICIARY_ORDER_STATUS("ERR4104"), //order status is not valid for beneficiary update
	
	ERR4201_ORDER_PROVIDER_FAIL("ERR4201"), //fail to submit policy to provider
	ERR4202_ORDER_PROVIDER_FILE_ERROR("ERR4202"), //fail to read document/file to be sent
	ERR4203_ORDER_PROVIDER_CONNECT_DISABLED("ERR4203"), //config connect is set to false
	
	ERR4301_DOWNLOAD_NO_URL("ERR4301"), //fail to submit policy to provider
	ERR4302_DOWNLOAD_NOT_ELIGIBLE("ERR4302"), //fail to read document/file to be sent
	ERR4303_DOWNLOAD_PROVIDER_ERROR("ERR4303"), //fail to stream from aswata
	
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
	ERR8003_CHARGE_ORDER_NOT_FOUND("ERR8003"), //empty or invalid order data
	ERR8004_CHARGE_ORDER_NOT_VALID("ERR8004"), //empty or invalid order data
	ERR8005_CHARGE_PREMI_NOT_MATCH("ERR8005"), //premi != grossamount
	
	ERR8200_PAYMENT_NOTIF_GENERIC_ERROR("ERR8200"), //
	ERR8201_PAYMENT_NOTIF_SIGNATURE_INVALID("ERR8201"), //
	ERR8202_PAYMENT_NOTIF_ORDER_NOT_FOUND("ERR8202"),
	
	ERR9001_VOUCHER_NOT_FOUND("ERR9001"),
	ERR9002_VOUCHER_EXPIRED("ERR9002"), //voucher expired
	ERR9003_VOUCHER_OVERUSE("ERR9003") //voucher usage limit exceeded
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
