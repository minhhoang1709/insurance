package com.ninelives.insurance.api.util;

import java.time.Duration;
import java.time.LocalDateTime;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class DateTimeFormatUtil {
	
	public static String DURATION_FORMAT = "H 'jam' m 'menit'";
	
	public static String timeBetween(LocalDateTime start, LocalDateTime end, String format){
		if(start == null || end == null){
			return null;
		}
		long duration = 0L;
		if(end.isAfter(start)){
			duration = Duration.between(start, end).toMillis();
			//return "0 jam 0 menit";
		}
		return DurationFormatUtils.formatDuration(duration, format);
	}
}