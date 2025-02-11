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

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheDataGroupStaticLoader<T> implements CacheEntryFactory {

	private CacheDataGroup<T> dataGroup;
	
	@SuppressWarnings("unchecked")
	public CacheDataGroupStaticLoader(Object[] entries, CacheKeyProvider keyProvider) {
		List<T> data = new ArrayList<T>();
		for(Object entry : entries) {
			data.add((T) entry);
		}
		dataGroup = new CacheDataGroup<T>(data, keyProvider);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.ehcache.constructs.blocking.CacheEntryFactory#createEntry(java.lang.Object)
	 */
	@Override
	public Object createEntry(Object key) throws Exception {
		return dataGroup;
	}
}
