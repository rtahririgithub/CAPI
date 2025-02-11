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

import org.junit.Test;


/**
 * @author Pavel Simonovsky
 *
 */
public class PerformanceMonitorTest {

	@Test
	public void testPerformanceMonitor() throws Exception {
		
		PerformanceMonitor monitor = new PerformanceStatisticsMonitor();
		
		MethodInvocationProfiler profiler1 = new MethodInvocationProfiler("com.telus.cmb.ClassA", "testMethodA");
		profiler1.start();
		Thread.sleep(50);
		profiler1.stop();
		monitor.handleMethodInvocation(profiler1);

		MethodInvocationProfiler profiler2 = new MethodInvocationProfiler("com.telus.cmb.ClassB", "testMethodB");
		profiler2.start();
		Thread.sleep(50);
		profiler2.stop();
		monitor.handleMethodInvocation(profiler2);

		MethodInvocationProfiler profiler3 = new MethodInvocationProfiler("com.telus.cmb.ClassB", "testMethodB");
		profiler3.start();
		Thread.sleep(50);
		profiler3.stop();
		monitor.handleMethodInvocation(profiler3);
		
		System.out.println(monitor.getReport());
		
	}
	
	@Test
	public void testReplacement() {
		String home = System.getProperty("user.home");
		
		home = home.replace('\\', '/');
		
		System.out.println(home);
	}
}
