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

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pavel Simonovsky
 *
 */
public abstract class DataCache {

	protected Map elementMap = Collections.synchronizedMap( new LinkedHashMap());
	
	private Class type;
	
	private String name;
	
	public DataCache(Class type, String group) {
		this.type = type;
		this.name = buildCacheName(type.getName(), group);
	}
	
	public class CacheElement {

		private Object data;

		private long creationTimestamp;
		
		private long lastAccessTimestamp;
		
		public CacheElement(Object data) {
			this.data = data;
			creationTimestamp = System.currentTimeMillis();
		}
		
		public void setData(Object data) {
			this.data = data;
		}
		
		public Object getData() {
			lastAccessTimestamp = System.currentTimeMillis();
			return data;
		}
		
		public long getCreationTimestamp() {
			return creationTimestamp;
		}

		public long getLastAccessTimestamp() {
			return lastAccessTimestamp;
		}
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(Class type) {
		this.type = type;
	}
	
	public Class getType() {
		return type;
	}
	
	protected CacheElement putCacheElement(String key, Object data) {
		CacheElement element = new CacheElement(data);
		elementMap.put(key, element);
		return element;
	}
	
	protected CacheElement getCacheElement(String key) {
		return (CacheElement) elementMap.get(key);
	}
	
	public void clear() {
		synchronized (elementMap) {
			elementMap.clear();
		}
	}
	
	static String buildCacheName(String name, String group) {
		StringBuffer buffer = new StringBuffer(name);
		if (group != null && !group.trim().equals("")) {
			buffer.append(".").append(group);
		}
		return buffer.toString();
	}
	
	protected int getObjectSize(Object obj) {
		int result = -1;
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			
			oos.writeObject(obj);
			
			result = bos.toByteArray().length;
			
			oos.close();
			bos.close();
			
		} catch (Throwable t) {
			System.err.println("Unable to calculate object size: " + t.getMessage());
		}
		return result;
	}
	
	public static String getComplexKey(Object[] params) {
		StringBuffer buffer = new StringBuffer();
		for (int idx = 0; idx < params.length; idx++) {
			if (idx > 0) {
				buffer.append('.');
			}
			Object param = params[idx];
			if (param != null) {
				buffer.append(param.toString());
			}
		}
		return buffer.toString();
	}
	
	public CacheElement[] evictCacheElements(long timeToLiveThreshold, long idleTimeThreshold) {
		
		List result = new ArrayList();
		
		synchronized (elementMap) {
			Iterator iter = elementMap.entrySet().iterator();
			while (iter.hasNext()) {
				
				CacheElement element = (CacheElement)((Map.Entry) iter.next()).getValue();

				long currentTimestamp = System.currentTimeMillis();
				
				if (timeToLiveThreshold != 0 && (element.getCreationTimestamp() + timeToLiveThreshold <= currentTimestamp) || 
					idleTimeThreshold != 0 && (element.getLastAccessTimestamp() + idleTimeThreshold <= currentTimestamp)) {
					result.add(element);
					iter.remove();
				}
			}
		}
		return (CacheElement[]) result.toArray( new CacheElement[result.size()]);
	}
	
	public int getSize() {
		return elementMap.size();
	}
}
