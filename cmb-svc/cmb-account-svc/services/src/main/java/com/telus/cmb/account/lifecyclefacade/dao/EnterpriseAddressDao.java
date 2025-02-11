package com.telus.cmb.account.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface EnterpriseAddressDao {
	AddressValidationResultInfo validateAddress(AddressInfo addressInfo) throws ApplicationException;
	TestPointResultInfo test();
}
