/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Pavel Simonovsky
 *
 */
public class StringUtil {

	public static char toChar(String str) {
		return (str == null || str.length() == 0) ? ' ' : str.charAt(0);
	}

	public static String emptyFromNull(String pString) {
		if ((pString == null) || (pString.toUpperCase().equals("NULL")))
			return "";
		return pString;
	}

	public static String[] stringTokensToArray(String param) {
		
		List<String> result = new ArrayList<String>();
		if (param != null) {
			StringTokenizer st = new StringTokenizer(param, ";");
			while (st.hasMoreTokens()) {
				result.add(st.nextToken().trim());
			}
		}
		
		return (String[]) result.toArray(new String[result.size()]);
	}

	public static String rangeIntToString(int range) {
		
		String rangeString = String.valueOf(range);
		while (rangeString.length() < 4) {
			rangeString = "0" + rangeString;
		}
		return rangeString;
	}

	public static String arrayToString(String[] a, String separator) {
		
		if (a == null || separator == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();

		for (String b : a) {
			result.append(b);
			result.append(separator);
		}
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		
		return result.toString();
	}

	public static String listToString(List<String> a, String separator) {
		
		if (a == null || separator == null) {
			return null;
		}
		StringBuilder result = new StringBuilder();

		for (String b : a) {
			result.append(b);
			result.append(separator);
		}
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		
		return result.toString();
	}
	
	/**
	 * This method will convert a string in the format {AccountType:SubType(s),...) to a list of account type/sub-type pairings
	 *     For example :  "I:123BDEFJMQRWYZ, B:134ABFDGMNOPRWX" -> {I1, I2, I3, ..., IZ, B1, B3, B4, ..., BX}
	 * @return
	 */
	public static List<String> accountTypeSubtypeStringToList(String typeString) {
		
		List<String> accountTypeSubtypeList = new ArrayList<String>();
		if (StringUtils.isNotEmpty(typeString)) {
			for (String patternToken : StringUtils.split(typeString, ",")) {
				String[] typeTokens = patternToken.split(":");
				if (typeTokens.length == 2) {					
					for(char subtype : typeTokens[1].trim().toCharArray()) {
						accountTypeSubtypeList.add(typeTokens[0].trim() + subtype);
					}
				}
			}
		}
		
		return accountTypeSubtypeList;
	}
}
