/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.provider.account;

import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.VendorServiceChangeHistory;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMVendorServiceChangeHistory extends BaseProvider implements VendorServiceChangeHistory {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @link aggregation
	 */
	private final VendorServiceChangeHistoryInfo delegate;
	private TMServiceChangeHistory[] promoSocs;

	public TMVendorServiceChangeHistory(TMProvider provider, VendorServiceChangeHistoryInfo delegate) {
		super(provider);
		this.delegate = delegate;
		this.promoSocs = (TMServiceChangeHistory[])decorate((ServiceChangeHistoryInfo[])delegate.getPromoSOCs());
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------

	public String getVendorServiceCode() {
		return delegate.getVendorServiceCode();
	}

	public ServiceChangeHistory[] getPromoSOCs() {
		return promoSocs;
	}
	
	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	
	private ServiceChangeHistory[] decorate(ServiceChangeHistoryInfo[] historyInfoArray) {

		TMServiceChangeHistory[] tmServiceChangeHistories = new TMServiceChangeHistory[historyInfoArray.length];
		for (int i = 0; i < historyInfoArray.length; i++) {
			tmServiceChangeHistories[i] = new TMServiceChangeHistory(provider, (ServiceChangeHistoryInfo)historyInfoArray[i]);
		}
		
		return tmServiceChangeHistories;
	}
}







