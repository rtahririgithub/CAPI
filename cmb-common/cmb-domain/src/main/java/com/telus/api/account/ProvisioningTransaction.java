/*
 * Created on 22-Jun-04
 *
 * Interface for a provisioning transaction
 */
package com.telus.api.account;

import java.util.*;

import com.telus.api.TelusAPIException;

/**
 * @author zhangji
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ProvisioningTransaction {
	String getTransactionNo();
	
	String getStatus();
	
	Date getEffectiveDate();
	
	String getTransactionType();
	
	String getProductType();
	
	String getErrorReason();
	
	String getUserID();
	
	/**
	 * Retrieves a list of ProvisioningTransactionDetail objects.
	 *
	 * <P>This method may involve a remote method call.
	 */
	ProvisioningTransactionDetail[] getDetails() throws TelusAPIException;
}
