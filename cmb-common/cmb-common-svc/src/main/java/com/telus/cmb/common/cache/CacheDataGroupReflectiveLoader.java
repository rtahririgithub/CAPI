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

import net.sf.ehcache.constructs.blocking.CacheEntryFactory;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Pavel Simonovsky
 *
 */
public class CacheDataGroupReflectiveLoader<T> implements CacheEntryFactory {

	private static final Log logger = LogFactory.getLog(CacheDataGroupReflectiveLoader.class);
	
	private Object target;
	
	private String methodName;
	
	private CacheKeyProvider keyProvider;
	
	public CacheDataGroupReflectiveLoader(Object target, String methodName, CacheKeyProvider keyProvider) {
		this.target = target;
		this.methodName = methodName;
		this.keyProvider = keyProvider;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.ehcache.constructs.blocking.CacheEntryFactory#createEntry(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public synchronized Object createEntry(Object key) throws Exception {
		
		synchronized (this) {
			logger.info("Loading cache data group using method [" + methodName + "]");
			
			Object data = MethodUtils.invokeMethod(target, methodName, null);
			
			CacheDataGroup<T> dataGroup = null;
			
			if (data != null) {
			
				if (data instanceof Map<?, ?> ) {
					dataGroup = new CacheDataGroup<T>((Map<String, T>) data);
				} else if (data instanceof Collection<?>) {
					dataGroup = new CacheDataGroup<T>((Collection<T>) data, keyProvider);
				} else if (data.getClass().isArray()) {
					dataGroup = new CacheDataGroup<T>((T []) data, keyProvider);
				} else {
					dataGroup = new CacheDataGroup<T>((T) data, keyProvider);
				}
				
				logger.info("Loaded [" + dataGroup.getSize() + "] elements");
						
	//			if (logger.isDebugEnabled()) {
	//				for(T entry : dataGroup.getAll()) {
	//					logger.debug("Key = [" + keyProvider.getCacheKey(entry) + "]");
	//				}
	//			}
				
			} else {
				logger.info("Loaded [0] elements");
			}
			
			return dataGroup;
		}
	}
}
