package com.telus.cmb.account.informationhelper.dao;

import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

public interface ContactDao {

	BillingPropertyInfo retrieveBillingInformation(int billingAccountNumber);

	ContactPropertyInfo retrieveContactInformation(int billingAccountNumber);

}
