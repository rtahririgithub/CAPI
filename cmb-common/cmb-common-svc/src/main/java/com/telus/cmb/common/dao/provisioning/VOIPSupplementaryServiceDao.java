package com.telus.cmb.common.dao.provisioning;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface VOIPSupplementaryServiceDao {
	public String getPrimaryStarterSeatSubscriptionId(int ban) throws ApplicationException;
	public VOIPAccountInfo getVOIPAccountInfo(int ban) throws ApplicationException;
	public TestPointResultInfo test();
}