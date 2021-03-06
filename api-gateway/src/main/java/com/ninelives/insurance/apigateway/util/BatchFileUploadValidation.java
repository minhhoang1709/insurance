package com.ninelives.insurance.apigateway.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Period;


public class BatchFileUploadValidation {
	
	
	public static boolean validateEmail(String txt) {

		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
	    Matcher matcher = pattern.matcher(txt);
	    return matcher.find();

	}
	
	public static boolean isValidDate(String str) {
		DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try{
			Date date = (Date)formatter.parse(str);
			return str.equals(formatter.format(date));
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean validateAge(String str) {
		
		boolean rValue = false;
		LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(Integer.parseInt(str.substring(0, 4)),
		    		Integer.parseInt(str.substring(4, 6)),
		    		Integer.parseInt(str.substring(6, 8)));
            Period period = Period.between(birthday, today);
            if(period.getYears()>17 && period.getYears()<60){
            	rValue= true;
            }
		return rValue;
        
	}
	
  public static int getAge(String str) {
		
		int rValue;
		LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.of(Integer.parseInt(str.substring(0, 4)),
		    		Integer.parseInt(str.substring(4, 6)),
		    		Integer.parseInt(str.substring(6, 8)));
        Period period = Period.between(birthday, today);
        rValue= period.getYears();
            
		return rValue;
        
	}
	
	
	public static boolean validateLetters(String txt) {
	    String regx = "^[\\p{L} .'-]+$";
	    Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(txt);
	    return matcher.find();

	}
	
	
	public static boolean validateSpecialCharacters(String txt) {
		String regx = "[a-zA-Z0-9 ']*";
	    Pattern pattern = Pattern.compile(regx,Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(txt.replace(".", ""));
	    return matcher.matches();

	}
	
	public static boolean validateNumeric(String txt) {
	    String regx = "[0-9]+";
	    Pattern pattern = Pattern.compile(regx);
	    Matcher matcher = pattern.matcher(txt);
	    return matcher.matches();

	}

	public static boolean validateFormatRow(String lineToUpload) {
		boolean rValue=true;
		String[] col=lineToUpload.split(",",-1);
		if(col.length!=7){
			rValue=false;
		}
		if(col[0].equalsIgnoreCase("email")){
			rValue=false;
		}
		return rValue;
	}
	
	
}