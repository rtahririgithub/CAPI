/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.cache;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheKeyBuilder {
	
	private static final String COMPLEX_KEY_DELIMITER = ".";
	
	public static String createComplexKey(Object ... keys) {
		StringBuffer buffer = new StringBuffer();

		for (int idx = 0; idx < keys.length; idx++) {
			if (idx != 0) {
				buffer.append(COMPLEX_KEY_DELIMITER);
			}
			buffer.append(keys[idx]);
		}
		return buffer.toString();
	}

}
