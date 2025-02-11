/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.api.account;

import java.util.Date;
import com.telus.api.TelusAPIException;

/**
 * <CODE>FutureStatusChangeRequest</CODE>
 *
 */
public interface FutureStatusChangeRequest {

	long getSequenceNumber();
	
	String getPhoneNumber();
	
	String getProductType();
	
	String getActivityCode();
	
	String getActivityReasonCode();
	
	Date getCreateDate();
	
	Date getEffectiveDate();
	
	void setEffectiveDate(Date effectiveDate);
	
	/**
	 * Saves this FutureStatusChangeRequest to the datastore.
	 *
	 * <P>This method may involve a remote method call.
	 */
	void save() throws TelusAPIException;
	
	/**
	 * Deletes this FutureStatusChangeRequest from the datastore.
	 *
	 * <P>This method may involve a remote method call.
	 */
	void delete() throws TelusAPIException;
	
}
