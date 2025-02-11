/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.config;

import java.util.Date;
import java.util.List;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ConfigurationManager {

	void refresh();

	void overrideValue(String key, Object value, Date fromDate, Date toDate);

	void overrideValue(String key, Object value);
	
	void resetValue(String key);
	
	<T> T getValue(String key, Class<T> targetType);

	<T> T getValue(String key, Class<T> targetType, T defaultValue);
	
	<E> List<E> getValues(String key, Class<E> targetType);

	<E> List<E> getValues(String key, Class<E> targetType, List<E> defaultValues);
	
	<E> List<E> getValues(String key, Class<E> targetType, String delimiter);

	<E> List<E> getValues(String key, Class<E> targetType, String delimiter, List<E> defaultValues);

	String buildPath(String ... segments);
	
	// convenient methods
	
	String getStringValue(String key);

	String getStringValue(String key, String defaultValue);

	List<String> getStringValues(String key);
	
	List<String> getStringValues(String key, List<String> defaultValues);

	List<String> getStringValues(String key, String delimiter, List<String> defaultValues);
	
	Long getLongValue(String key);

	Long getLongValue(String key, long defaultValue);

	Integer getIntegerValue(String key);

	Integer getIntegerValue(String key, int defaultValue);
	
	Boolean getBooleanValue(String key);
	
	Boolean getBooleanValue(String key, boolean defaultValue);

	Date getDateValue(String key);
	
	Date getDateValue(String key, Date defaultValue);
	
	void destroy();
}
