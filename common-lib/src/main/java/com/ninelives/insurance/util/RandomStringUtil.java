package com.ninelives.insurance.util;

import org.apache.commons.text.RandomStringGenerator;

public class RandomStringUtil {
	
	public static final char[] DEFAULT_CHARS = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	public static final char[] LOWER_ALPHABET_CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	public enum RANDOM_TYPE{
		TYPE_1,
		TYPE_2
	}
	
	public static RandomStringGenerator randomStringGenerator;
	public static RandomStringGenerator randomStringGenerator2;
	
	static{
		randomStringGenerator = new RandomStringGenerator.Builder().selectFrom(DEFAULT_CHARS).build();
		randomStringGenerator2 = new RandomStringGenerator.Builder().selectFrom(LOWER_ALPHABET_CHARS).build();
	}
	
	public static String generate(int length){
		return randomStringGenerator.generate(length);
	}
	
	public static String generate(int length, RANDOM_TYPE type) {
		if(type != null && type.equals(RANDOM_TYPE.TYPE_2)) {
			return randomStringGenerator2.generate(length);
		}else {
			return randomStringGenerator.generate(length);
		}
		
	}
}
