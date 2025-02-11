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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheUtil {
	
	private static final Log logger = LogFactory.getLog(CacheUtil.class);

	/**
	 * Creates a new ehcache based on existing cache configuration under 'defaultCacheName'.
	 * 
	 * @param cacheName
	 * @param defaultCacheName
	 * @param cacheManager
	 * @return
	 */
	public static Ehcache getCache(String cacheName, String defaultCacheName, CacheManager cacheManager) {
		
		Ehcache ehcache = null;
		
		// try to use existing cache defined in configuration first 
		
		ehcache = cacheManager.getEhcache(cacheName);
		
		if (ehcache == null && defaultCacheName != null) {
			// try to create a new cache based on template cache configuration
			logger.debug("Creating cache [" + cacheName + "] using default cache configuration [" + defaultCacheName + "].");
			
			Ehcache defaultCache = cacheManager.getEhcache(defaultCacheName);

			if (defaultCache != null) {
				CacheConfiguration config = defaultCache.getCacheConfiguration();
				
				ehcache = new Cache(cacheName, config.getMaxElementsInMemory(), config.getMemoryStoreEvictionPolicy(), 
						config.isOverflowToDisk(), null, config.isEternal(), config.getTimeToLiveSeconds(), config.getTimeToIdleSeconds(), 
						config.isDiskPersistent(), config.getDiskExpiryThreadIntervalSeconds(),	null);
				cacheManager.addCache(ehcache);
				
			} else {
				logger.warn("Unable to get template cache for name [" + defaultCacheName + "]");
			}
		}

		if (ehcache == null) {
			// create a new cache based on defualt cache configuration
			logger.debug("Creating cache [" + cacheName + "] using ehcache default configuration.");
			
			cacheManager.addCache(cacheName);
		}
 		
		return cacheManager.getEhcache(cacheName);
	}
}
