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

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheStaticDataLoader implements CacheLoader {

	private Object[] data;
	
	public CacheStaticDataLoader(Object[] data) {
		this.data = data;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.provider.cache.CacheLoader#load(java.lang.String)
	 */
	public Object load(String key) throws CacheException {
		return data;
	}

}
