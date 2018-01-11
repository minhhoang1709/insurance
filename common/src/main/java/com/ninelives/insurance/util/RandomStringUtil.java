package com.ninelives.insurance.util;

import org.apache.commons.text.RandomStringGenerator;

public class RandomStringUtil {
	public static final char[] DEFAULT_CHARS = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public static RandomStringGenerator randomStringGenerator;
	
	static{
		randomStringGenerator = new RandomStringGenerator.Builder().selectFrom(DEFAULT_CHARS).build();
	}
	
	public static String generate(int length){
		return randomStringGenerator.generate(length);
	}
}
