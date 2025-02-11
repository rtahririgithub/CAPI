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

import java.io.Serializable;
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
public class CacheDataGroup<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<String, T> dataMap = new LinkedHashMap<String, T>();
	
	public CacheDataGroup(T[] entries, CacheKeyProvider keyProvider) {
		this(Arrays.asList(entries), keyProvider);
	}

	public CacheDataGroup(T entry, CacheKeyProvider keyProvider) {
		String key = keyProvider.getCacheKey(entry);
		dataMap.put(key, entry);
	}

	public CacheDataGroup(Collection<T> entries, CacheKeyProvider keyProvider) {
		for (T entry : entries) {
			String key = keyProvider.getCacheKey(entry);
			dataMap.put(key, entry);
		}
	}

	public CacheDataGroup(Map<String, T> map) {
		dataMap.putAll( map );
	}
	
	public T get(String key) {
		return dataMap.get(key);
	}
	
	public Collection<T> get(String[] keys) {
		List<T> result = new ArrayList<T>();
		for (String key : keys) {
			T value = dataMap.get(key);
			if (value != null) {
				result.add(value);
			}
		}
		return result;
	}
	
	public Collection<T> getAll() {
		return dataMap.values();
	}

	
	// Defect PROD00178088 Start	
	public Collection<T> getAllByCode(String key) {		
		List<T> result = new ArrayList<T>();		
		
		if (key == null)
			return result;				
		
		Iterator<Map.Entry<String,T>> i = dataMap.entrySet().iterator();
		while(i.hasNext()){
			Map.Entry<String,T> entry = i.next();
			if (entry.getKey().toUpperCase().startsWith((key.toUpperCase()+ "."))){
				result.add(entry.getValue());
			}
		}
		return result;
	}
	// Defect PROD00178088 End	
	
	
	public int getSize() {
		return dataMap.size();
	}
	
	public boolean containsKey(String key){
		if(dataMap.containsKey(key))
			return true;
		return false;
	}
	
}
