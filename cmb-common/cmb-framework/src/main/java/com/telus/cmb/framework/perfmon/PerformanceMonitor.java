/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.perfmon;

import java.util.Collection;

/**
 * @author Pavel Simonovsky
 *
 */
public interface PerformanceMonitor {
	
	void handleMethodInvocation(MethodInvocationProfiler profiler);
	
	Collection<MethodInvocationStatistics> getStatistics();

	void reset();
	
	String getReport();
	
	String getTextReport();
	
}
