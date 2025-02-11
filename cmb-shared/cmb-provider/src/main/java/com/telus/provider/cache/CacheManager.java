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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheManager {
	
	private static CacheManager instance;
	
	public static CacheManager getInstance() {
		if (instance == null) {
			instance = new CacheManager();
		}
		return instance;
	}

	private Map cacheMap = new HashMap();

	public DataEntryCache createDataEntryCache(Class type) {
		return createDataEntryCache(type, null);
	}

	public DataEntryCache createDataEntryCache(Class type, String group) {
		DataEntryCache cache = new DataEntryCache(type, group);
		registerCache(cache);
		return cache;
	}
	
	public DataGroupCache createDataGroupCache(Class type, CacheLoader loader, CacheKeyProvider keyProvider) {
		return createDataGroupCache(type, null, loader, keyProvider);
	}

	public DataGroupCache createDataGroupCache(Class type, String group, CacheLoader loader, CacheKeyProvider keyProvider) {
		DataGroupCache cache = new DataGroupCache(type, group, loader, keyProvider);
		registerCache(cache);
		return cache;
	}
	
	private void registerCache(DataCache cache) {
		cacheMap.put(cache.getName(), cache);
	}
	
	public DataGroupCache getDataGroupCache(Class type) {
		return getDataGroupCache(type, null);
	}
	
	public DataGroupCache getDataGroupCache(Class type, String group) {
		String name = DataCache.buildCacheName(type.getName(), group);
		return (DataGroupCache) cacheMap.get(name);
	}

	public DataEntryCache getDataEntryCache(Class type, String group) {
		String name = DataCache.buildCacheName(type.getName(), group);
		return (DataEntryCache) cacheMap.get(name);
	}
	
	public DataEntryCache getDataEntryCache(Class type) {
		return getDataEntryCache(type, null);
	}
	public void clearAll() {
		Iterator iter = cacheMap.values().iterator();
		while (iter.hasNext()) {
			DataCache cache = (DataCache) iter.next();
			cache.clear();
		}
	}
	
}
