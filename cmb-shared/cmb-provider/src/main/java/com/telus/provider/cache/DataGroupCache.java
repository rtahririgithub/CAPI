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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Simonovsky
 *
 */
public class DataGroupCache extends DataCache {

	private static final String DEFAULT_KEY = "defaultKey";
	
	private CacheLoader loader;
	
	private CacheKeyProvider keyProvider;
	
	public DataGroupCache(Class type, String group, CacheLoader loader, CacheKeyProvider keyProvider) {
		super(type, group);
		this.loader = loader;
		this.keyProvider = keyProvider;
	}
	
	public Object get(String key) throws CacheException {
		CacheElement element = getCacheElement(DEFAULT_KEY);
		if (element == null) {
			element = loadDataGroup();
		}
		Map group = (Map) element.getData();
		return group.get(key);
	}
	
	public Object[] getAll() throws CacheException {
		CacheElement element = getCacheElement(DEFAULT_KEY);
		if (element == null) {
			element = loadDataGroup();
		}
		Map group = (Map) element.getData();
		return group.values().toArray((Object[]) Array.newInstance(getType(), group.size()));
	}
	
	private CacheElement loadDataGroup() throws CacheException { 
		try {
			
			Object result = loader.load("");
			
			//use LinkedHashMap to preserve the data collectin's sequence.
			Map group = new LinkedHashMap();
			
			if (result != null) {
				
				List data = new ArrayList();
				
				if (result.getClass().isArray()) {
					data.addAll(Arrays.asList((Object[]) result));
				} else if (result instanceof Collection) {
					data.addAll((Collection) result);
				} else {
					data.add(result);
				}
				
				Iterator iter = data.iterator();
				while (iter.hasNext()) {
					Object object = iter.next();
					group.put(keyProvider.getKey(object), object);
				}
			}
			
			return putCacheElement(DEFAULT_KEY, group);
			
		} catch (Exception e) {
			throw new CacheException("Error loading group data: " + e.getMessage(), e);
		}
	}
}
