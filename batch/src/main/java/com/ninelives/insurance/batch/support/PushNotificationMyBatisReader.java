package com.ninelives.insurance.batch.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.beans.factory.annotation.Value;

public class PushNotificationMyBatisReader<T> extends MyBatisCursorItemReader<T>{
	private Map<String, Object> parameters = new HashMap<>();
	
	public void addParameter(String key, Object value){
		parameters.put(key, value);
	}
	
	@Value("#{jobParameters['targetDate']}")
	public void setTargetDate(final String targetDateStr) {
		LocalDate targetDate = LocalDate.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
	    addParameter("targetDate", targetDate);
	}
	
	@Override 
	protected void doOpen() throws Exception {
		super.setParameterValues(parameters);
		super.doOpen();
	}
}
