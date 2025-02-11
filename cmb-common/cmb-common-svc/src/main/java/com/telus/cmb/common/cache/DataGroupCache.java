/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.cache;


import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.cmb.common.util.ArrayUtil;

/**
 * @author Pavel Simonovsky
 *
 */
public class DataGroupCache<T> {
	
	private static final Log logger = LogFactory.getLog(DataGroupCache.class);
	public static final String CACHE_DEFAULT_KEY = "defaultKey";

	private SelfPopulatingCache cache;
	
	private Class<T> type;
	
	private boolean firstExecution = true;
	
	public DataGroupCache(Class<T> type, String cacheName, CacheManager cacheManager, CacheEntryFactory cacheEntryFactory, String defaultCacheName, boolean preload) {
		
		this.type = type;
 		
		Ehcache ehcache = CacheUtil.getCache(cacheName, defaultCacheName, cacheManager);
		cache = new SelfPopulatingCache(ehcache, cacheEntryFactory);
		cacheManager.replaceCacheWithDecoratedCache(ehcache, cache);
		
		if (preload) {
			try {
				getAll();
			}catch (Exception e) {
				logger.error("Error preloading cache for ["+cacheName+"]", e);
			}
		}
	}
	
	public DataGroupCache(Class<T> type, String cacheName, CacheManager cacheManager, CacheEntryFactory cacheEntryFactory, String defaultCacheName) {
		this(type, cacheName, cacheManager, cacheEntryFactory, defaultCacheName, false);
	}
	
	public DataGroupCache(Class<T> type, String cacheName, CacheManager cacheManager, CacheEntryFactory cacheEntryFactory) {
		this(type, cacheName, cacheManager, cacheEntryFactory, null);
	}
	
	public T get(String key) {
		CacheDataGroup<T> dataGroup = getDataGroup();
		return dataGroup == null ? null : dataGroup.get(key);
	}
	
	public T[] get(String[] keys) {
		CacheDataGroup<T> dataGroup = getDataGroup();
		return dataGroup == null ? null : dataGroup.get(keys).toArray(ArrayUtil.newArray(type, 0));
	}
	
	public T[] getAll() {
		CacheDataGroup<T> dataGroup = getDataGroup();
		return dataGroup == null ? null : dataGroup.getAll().toArray(ArrayUtil.newArray(type, 0));
	}

	// Defect PROD00178088 Start	
	public T[] getAllByCode(String key) {
		CacheDataGroup<T> dataGroup = getDataGroup();
		return dataGroup == null ? null : dataGroup.getAllByCode(key).toArray(ArrayUtil.newArray(type, 0));
	}
	// Defect PROD00178088 Start
	
	
	@SuppressWarnings("unchecked")
	private CacheDataGroup<T> getDataGroup() {
		Element element = null;
		if (firstExecution) {
			synchronized (this) { //prevent multiple database retrieval on first call
				element = cache.get(CACHE_DEFAULT_KEY);
				firstExecution = false;
			}
		}else {
			element = cache.get(CACHE_DEFAULT_KEY);
		}
		return element == null ? null : (CacheDataGroup<T>) element.getValue();
	}
	
	public boolean containsKey(String key){
		CacheDataGroup<T> dataGroup = getDataGroup();
		return dataGroup == null ? null : dataGroup.containsKey(key);
	}
}
