/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.CallList;
import com.telus.api.account.CallSummary;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CallSummaryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMCallList extends BaseProvider implements CallList {
	
	/**
	 * @link aggregation
	 */
	private final CallListInfo delegate;
	private final int ban;
	private final String subscriberId;
	private final String productType;
	private final int billSeqNo;
	
	public TMCallList(TMProvider provider, CallListInfo delegate, int ban, String subscriberId, String productType, int billSeqNo) {		
		super(provider);
		this.delegate = delegate;
		this.ban = ban;
		this.subscriberId = subscriberId;
		this.productType = productType;
		this.billSeqNo = billSeqNo;
	}
	
	public CallListInfo getDelegate() {
		return delegate;
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public int getTotalCallCount() {
		return delegate.getTotalCallCount();
	}
	
	public String toString() {
		return delegate.toString();
	}	
	
	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public CallSummary[] getCallSummaries() throws TelusAPIException {
		return decorate(delegate.getCallSummaries());
	}
	
	private CallSummary[] decorate(CallSummary[] callSummaries) throws TelusAPIException {
		
		try {
			TMCallSummary[] tmCallSummaries = new TMCallSummary[callSummaries.length];
		    for (int i = 0; i < callSummaries.length; i++) {
		    	TMCallSummary tmCallSummary = new TMCallSummary(provider, (CallSummaryInfo)callSummaries[i], ban, subscriberId, productType, billSeqNo);
		    	tmCallSummaries[i] = tmCallSummary;
		    }
		    return tmCallSummaries;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}
	
}



