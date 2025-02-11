package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.lifecyclefacade.dao.CreditProfileServiceDao;
import com.telus.cmb.account.mapping.CreditAuditMapper;
import com.telus.cmb.account.mapping.CreditProfileServiceMapper;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.CreditProfileServicePort30;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.HCDclpActivationOptionDetailsInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.FindAccountsByCustomerProfile;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.FindAccountsByCustomerProfileResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.GetCLPActivationOptionDetails;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.GetCLPActivationOptionDetailsResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformBusinessCreditCheck;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformBusinessCreditCheckResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformCreditCheck;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformCreditCheckResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformSubscriberEligibilityCheck;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.PerformSubscriberEligibilityCheckResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.RetrieveCreditEvaluationBusinessList;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.creditprofilesvcrequestresponse_v3.RetrieveCreditEvaluationBusinessListResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class CreditProfileServiceDaoImpl extends SoaBaseSvcClient implements CreditProfileServiceDao {
	
	@Autowired
	private CreditProfileServicePort30 port;

	public void setPortType(CreditProfileServicePort30 port) {
		this.port = port;
	}

	@Override
	public TestPointResultInfo test() {
		TestPointResultInfo resultInfo = super.test();
		resultInfo.setTestPointName("CreditProfileSvc 3.0 ping");
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
	public CreditCheckResultInfo checkCredit(final AccountInfo accountInfo, final BusinessCreditIdentityInfo selectedBusinessCreditIdentity, final AuditInfo auditInfo, final AuditHeader auditHeader,
			final boolean isCreditCheckForBusiness) throws ApplicationException {

		return execute(new SoaCallback<CreditCheckResultInfo>() {

			@Override
			public CreditCheckResultInfo doCallback() throws Exception {
				
				CreditCheckResultInfo creditCheckResultInfo;
				
				if (!isCreditCheckForBusiness) {
					// Call CreditProfileService for consumer account
					PerformCreditCheck performCreditCheck = new PerformCreditCheck();
					performCreditCheck.setAuditInfo(CreditAuditMapper.AuditInfoMapper().mapToSchema(auditInfo));
					performCreditCheck.setAccount(CreditProfileServiceMapper.AccountMapper().mapToSchema(accountInfo));
					OriginatingUserType userHeader = CreditAuditMapper.AuditHeaderMapper().mapToSchema(auditHeader);
					PerformCreditCheckResponse hcdResponse = port.performCreditCheck(performCreditCheck, userHeader);
					
					// Map attributes into the CreditCheckResultInfo domain object
					creditCheckResultInfo = CreditProfileServiceMapper.CreditCheckResponseMapper().mapToDomain(hcdResponse.getCreditCheckResult());
					creditCheckResultInfo.setCreditParamType("I");
					
				} else {
					// Call CreditProfileService for business account
					PerformBusinessCreditCheck performBusinessCreditCheck = new PerformBusinessCreditCheck();
					performBusinessCreditCheck.setAuditInfo(CreditAuditMapper.AuditInfoMapper().mapToSchema(auditInfo));
					performBusinessCreditCheck.setAccount(CreditProfileServiceMapper.AccountMapper().mapToSchema(accountInfo));
					performBusinessCreditCheck.setBusinessCreditIdentity(CreditProfileServiceMapper.BusinessCreditIdentityMapper().mapToSchema(selectedBusinessCreditIdentity));
					PerformBusinessCreditCheckResponse hcdBusinessResponse = port.performBusinessCreditCheck(performBusinessCreditCheck);
					
					// Map attributes into the CreditCheckResultInfo domain object
					creditCheckResultInfo = CreditProfileServiceMapper.CreditCheckResponseMapper().mapToDomain(hcdBusinessResponse.getCreditCheckResult());
					creditCheckResultInfo.setLastCreditCheckSelectedBusiness(selectedBusinessCreditIdentity);
					creditCheckResultInfo.setCreditParamType("B");
				}

				return creditCheckResultInfo;
			}
		});
	}

	@Override
	public CreditCheckResultInfo performSubscriberEligibilityCheck(final AccountInfo accountInfo, final int subscriberCount, final double thresholdAmount, final AuditInfo auditInfo)
			throws ApplicationException {

		return execute(new SoaCallback<CreditCheckResultInfo>() {

			@Override
			public CreditCheckResultInfo doCallback() throws Exception {

				PerformSubscriberEligibilityCheck performSubscriberEligibilityCheck = new PerformSubscriberEligibilityCheck();
				performSubscriberEligibilityCheck.setAuditInfo(CreditAuditMapper.AuditInfoMapper().mapToSchema(auditInfo));
				performSubscriberEligibilityCheck.setAccount(CreditProfileServiceMapper.AccountMapper().mapToSchema(accountInfo));
				performSubscriberEligibilityCheck.setNewSubscribersCount(subscriberCount);
				performSubscriberEligibilityCheck.setWaivedDepositAmount(thresholdAmount);
				PerformSubscriberEligibilityCheckResponse response = port.performSubscriberEligibilityCheck(performSubscriberEligibilityCheck);
				CreditCheckResultInfo creditCheckResultInfo = CreditProfileServiceMapper.EligibilityCheckResponseMapper().mapToDomain(response.getEligibilityResult());

				return creditCheckResultInfo;
			}
		});
	}

	@Override
	public HCDclpActivationOptionDetailsInfo getCLPActivationOptionsDetail(final AccountInfo accountInfo) throws ApplicationException {

		return execute(new SoaCallback<HCDclpActivationOptionDetailsInfo>() {

			@Override
			public HCDclpActivationOptionDetailsInfo doCallback() throws Exception {

				GetCLPActivationOptionDetails getCLPActivationOptionDetails = new GetCLPActivationOptionDetails();
				getCLPActivationOptionDetails.setAccount(CreditProfileServiceMapper.AccountMapper().mapToSchema(accountInfo));
				GetCLPActivationOptionDetailsResponse response = port.getCLPActivationOptionDetails(getCLPActivationOptionDetails);
				HCDclpActivationOptionDetailsInfo info = CreditProfileServiceMapper.CLPActivationOptionMapper().mapToDomain(response.getCLPActivationOptionDetails());

				return info;
			}
		});
	}

	@Override
	public List<BusinessCreditIdentityInfo> retrieveCreditEvaluationBusinessList(final PostpaidBusinessRegularAccountInfo accountInfo, final AuditInfo auditInfo) throws ApplicationException {

		return execute(new SoaCallback<List<BusinessCreditIdentityInfo>>() {

			@Override
			public List<BusinessCreditIdentityInfo> doCallback() throws Exception {

				RetrieveCreditEvaluationBusinessList request = new RetrieveCreditEvaluationBusinessList();
				request.setAuditInfo(CreditAuditMapper.AuditInfoMapper().mapToSchema(auditInfo));
				request.setCreditBusinessAccountInfo(CreditProfileServiceMapper.BusinessAccountInfoMapper().mapToSchema(accountInfo));
				request.setCreditBusinessCustomerInfo(CreditProfileServiceMapper.CreditBusinessCustomerInfoMapper().mapToSchema(accountInfo));
				RetrieveCreditEvaluationBusinessListResponse response = port.retrieveCreditEvaluationBusinessList(request);

				return CreditProfileServiceMapper.BusinessCreditIdentityMapper().mapToDomain(response.getBusinessCreditIdentityList());
			}
		});
	}
	
	@Override
	public List<MatchedAccountInfo> findAccountsByCustomerProfile(final AccountInfo accountInfo) throws ApplicationException {

		return execute(new SoaCallback<List<MatchedAccountInfo>>() {

			@Override
			public List<MatchedAccountInfo> doCallback() throws Exception {

				FindAccountsByCustomerProfile request = CreditProfileServiceMapper.FindAccountsByCustomerProfileMapper().mapToSchema(accountInfo);
				FindAccountsByCustomerProfileResponse response = port.findAccountsByCustomerProfile(request);
				
				return CreditProfileServiceMapper.MatchedAccountMapper().mapToDomain(response.getMatchedAccountList());
			}
		});
	}

}