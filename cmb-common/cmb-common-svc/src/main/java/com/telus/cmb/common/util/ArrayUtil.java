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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import com.telus.eas.framework.info.Info;

/**
 * @author Pavel Simonovsky
 *
 */
public class ArrayUtil {
	
	public static int [] unboxInteger(Collection<Integer> collection) {
		if (collection ==  null) {
			return null;
		}
		
		Integer[] src = filterNullValues(collection).toArray( new Integer[collection.size()]);
		int[] dst = new int[src.length];

		for (int idx = 0; idx < src.length; idx++) {
			dst[idx] = src[idx];
		}
		
		return dst;
	}

	public static long [] unboxLong(Long[] src) {
		if (src == null) {
			return null;
		}
				
		long [] dst = new long[src.length];
		for (int idx = 0; idx < src.length; idx++) {
			if (src[idx] != null) {
				dst[idx] = src[idx];
			}
		}
		return dst;
	}

	public static long [] unboxLong(Collection<Long> collection) {
		
		Long[] src = filterNullValues(collection).toArray(new Long[0]);
		long[] dst = new long[src.length];

		for (int idx = 0; idx < src.length; idx++) {
			dst[idx] = src[idx];
		}
		
		return dst;
	}

	private static <T> Collection<T> filterNullValues(Collection<T> src) {
		
		List<T> result = new ArrayList<T>();
		if (src != null) {
			for (T value : src) {
				if (value != null) {
					result.add(value);
				}
			}
		}
		return result;
	}
	
	public static String[] padTo(String[] values, char padChar, int length) {
		if (values != null) {
			String[] result = new String[values.length];
			for (int idx = 0; idx < values.length; idx++) {
				result[idx] = Info.padTo(values[idx], padChar, length);
			}
			return result;
		}
		return new String[0];
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<T> type, int length) {
		return (T[]) Array.newInstance(type, length);
	}
	
	public static int[] castToPrimitiveInt(Integer[] array) {   
        if (array == null) {   
            return null;   
        } else if (array.length == 0) {   
            return new int[0];   
        }   
        final int[] result = new int[array.length];   
        for (int i = 0; i < array.length; i++) {   
            result[i] = array[i].intValue();   
        }   
        return result;   
    }  

	public static HashMap<String,String> convertKeyValuePairsToHashMap(String[] stringArray, String delimiters) {
		HashMap<String,String> hashMap = new HashMap<String,String>();

		if (stringArray != null) {
			// key-value pairs are separated by delimiters - parse these into
			// the array list
			for (String arrayElement : stringArray) {
				String[] keyValuePair = stringToArray(arrayElement, delimiters);
				hashMap.put(keyValuePair[0], keyValuePair[1]);
			}
		}

		return hashMap;
	}

	public static HashMap<String,String> convertKeyValuePairsToHashMap(Collection<String> pairs, String delimiters) {
		HashMap<String,String> hashMap = new HashMap<String,String>();

		if (pairs != null) {
			// key-value pairs are separated by delimiters - parse these into
			// the array list
			for (String pair : pairs) {
				String[] keyValuePair = stringToArray(pair, delimiters);
				hashMap.put(keyValuePair[0], keyValuePair[1]);
			}
		}

		return hashMap;
	}
	
	public static String[] stringToArray(String string, String delimeters) {

		if (string == null) {
			return new String[0];
		}

		List<String> list = new ArrayList<String>(10);
		StringTokenizer t = (delimeters == null) ? new StringTokenizer(string) : new StringTokenizer(string, delimeters);
		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}

		return (String[])list.toArray(new String[list.size()]);
	}
}
