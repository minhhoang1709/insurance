package com.ninelives.insurance.apigateway.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.DurationFormatUtils;

public class DateTimeFormatUtil {
	
	public static String DURATION_FORMAT = "H 'jam' m 'menit'";
	
	public static String timeBetween(LocalDateTime start, LocalDateTime end){
		if(start == null || end == null){
			return null;
		}
		if(!end.isAfter(start)){
			return "0 jam 0 menit";
		}
		return DurationFormatUtils.formatDuration(Duration.between(start, end).toMillis(), DURATION_FORMAT);
	}
	
	public static String getPaymentDateTimeString(LocalDateTime localDateTime) {
		
		Date out = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
		String dateTime = null;
		dateTime = DATE_TIME_FORMAT.format(out);
		
		return dateTime;
	}
	
    public static String getPaymentDateTimeString2(LocalDate localDate) {
		
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    	String dateTime=(localDate).format(formatter);
    	
		return dateTime;
	}
    
    public static String getPaymentDateTimeString3(LocalDate localDate) {
		
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    	String dateTime=(localDate).format(formatter);
    	
		return dateTime;
	}
    
    public static String getFormatDate1(String date){

        String rValue="";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
        Date dateFormat;
		try {
			dateFormat = format1.parse(date);
			rValue = format2.format(dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
     
        return rValue;
    
    }
    
    public static String getFormatDate2(String date){

        String rValue="";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
        Date dateFormat;
		try {
			dateFormat = format1.parse(date);
			rValue = format2.format(dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
     
        return rValue;
    
    }
    
   
	public static LocalDate getLocalDateFromString(String date) {

		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
        LocalDate  localDate = LocalDate.parse(date, formatter); 
        return localDate;
    
	}

	public static boolean isEnableEdit(String useDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
	    formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); 
	    LocalDate  useEndDate = LocalDate.parse(useDate, formatter); 
		
		boolean rValue= true;
		LocalDate today = LocalDate.now();
		if (useEndDate.equals(today)) {
           rValue=false;
        }
	    if (useEndDate.isBefore(today)) {
	       rValue=false;
	    }
        if (useEndDate.isAfter(today)) {
	       rValue=true;
	    }
        
		return rValue;
	}
	
	 public static String getFormatStatDate(String date) {
		    String[] dt = date.split("/");
			String dateTime = dt[2]+"-"+dt[0]+"-"+dt[1];
			
			return dateTime;
	}
	
}
