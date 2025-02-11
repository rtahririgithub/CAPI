/*
 * Created on 11-Nov-2008
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.api.reference;

import com.telus.api.TelusAPIException;

/**
 * @author x119734
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface PrepaidRechargeDenomination extends Reference {
	
	double getAmount();
	int getRateId();
	String getRechargeType();
	
	/**
	 * This method is intended to be used for one-time call.  
	 * When multiple calls are made to this method within for a given transaction, better performance
	 * can be attained through the  retainByPrivilege method on ReferenceDataManager interface
	 */
	boolean containsPrivilege(String businessRole) throws TelusAPIException;


}
