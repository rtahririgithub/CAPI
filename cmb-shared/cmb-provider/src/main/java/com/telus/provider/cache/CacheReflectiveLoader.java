/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.provider.cache;

import java.lang.reflect.Method;

import com.telus.provider.cache.CacheException;;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheReflectiveLoader implements CacheLoader {

	private Object target;
	
	private String methodName;

	
	public CacheReflectiveLoader(Object target, String methodName) {
		this.target = target;
		this.methodName = methodName;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
	 */
	public Object load(String key) throws CacheException {
		Object result = null;
		
		try {
			
			Method method = target.getClass().getMethod(methodName, (Class<?>[]) null);
			if (method != null) {
				result = method.invoke(target, (Object[]) null);
			}
			
		} catch (Throwable e) {
			throw new CacheException("Unable to load cache data: " + e.getMessage(), e);
		}
		return result;
	}

}
