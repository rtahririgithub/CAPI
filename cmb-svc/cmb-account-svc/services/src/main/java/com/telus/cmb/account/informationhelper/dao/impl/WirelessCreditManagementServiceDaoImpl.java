package com.telus.cmb.account.informationhelper.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.account.informationhelper.dao.WirelessCreditManagementServiceDao;
import com.telus.cmb.account.mapping.WirelessCreditManagementServiceMapper;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClientV2;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.WirelessCreditManagementServicePort;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditWorthiness;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditWorthinessResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class WirelessCreditManagementServiceDaoImpl extends SoaBaseSvcClientV2 implements WirelessCreditManagementServiceDao {

	@Autowired
	private WirelessCreditManagementServicePort port;

	@Override
	public TestPointResultInfo test() {
		TestPointResultInfo resultInfo = super.test();
		resultInfo.setTestPointName("WLSCreditManagmentSvc v2.0 ping");
		return resultInfo;
	}

	@Override
	public String ping() throws ApplicationException {
		
		return execute(new SoaCallback<String>() {
		
			@Override
			public String doCallback() throws Exception {
				PingResponse pingResponse = port.ping(new Ping());
				return pingResponse.getVersion();
			}
		});
	}

	@Override
	public CreditAssessmentInfo getCreditWorthiness(final int ban) throws ApplicationException {

		return execute(new SoaCallback<CreditAssessmentInfo>() {

			@Override
			public CreditAssessmentInfo doCallback() throws Exception {

				GetCreditWorthiness request = new GetCreditWorthiness();
				request.setBillingAccountNumber(ban);
				GetCreditWorthinessResponse response = getResponseHandler().handleErrorResponse(port.getCreditWorthiness(request), SystemCodes.CMB_CDA_DAO);	
				
				return WirelessCreditManagementServiceMapper.CreditWorthinessResponseMapper().mapToDomain(response);
			}			
		});
	}

}