package com.telus.ait.tests.clientapi.helpers;

public class CommonHelper {

	public static final String WILDCARD = "*";
	public static final String LIST_SEPARATOR = ";";
	
	protected static String getDefaultValue(String value, String defaultValue) {
		if (WILDCARD.equals(value)) {
			return defaultValue;
		}
		return (value != null) ? value.trim() : value;
	}
	
	public static boolean isEmptyOrWildcard(String value) {
		return value == null || value.trim().equals("") || WILDCARD.equals(value.trim());
	}
		
}
