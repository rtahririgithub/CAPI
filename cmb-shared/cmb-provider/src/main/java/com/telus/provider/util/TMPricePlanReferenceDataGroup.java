/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import java.util.Iterator;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.Reference;
import com.telus.provider.reference.TMPricePlan;
import com.telus.provider.reference.TMReferenceDataManager;


public class TMPricePlanReferenceDataGroup extends TMReflexiveReferenceDataGroup {

	private final TMReferenceDataManager referenceDataManager;


	public TMPricePlanReferenceDataGroup(TMReferenceDataManager referenceDataManager) {
		super(PricePlan.class, null, null);
		this.referenceDataManager = referenceDataManager;
	}

	public synchronized void reset()throws TelusAPIException {      
		Iterator entryIter = cache.entrySet().iterator();  
		long currentTime = System.currentTimeMillis();
		int counter = 0;
		Logger.debug0("Number of price plan cache [ " + cache.size() + "]"); 
		while(entryIter.hasNext()){
			Map.Entry entry = (Map.Entry)entryIter.next();
			String key = (String)entry.getKey();  // Get the key from the entry.
			TMPricePlan value = (TMPricePlan)entry.getValue();  
			//          TMProvider.debug0( "   (" + key + ", " + value.getDescription());
			//          TMProvider.debug0("Current Time [" + new Date(currentTime) + "]-- last time cache[" + new Date(value.getLastCacheTime())+ "]"); 
			if((currentTime - value.getLastCacheTime()) > (1000L * 60L * 120)){
				//              TMProvider.debug0( "removing   (" + key + ", " + value.getDescription() + ", " + value.getLastCacheTime()+ ")");
				entryIter.remove();
				counter++;
			}
		}
		Logger.debug0("Number of price plan cache after the clean up[ " + cache.size() + "]");
		Logger.debug0("Number of price plan removed[ " + counter + "]");
	}

	public synchronized void reload() {
		cache.clear();
		cacheAsArray = new Reference[0];
	}

	public Reference[] getAll() throws TelusAPIException {
		throw new UnsupportedOperationException("method call is inappropriate, not all data will be available");
	}

	public Reference get(String code) throws TelusAPIException {
		throw new UnsupportedOperationException("method call is inappropriate");
	}

	private String getKey(String pricePlanCode, String equipmentType,
			String provinceCode, char accountType, char accountSubType, int brandId) throws TelusAPIException {
		return "["   + pricePlanCode +
		"]-[" + equipmentType +
		"]-[" + provinceCode +
		"]-[" + accountType +
		"]-[" + accountSubType + "]"+
		"]-[" + brandId;
	}

	public PricePlan get(String pricePlanCode, String equipmentType, String provinceCode,
			char accountType, char accountSubType, int brandId) throws TelusAPIException {

		String key = getKey(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);

		PricePlan r = (PricePlan)super.get(key);
		if(r == null) {
			r = get0(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);
		}
		((TMPricePlan)r).setLastCacheTime(System.currentTimeMillis());
		return r;
	}

	private PricePlan get0(String pricePlanCode, String equipmentType, String provinceCode,
			char accountType, char accountSubType, int brandId) throws TelusAPIException {
		try {
			PricePlan result = referenceDataManager.decorate(
					referenceDataManager.getReferenceDataFacade().retrievePricePlan(pricePlanCode,
							equipmentType, provinceCode, accountType, accountSubType, brandId));

			String key = getKey(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);
			addData(key, result);

			exception = null;

			return result;
		}catch (Throwable e) {
			Logger.debug0(e);
			exception = e;
			return null;
		} finally {

		}
	}

}



