package com.telus.cmb.subscriber.lifecyclefacade.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.rest.PingResponse;
import com.telus.cmb.common.dao.soa.spring.rest.SoaBaseRestSvcClient;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.cmb.subscriber.domain.rest.CommunicationSuiteRepairRequest;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CommSuiteMgmtRestSvcDao;
import com.telus.eas.framework.info.TestPointResultInfo;

public class CommSuiteMgmtRestSvcDaoImpl extends SoaBaseRestSvcClient implements CommSuiteMgmtRestSvcDao {

	private static final String SESSION_ID = "sessionId";
	private static final Log LOGGER = LogFactory.getLog(CommSuiteMgmtRestSvcDaoImpl.class);

	@Override
	public TestPointResultInfo test() {

		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("Communication suite management internal rest service 1.0");

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
	public void repairCommunicationSuite(CommunicationSuiteRepairData repairData, String sessionId) throws ApplicationException {
		if (repairData != null) {
			Map<String, String> uriParams = new HashMap<String, String>();
			uriParams.put("ban", String.valueOf(repairData.getBan()));
			uriParams.put("subscriberId", repairData.getSubscriberId());

			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put(SESSION_ID, sessionId);

			CommunicationSuiteRepairRequest repairRequest = CommunicationSuiteRepairRequest.fromData(repairData);
			put("/commsuite/{ban}/{subscriberId}", uriParams, headers, repairRequest);
		}else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Received null repairData. sessionId=["+sessionId+"]");
			}
		}
		
	}

	@Override
	public String ping() throws ApplicationException {
		Map<String, String> uriParams = new HashMap<String, String>();
		PingResponse response = get("ping", uriParams, null, PingResponse.class);
		return "[ CommSuiteMgmtRestSvc status = " + response.getStatus().getStatusCode() + " , buildLabel = " + response.getBuildLabel() + " , buildDt = " + response.getBuildDate() + "]";
	}

}
