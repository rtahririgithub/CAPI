package com.telus.cmb.common.cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

/**
 * @author Michael Liao
 *
 */
public class SelfPopulatiingDataEntryCache<T> {

	private Ehcache cache;
	
	public SelfPopulatiingDataEntryCache(String cacheName, CacheManager cacheManager, String defaultCacheName, CacheEntryFactory cacheEntryFactory) {
		
		Ehcache ehcache = CacheUtil.getCache(cacheName, defaultCacheName, cacheManager);
		cache = new SelfPopulatingCache(ehcache, cacheEntryFactory);
		cacheManager.replaceCacheWithDecoratedCache(ehcache, cache);
	}
	
	@SuppressWarnings("unchecked")
	public T get( Object key ) {
		Element element = cache.get( key ); 
		return element == null ? null : (T) element.getValue();
	}
	
}
