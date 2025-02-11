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
public class DataEntryCache extends DataCache {

	public DataEntryCache(Class type, String group) {
		super(type, group);
	}
	
	public Object get(String key, CacheLoader loader) throws CacheException {
		
		CacheElement element = getCacheElement(key);
		if (element == null) {
			try {

				element = putCacheElement(key, loader.load(key));
				
			} catch (Exception e) {
				throw new CacheException("Error loading data for key [" + key + "]: " + e.getMessage(), e);
			}
		}
		return element.getData();
	}
	
}
