package com.telus.cmb.account.lifecyclefacade.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.lifecyclefacade.dao.WirelessCreditAssessmentProxyServiceDao;
import com.telus.cmb.account.mapping.CreditAuditMapper;
import com.telus.cmb.account.mapping.WirelessCreditAssessmentProxyServiceMapper;
import com.telus.cmb.common.aspects.ExpectedSLA;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClientV2;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.WirelessCreditAssessmentProxyServicePort;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.PerformCreditAssessment;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.PerformCreditAssessmentResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class WirelessCreditAssessmentProxyServiceDaoImpl extends SoaBaseSvcClientV2 implements WirelessCreditAssessmentProxyServiceDao {

	@Autowired
	private WirelessCreditAssessmentProxyServicePort port;
	
	@Override
	public TestPointResultInfo test() {
		TestPointResultInfo resultInfo = super.test();
		resultInfo.setTestPointName("WLSCreditAssessmentProxySvc v2.0 ping");
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
	public CreditAssessmentInfo overrideCreditWorthiness(final int ban, final String creditProgram, final String creditClass, final double creditLimit, final CreditCheckResultDepositInfo[] depositInfoArray,
			final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException {
		return performCreditAssessment(WirelessCreditAssessmentProxyServiceRequestFactory.createOverrideAssessmentRequest(ban, creditProgram, creditClass, creditLimit, depositInfoArray, auditInfo),
				auditHeader);
	}
	
	@Override
	@ExpectedSLA(value=2000)
	public CreditAssessmentInfo performCreditCheck(final AccountInfo accountInfo, final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException {
		return performCreditAssessment(WirelessCreditAssessmentProxyServiceRequestFactory.createCreditCheckAssessmentRequest(accountInfo, auditInfo), auditHeader);
	}

	@Override
	public CreditAssessmentInfo performManualCreditCheck(final AccountInfo accountInfo, final AuditInfo auditInfo, final AuditHeader auditHeader) throws ApplicationException {
		return performCreditAssessment(WirelessCreditAssessmentProxyServiceRequestFactory.createManualCreditCheckAssessmentRequest(accountInfo, auditInfo), auditHeader);
	}
	
	private CreditAssessmentInfo performCreditAssessment(final PerformCreditAssessment request, final AuditHeader auditHeader) throws ApplicationException {
		
		return execute(new SoaCallback<CreditAssessmentInfo>() {

			@Override
			public CreditAssessmentInfo doCallback() throws Exception {
				
				OriginatingUserType userHeader = CreditAuditMapper.AuditHeaderMapper().mapToSchema(auditHeader);
				PerformCreditAssessmentResponse response = getResponseHandler().handleErrorResponse(port.performCreditAssessment(request, userHeader), SystemCodes.CMB_CDA_DAO);
				
				return WirelessCreditAssessmentProxyServiceMapper.CreditAssessmentResultMapper().mapToDomain(response.getCreditAssessmentResult());
			}
		});
	}

}