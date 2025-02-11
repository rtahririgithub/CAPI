package com.telus.cmb.account.informationhelper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface WirelessCreditManagementServiceDao {
	
	CreditAssessmentInfo getCreditWorthiness(final int ban) throws ApplicationException;
	TestPointResultInfo test();
}