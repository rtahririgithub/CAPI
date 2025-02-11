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
public class PerformanceReportWriterTest {

	@Test
	public void testWriteReport() throws Exception {
		
		PerformanceMonitor monitor = new PerformanceStatisticsMonitor();
		
		MethodInvocationProfiler profiler1 = new MethodInvocationProfiler("com.telus.cmb.ClassA", "testMethodA");
		profiler1.start();
		Thread.sleep(50);
		profiler1.stop();
		monitor.handleMethodInvocation(profiler1);
		
		PerformanceReportWriter writer = new PerformanceReportWriter();
		writer.setBaseName("perfstats");
		writer.setExtension("txt");
		writer.setTimestampPattern("-ddMM-hhmmss");
		writer.setMaxHistoryFiles(3);
		writer.setMonitor(monitor);
		
		writer.writeReport();
	}
}
