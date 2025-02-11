/**
 * Title:        AccountTypeInfo<p>
 * Description:  The AccountTypeInfo contains the account type information.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.api.reference;

public interface AccountType extends Reference {

	public static final char BILLING_NAME_FORMAT_BUSINESS = 'D';
	public static final char BILLING_NAME_FORMAT_PERSONAL = 'P';

	char getAccountType() ;
	char getAccountSubType();
	String getNumberLocation();
	String getDefaultDealer();
	String getDefaultSalesCode() ;
	boolean isDuplicateBANCheck();
	String toString() ;
	int getMinimumSubscribersForFleet();
	char getBillingNameFormat();	
	boolean isCreditCheckRequired();
	int getBrandId();
}
