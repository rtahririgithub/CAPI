/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * Title:        TMFutureStatusChangeRequest<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class TMFutureStatusChangeRequest extends BaseProvider implements FutureStatusChangeRequest {

	/**
	 * @link aggregation
	 */
	private final FutureStatusChangeRequestInfo delegate;
	
	public TMFutureStatusChangeRequest(TMProvider provider, FutureStatusChangeRequestInfo delegate) {
		super(provider);
		this.delegate = delegate;
	   
	}
	
	public FutureStatusChangeRequestInfo getDelegate() {
		return delegate;
	}
	
	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
	public String getActivityCode() {
		return delegate.getActivityCode();
	}	

	public void setActivityCode(String activityCode) {
		delegate.setActivityCode(activityCode);
	}
	
	public String getActivityReasonCode() {
		return delegate.getActivityReasonCode();
	}

	public void setActivityReasonCode(String activityReasonCode) {
		delegate.setActivityReasonCode(activityReasonCode);
	}
	
	public Date getCreateDate() {
		return delegate.getCreateDate();
	}

	public void setCreateDate(Date createDate) {
		delegate.setCreateDate(createDate);
	}
	
	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public void setEffectiveDate(Date effectiveDate) {
		delegate.setEffectiveDate(effectiveDate);
	}
	
	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}

	public void setPhoneNumber(String phoneNumber) {
		delegate.setPhoneNumber(phoneNumber);
	}
	
	public String getProductType() {
		return delegate.getProductType();
	}

	public void setProductType(String productType) {
		delegate.setProductType(productType);
	}
	
	public long getSequenceNumber() {
		return delegate.getSequenceNumber();
	}

	public void setSequenceNumber(long sequenceNumber) {
		delegate.setSequenceNumber(sequenceNumber);
	}
	
	public String getSubscriberId() {
		return delegate.getSubscriberId();
	}

	public void setSubscriberId(String subscriberId) {
		delegate.setSubscriberId(subscriberId);
	}
	
	public String toString() {
	    return delegate.toString();
	}
	
	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public void save() throws TelusAPIException {
		
		try {
			delegate.setUpdateFlag("U");
			provider.getAccountLifecycleManager().updateFutureStatusChangeRequest(delegate.getBan(), delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		
		} catch (Throwable t){
			provider.getExceptionHandler().handleException(t);
		}
	}
	
	public void delete() throws TelusAPIException {
		
		try {
			delegate.setUpdateFlag("D");
			provider.getAccountLifecycleManager().updateFutureStatusChangeRequest(delegate.getBan(), delegate, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		} catch (Throwable t){
			provider.getExceptionHandler().handleException(t);
		}
	}

}
