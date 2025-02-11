package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.telus.cmb.account.mapping.CreditAuditMapper;
import com.telus.cmb.account.mapping.WirelessCreditAssessmentProxyServiceMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.BaseCreditAssessmentRequestAbstract;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.ManualCreditCheckAssessmentRequest;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.NewAccountCreditCheckAssessmentRequest;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.OverrideCreditWorthinessRequest;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditassessmentproxysvcrequestresponse_v2.PerformCreditAssessment;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.CreditWorthinessChange;
import com.telus.tmi.xmlschema.xsd.customer.customer.wirelesscredittypes_v2.ProductDeposit;

public class WirelessCreditAssessmentProxyServiceRequestFactory {
	
	private static final String OVERRIDE_TYPE = "OVRD_ASSESSMENT";
	private static final String OVERRIDE_SYS_TYPE = "SYS_OVRD_ASSESSMENT";
	private static final String OVERRIDE_SUBTYPE = "CREDIT_RESULT_OVRD";	
	private static final String FULL_TYPE = "FULL_ASSESSMENT";
	
	private static final String FULL_CREDIT_CHECK_SUBTYPE = "CREDIT_CHECK";
	private static final String FULL_ADDON_SUBTYPE = "ADDON";
	private static final String FULL_MANUAL_SUBTYPE = "MANUAL_ASSESSMENT";
	private static final String FULL_CCUD_SUBTYPE = "MONTHLY_CCUD";

	public static PerformCreditAssessment createOverrideAssessmentRequest(int ban, String creditProgram, String creditClass, double creditLimit, CreditCheckResultDepositInfo[] depositInfoArray,
			AuditInfo auditInfo) {

		CreditWorthinessChange changes = new CreditWorthinessChange();
		changes.setCreditProgramName(creditProgram);
		changes.setCreditClassCd(creditClass);
		changes.setClpCreditLimitAmount(new BigDecimal(creditLimit));
		List<ProductDeposit> securityDepositList = new ArrayList<ProductDeposit>();
		if (depositInfoArray != null && ArrayUtils.isNotEmpty(depositInfoArray)) {
			for (CreditCheckResultDepositInfo info : depositInfoArray) {
				ProductDeposit deposit = new ProductDeposit();
				deposit.setProductCd(info.getProductType());
				deposit.setDepositAmt(new BigDecimal(info.getDeposit()));
				securityDepositList.add(deposit);
			}
		}
		changes.setSecurityDepositList(securityDepositList);

		OverrideCreditWorthinessRequest overrideCreditWorthinessRequest = createBaseCreditAssessmentRequest(new OverrideCreditWorthinessRequest(), ban, OVERRIDE_TYPE, OVERRIDE_SUBTYPE, auditInfo);
		overrideCreditWorthinessRequest.setCreditWorthinessChange(changes);

		PerformCreditAssessment request = new PerformCreditAssessment();
		request.setCreditAssessmentRequest(overrideCreditWorthinessRequest);

		return request;
	}
	
	public static PerformCreditAssessment createCreditCheckAssessmentRequest(AccountInfo accountInfo, AuditInfo auditInfo) {
		
		NewAccountCreditCheckAssessmentRequest newAccountCreditCheckAssessmentRequest = createBaseCreditAssessmentRequest(new NewAccountCreditCheckAssessmentRequest(), accountInfo.getBanId(), FULL_TYPE, FULL_CREDIT_CHECK_SUBTYPE, auditInfo);
		newAccountCreditCheckAssessmentRequest.setIncommingAccount(WirelessCreditAssessmentProxyServiceMapper.NewAccountMapper().mapToSchema(accountInfo));
		
		PerformCreditAssessment request = new PerformCreditAssessment();
		request.setCreditAssessmentRequest(newAccountCreditCheckAssessmentRequest);

		return request;
	}
	
	public static PerformCreditAssessment createManualCreditCheckAssessmentRequest(AccountInfo accountInfo, AuditInfo auditInfo) {

		ManualCreditCheckAssessmentRequest manualCreditCheckAssessmentRequest = createBaseCreditAssessmentRequest(new ManualCreditCheckAssessmentRequest(), accountInfo.getBanId(), FULL_TYPE, FULL_MANUAL_SUBTYPE, auditInfo);
		manualCreditCheckAssessmentRequest.setCreditProfileInfoChange(WirelessCreditAssessmentProxyServiceMapper.CreditProfileChangeMapper().mapToSchema(accountInfo));
		
		PerformCreditAssessment request = new PerformCreditAssessment();
		request.setCreditAssessmentRequest(manualCreditCheckAssessmentRequest);

		return request;
	}
	
	private static <T extends BaseCreditAssessmentRequestAbstract> T createBaseCreditAssessmentRequest(T request, int ban, String type, String subType, AuditInfo auditInfo) {

		request.setBillingAccountNumber(ban);
		request.setCreditAssessmentTypeCd(type);
		request.setCreditAssessmentSubTypeCd(subType);
		request.setCreditAuditInfo(CreditAuditMapper.CreditAuditInfoMapper().mapToSchema(auditInfo));

		return request;
	}

}