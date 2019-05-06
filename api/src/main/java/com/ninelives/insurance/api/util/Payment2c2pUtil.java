package com.ninelives.insurance.api.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Payment2c2pUtil {
	
	public Payment2c2pUtil(){
		
	}
	
	public static boolean checkSignature( final String queryKey, 
			final String subject, String baseString ){ 
		boolean rValue=false;
		try 
	    { 
	     String hashParam =hashSignature(queryKey, baseString);
	     	if(hashParam.equals(subject)){
	     		rValue=true;
	     	}
	    }catch (Exception e){ 
	     System.out.println(e.toString());
	    } 
	    
	    return rValue;
	  
	} 
	
	public static String hashSignature(String secretKey, String arrayString) {

        String hashedSignature = "";
        try {

            Mac sha1HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1");
            sha1HMAC.init(secretKeySpec);
            hashedSignature = bytesToHex(sha1HMAC.doFinal(arrayString.getBytes("UTF-8"))).toUpperCase();
        } catch (Exception e) { e.printStackTrace(); }

        return hashedSignature;
    }
	
	private static String bytesToHex(byte[] bytes) {

        final char[] hexArray = "0123456789abcdef".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
	
	public static String zeroPad(int length, String field){
        String rSpasi;
        String spasi="0";
        for(int i=1;i<=length-field.length();i++){            
            spasi = spasi +"0";            
        }
        rSpasi = spasi+field;
        return rSpasi;
    }
	
	
	public static String getButtonHtml(){
		String rValue="";
		
		rValue="<style> "+
				".button {display: inline-block;border-radius: 4px;background-color: purple;border: none;color: #FFFFFF;"+
				"text-align: center;font-size: 28px;padding: 20px;width: 200px;transition: all 0.5s;cursor: pointer;margin: 5px;}"+
				".button span {cursor: pointer;display: inline-block;position: relative;transition: 0.5s;}"+
				".button span:after {content: '';position: absolute;opacity: 0;top: 0;right: -20px;transition: 0.5s;}"+
				".button:hover span {padding-right: 25px;}"+
				".button:hover span:after {opacity: 1;right: 0;}"+
				"</style>";
		
		return rValue;
	
	}
	
	public static boolean isStatusAllowedReuse(String status){ 
		boolean rValue=false;
		if(status.equals("OVERDUE")){
	     	rValue=true;
	    }
		if(status.equals("INPAYMENT")){
     		rValue=true;
		}
		if(status.equals("SUBMITTED")){
     		rValue=true;
		}
	    
		return rValue;
	  
	} 
	
	
}