/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.perfmon;

import java.util.Date;

import org.junit.Test;
import org.quartz.CronExpression;


/**
 * @author Pavel Simonovsky
 *
 */
public class CronExpressionTest {

	@Test
	public void testCronExpression() throws Exception {
		
		CronExpression expression = new CronExpression("0 0/5 * * * ?");

		final Date nextValidDate1 = expression.getNextValidTimeAfter(new Date());
		final Date nextValidDate2 = expression.getNextValidTimeAfter(nextValidDate1);
		
		System.out.println(nextValidDate1);
		System.out.println(nextValidDate2);
	}
}
