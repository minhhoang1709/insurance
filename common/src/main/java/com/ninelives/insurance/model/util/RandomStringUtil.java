package com.ninelives.insurance.model.util;

import org.apache.commons.text.RandomStringGenerator;

public class RandomStringUtil {
	public static final char[] CHARS_UPPER = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	public static RandomStringGenerator randomStringGenerator;
	
	static{
		randomStringGenerator = new RandomStringGenerator.Builder().selectFrom(CHARS_UPPER).build();
	}
	
	public static String generate(int length){
		return randomStringGenerator.generate(length);
	}
}
