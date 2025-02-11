/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ReferenceServiceController {

	void clearCache();
	
	void restartCache();

	String getPerformanceData();
	
	String getLoggingLevel();
	
	void setLoggingLevel(String loggingLevel);
	
	String getCacheConfiguration( String cacheName );
	
	String getCacheStatistics( String cacheName );

	void clearCache(String cacheName);
	
	void refreshAllCache();
	
	void refreshCache(String cacheName);
}
