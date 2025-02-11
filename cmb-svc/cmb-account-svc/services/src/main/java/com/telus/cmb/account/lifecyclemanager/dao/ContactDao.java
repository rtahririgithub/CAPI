package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

public interface ContactDao {

	void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException;
	
	void updateContactInformation(int billingAccountNumber, ContactPropertyInfo contactPropertyInfo, String sessionId) throws ApplicationException;

}
