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

/**
 * @author Pavel Simonovsky
 *
 */
public interface PerformanceMonitorMBean {

	String getReport();

	void reset();
	
}
