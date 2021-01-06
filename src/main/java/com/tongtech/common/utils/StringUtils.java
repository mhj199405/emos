package com.tongtech.common.utils;

import java.util.UUID;

public class StringUtils {
	public static Boolean isBlank(String str) {
		return str==null || "".equals(str.trim());
	}
	
	public static String trim(String str) {
		if (str == null)
			return null;
		return str.trim();
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
