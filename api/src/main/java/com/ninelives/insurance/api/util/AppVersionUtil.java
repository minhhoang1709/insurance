package com.ninelives.insurance.api.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.ninelives.insurance.api.model.Version;

public class AppVersionUtil {
	
	public static boolean isUpgradeRequired(Version minimumVersion, String versionToBeVerified){
		if(minimumVersion == null){
			return false;
		}
		if(StringUtils.isEmpty(versionToBeVerified)){
			return true;
		}
		try {			
			Version toBeVerified = AppVersionUtil.parse(versionToBeVerified);
			
			if(toBeVerified.compareTo(minimumVersion)<0){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			return true;
		} 
	}
	
	public static Version parse(String versionStr){
		int[] arr = Arrays.stream(versionStr.split("\\."))
			    .mapToInt(Integer::parseInt)
			    .toArray();
		if(arr == null || arr.length < 3){
			return null;
		}
		return new Version(arr[0],arr[1],arr[2]);		
	}
}
