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

import java.util.Collection;
import java.util.Map;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.loader.CacheLoader;

/**
 * @author Pavel Simonovsky
 *
 */
public class DataEntryCache<T> {

	public static final String CACHE_DEFAULT_KEY = "defaultKey";
	
	private Ehcache cache;
	
	public DataEntryCache(String cacheName, CacheManager cacheManager, String defaultCacheName) {
		cache = CacheUtil.getCache(cacheName, defaultCacheName, cacheManager);
	}

	public DataEntryCache(String cacheName, CacheManager cacheManager) {
		this(cacheName, cacheManager, null);
	}
	
	@SuppressWarnings("unchecked")
	public T get(String key, final CacheEntryFactory entryFactory) {
		Element element = cache.getWithLoader(key, new CacheLoaderAdapter(entryFactory), null); 
		return element == null ? null : (T) element.getValue();
	}

	private class CacheLoaderAdapter implements CacheLoader {

		private CacheEntryFactory entryFactory;
		
		public CacheLoaderAdapter(CacheEntryFactory entryFactory) {
			this.entryFactory = entryFactory;
		}
		
		@Override
		public CacheLoader clone(Ehcache cache) throws CloneNotSupportedException {
			return null;
		}

		@Override
		public void dispose() throws CacheException {
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public Status getStatus() {
			return null;
		}

		@Override
		public void init() {
		}

		@Override
		public Object load(Object key, Object argument) {
			return null;
		}

		/* (non-Javadoc)
		 * @see net.sf.ehcache.loader.CacheLoader#load(java.lang.Object)
		 */
		@Override
		public Object load(Object key) throws CacheException {
			try {
				return entryFactory.createEntry(key);
			} catch (Exception e) {
				throw new CacheException(e);
			}
		}

		/* (non-Javadoc)
		 * @see net.sf.ehcache.loader.CacheLoader#loadAll(java.util.Collection, java.lang.Object)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Map loadAll(Collection keys, Object argument) {
			return null;
		}

		/* (non-Javadoc)
		 * @see net.sf.ehcache.loader.CacheLoader#loadAll(java.util.Collection)
		 */
		@Override
		@SuppressWarnings("unchecked")
		public Map loadAll(Collection keys) {
			return null;
		}
	}
	
}
