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
import java.util.Collection;

import org.junit.Test;

/**
 * @author Pavel Simonovsky
 *
 */
public class ArrayUtilTest {

	@Test
	public void testUnbox() throws Exception {
		Collection<Long> src = new ArrayList<Long>();
		
		src.add(new Long(1));
		
		long [] result = ArrayUtil.unboxLong(src);
		
		System.out.println(result.length);
	}
}
