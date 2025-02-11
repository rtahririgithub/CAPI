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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Pavel Simonovsky
 *
 */
public class StringUtilTest {

	@Test
	public void testArrayToStringWithValue(){
		
		String separator = ".";
		String[] array = new String[]{"a","b"};
		
		String result;
		result = StringUtil.arrayToString(array, separator);
		
		assertEquals("a.b",result);
		
		array=null;
		separator=null;
		result = StringUtil.arrayToString(array, separator);
		
		assertEquals(null,result);
		
	}
}
