package com.ninelives.insurance.util;

import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {
	
	/**
	 * Validate digit and length
	 *  * If phone starts with '0', maximum length is 14
	 *  * If phone starts with '62', maximum length is 15
	 *  * otherwise, maximum length is 13
	 * 
	 * This requirement related with aswata api that will replace starting 0 with 62, and others with 6221
	 * I.e. the api assume Indonesia, then Jakarta number format
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phone){
		boolean result = false;
		
		if(!StringUtils.isEmpty(phone)){
			if(phone.startsWith("0") && phone.length()<=14){
				//assume phone number without country code, but with province code
				//maximum length should be 15 after replace 0 with 62
				result = true;
			}else if(phone.startsWith("62") && phone.length()<=15){
				//assume phone number with country code
				//maximum length should be 15
				result = true;
			}else if(phone.length()<=13){
				//assume phone number without country code, without province code
				//maximum length should be 15 after replace 0 with 6221
				result = true;
			}
		}
		
		return result;
	}
	
	public static String toAswataPhoneNumber(String phone){
		String result = phone;
		
		if(!StringUtils.isEmpty(phone)){
			if(!phone.startsWith("0") && !phone.startsWith("62")){
				result = "0" + phone;
			}		
		}
		
		return result;
	}
}
