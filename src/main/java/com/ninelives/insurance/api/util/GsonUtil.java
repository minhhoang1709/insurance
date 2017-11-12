package com.ninelives.insurance.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static final Gson gson = new GsonBuilder()
	        .setDateFormat("yyyy-MM-dd'T'hh:mm:ss")
	        .create();
	
//	public static String toJson(Object src, Type clazz){
//		return gson.toJson(src, clazz);
//	}
//	
//	public static <T> T fromJson(String src, Class<T> clazz){
//		return gson.fromJson(src, clazz);
//	}
}
