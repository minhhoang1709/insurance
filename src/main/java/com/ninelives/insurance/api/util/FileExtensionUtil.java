package com.ninelives.insurance.api.util;

import java.util.HashMap;
import java.util.Map;

public class FileExtensionUtil {
	private static final Map<String, String> fileExtensionMap;

	static {
	    fileExtensionMap = new HashMap<String, String>();
	    fileExtensionMap.put("pdf", "application/pdf");
	    fileExtensionMap.put("jpg", "image/jpeg");
	    fileExtensionMap.put("png", "image/png");
	}
}
