package com.ninelives.insurance.apigateway.util;

import java.time.Duration;
import java.time.LocalDateTime;
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
}