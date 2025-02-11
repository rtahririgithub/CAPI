/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc.impl;

import com.telus.api.reference.Reference;
import com.telus.cmb.common.cache.CacheKeyProvider;
import com.telus.cmb.reference.bo.ReferenceBo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataCacheKeyProvider implements CacheKeyProvider {

	/* (non-Javadoc)
	 * @see com.telus.cmb.cache.CacheKeyProvider#getCacheKey(java.lang.Object)
	 */
	@Override
	public String getCacheKey(Object entry) {
		if (entry != null) {
			if (entry instanceof Reference) {
				return ((Reference) entry).getCode();
			} else if (entry instanceof ReferenceBo<?>) {
				return ((Reference)((ReferenceBo<?>) entry).getDelegate()).getCode();
			} else {
				throw new IllegalArgumentException("Unsupported class " + entry.getClass());
			}
		}
		return null;
	}

}
