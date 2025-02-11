package com.telus.cmb.common.dao.provisioning;

import java.util.List;
import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface VOIPLicensePurchaseServiceDao {
	public TestPointResultInfo test();
	public void removeLicenses(int ban, String subscriptionId,List<String> switchCodes) throws ApplicationException;
	public void addLicenses(int ban, String subscriptionId,List<String> switchCodes) throws ApplicationException;
}