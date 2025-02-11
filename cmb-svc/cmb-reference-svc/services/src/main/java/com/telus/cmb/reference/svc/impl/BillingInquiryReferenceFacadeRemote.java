package com.telus.cmb.reference.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.framework.exception.TelusException;

public interface BillingInquiryReferenceFacadeRemote  extends EJBObject {

	TaxSummaryInfo getTaxCalculationListByProvince(String provinceCode, double amount, 
			TaxExemptionInfo taxExemptionInfo) throws TelusException , RemoteException;

}
