package com.telus.cmb.common.dao.provisioning;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public interface WirelessProvisioningServiceDao {
	
	public void submitProvisioningOrder(ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException;
	public TestPointResultInfo test();
	
}