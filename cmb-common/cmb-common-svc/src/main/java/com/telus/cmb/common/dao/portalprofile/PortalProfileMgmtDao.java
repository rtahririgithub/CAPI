package com.telus.cmb.common.dao.portalprofile;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface PortalProfileMgmtDao {

	public PortalProfileResponse getPortalProfiles(int ban, PortalProfileFilterCriteria criteria) throws ApplicationException;
	
	public TestPointResultInfo test();

}
