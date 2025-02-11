/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Call;
import com.telus.api.account.CallSummary;
import com.telus.eas.subscriber.info.CallSummaryInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMCallSummary extends BaseProvider implements CallSummary {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private final CallSummaryInfo delegate;
	private final int ban;
	private final String subscriberId;
	private final String productType;
	private final int billSeqNo;
	
	public TMCallSummary(TMProvider provider, CallSummaryInfo delegate, int ban, String subscriberId, String productType, int billSeqNo) {		
		super(provider);
		this.delegate = delegate;
		this.ban = ban;
		this.subscriberId = subscriberId;
		this.productType = productType;
		this.billSeqNo = billSeqNo;
	}
	
	public CallSummaryInfo getDelegate() {
		return delegate;
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public Date getDate() {
		return delegate.getDate();
	}
	
	public String getSwitchId() {
		return delegate.getSwitchId();
	}
	
	public String getProductType() {
		return delegate.getProductType();
	}
	
	public String getLocationDescription() {
		return delegate.getLocationDescription();
	}
	
	public String getLocationProvince() {
		return delegate.getLocationProvince();
	}
	
	public String getLocationCity() {
		return delegate.getLocationCity();
	}
	
	public String getCallToCity() {
		return delegate.getCallToCity();
	}
	
	public String getCallToState() {
		return delegate.getCallToState();
	}
	
	public String getCallToNumber() {
		return delegate.getCallToNumber();
	}
	
	public double getCallDuration() {
		return delegate.getCallDuration();
	}
	
	public double getAirtimeChargeAmount() {
		return delegate.getAirtimeChargeAmount();
	}
	
	public double getTollChargeAmount() {
		return delegate.getTollChargeAmount();
	}
	
	public double getAdditionalChargeAmount() {
		return delegate.getAdditionalChargeAmount();
	}
	
	public double getTaxAmount() {
		return delegate.getTaxAmount();
	}
	
	public double getCreditedAmount() {
		return delegate.getCreditedAmount();
	}
	
	public String getPeriodLevel() {
		return delegate.getPeriodLevel();
	}
	
	public double getRoamingTaxTollAmount() {
		return delegate.getRoamingTaxTollAmount();
	}
	
	public double getRoamingTaxAirtimeAmount() {
		return delegate.getRoamingTaxAirtimeAmount();
	}
	
	public double getRoamingTaxAdditionalAmount() {
		return delegate.getRoamingTaxAdditionalAmount();
	}
	
	public boolean isExtendedHomeArea() {
		return delegate.isExtendedHomeArea();
	}
	
	public String getBillPresentationNumber() {
		return delegate.getBillPresentationNumber();
	}
	
	public String toString() {
		return delegate.toString();
	}	

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	public Call getCallDetails() throws TelusAPIException {
		
        try {
        	if (delegate.getCallDetails() == null) {
        		delegate.setCallDetails(provider.getSubscriberManagerBean().retrieveCallDetails(ban, subscriberId, productType, billSeqNo,
        				getDate(), getSwitchId(), getProductType()));
        	}
        } catch (Throwable t) {
        	provider.getExceptionHandler().handleException(t);
        }
    	return delegate.getCallDetails();
	}
	
	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	public void adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException{
        
	    try {
        	provider.getSubscriberManagerBean().adjustCall(ban, subscriberId, productType, billSeqNo, getDate(), getSwitchId(),
        	        adjustmentAmount, adjustmentReasonCode, memoText, getProductType());        	
        } catch (Throwable t) {
        	provider.getExceptionHandler().handleException(t);
        }
	}

	public String getCallActionCode() {
		return delegate.getCallActionCode();
	}

	public String getCallTypeFeature() {
		return delegate.getCallTypeFeature();
	}

	public int getMessageType() {
		return delegate.getMessageType();
	}

	public Boolean isLteHspaHandover() {
		return delegate.isLteHspaHandover();
	}

}
