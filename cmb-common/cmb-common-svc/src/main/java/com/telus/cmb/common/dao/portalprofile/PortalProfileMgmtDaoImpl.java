package com.telus.cmb.common.dao.portalprofile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.rest.SoaBaseRestSvcClient;
import com.telus.eas.framework.info.TestPointResultInfo;

public class PortalProfileMgmtDaoImpl extends SoaBaseRestSvcClient implements PortalProfileMgmtDao {
	
	@Override
	public PortalProfileResponse getPortalProfiles(int ban, PortalProfileFilterCriteria criteria) throws ApplicationException {
		
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("ban", String.valueOf(ban));		
		if (criteria == null) {
			criteria = new PortalProfileFilterCriteria();
		}
		
		return get("account/{ban}/user-persona-list", uriParams, criteria.convertToQueryMap(), null, PortalProfileResponse.class);
	}
	
	@Override
	public TestPointResultInfo test() {
		
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("Portal Profile Management Service v3.0 ping");

		try {
			resultInfo.setResultDetail(ping());
			resultInfo.setPass(true);
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}

		return resultInfo;
	}
	
	@Override
	public String ping() throws ApplicationException {
		Map<String, String> uriParams = new HashMap<String, String>();
		return get("ping", uriParams, null, PortalProfileResponse.class).toString();
	}

}
