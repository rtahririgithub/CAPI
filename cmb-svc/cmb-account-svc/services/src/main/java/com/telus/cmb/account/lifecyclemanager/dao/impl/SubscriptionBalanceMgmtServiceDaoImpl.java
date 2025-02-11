package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionBalanceMgmtServiceDao;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.cmb.common.prepaid.SubscriptionBalanceMgmtServiceClient;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Charge;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.ChargeResponse;
import com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.CreditResponse;
import com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.TransactionReference;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Quantity;


@Component
public class SubscriptionBalanceMgmtServiceDaoImpl implements SubscriptionBalanceMgmtServiceDao {
	
	private static final Logger LOGGER = Logger.getLogger(SubscriptionBalanceMgmtServiceDaoImpl.class);

	@Autowired
	private SubscriptionBalanceMgmtServiceClient sbmsWSClient;

	@Override
	public String adjustBalance(String phoneNumber, double amount, String reasonCode, String transactionId,String userId) throws ApplicationException {
		//adjustBalance -> SubscriptionBalanceMgmtService.charge & SubscriptionBalanceMgmtService.credit
		String referenceNumber = "";
		if (amount <0 ) {
			double chargeAmount = -amount;
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("Adjusting Balance (charge) with " + chargeAmount + " for " + phoneNumber);
			charge(phoneNumber, chargeAmount, reasonCode, transactionId,userId);
		} else {
			if (LOGGER.isInfoEnabled()) 
				LOGGER.info("Adjusting Balance (credit) with " + amount + " for " + phoneNumber);
			credit(phoneNumber, amount, reasonCode, transactionId,userId);
		}
		return referenceNumber;
	}
	
	@Override
	public String credit(CreditInfo pCreditInfo,String userId) throws ApplicationException {
		/*
		 * BAN					- N/A
		 * PhoneNumber			- credit/subscriptionID (Mandatory)
		 * Amount				- credit/amount (Mandatory).
		 * SOURCE_OTHER			- N/A
		 * UserId				- N/A
		 * comments				- N/A
		 * reasonCode			- credit/adjustmentReason  
		 * 
		 * Note:
		 * credit/balanceType: 		Need to be set to "primary".  (Mandatory)
		 * credit/referenceCode: 	Any reference to any previous transaction caused the adjustment to be done. (Optional)
		 * credit/amount/units: 	Always send "CAD" for now. TODO check if we have this information available. (Mandatory)
		 */
		
		String phoneNumber = pCreditInfo.getPhoneNumber();
		if (phoneNumber == null) {
			phoneNumber = pCreditInfo.getSubscriberId();
		}
		double amount = pCreditInfo.getAmount();
		String reasonCode = pCreditInfo.getReasonCode();

		return credit(phoneNumber, amount, reasonCode, null,userId);
		
	}
	
	@Override
	public String credit(String phoneNumber, double amount, String reasonCode, String transactionId, String userId) throws ApplicationException {
		String referenceNumber = "";
		
		//Log request
		logCreditChargeReq(phoneNumber, amount, reasonCode, "Credit");
		
		 com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Credit parameters = createCreditReqObj(phoneNumber, 
				amount, 
				PrepaidUtils.UNITS_CAD,
				reasonCode, 
				PrepaidUtils.PRIMARY_TYPE, 
				transactionId);
		
		CreditResponse response = sbmsWSClient.credit(parameters, PrepaidUtils.createOriginatingUserTypeWithAppInfoPopulated(userId));
		if (response != null)
			referenceNumber = response.getReferenceNumber();
		
		//Log response
		logCreditChargeRes(referenceNumber, phoneNumber, "Credit");
		
		return referenceNumber;
	}
	
	private void logCreditChargeReq(String phoneNumber, double amount, String reasonCode, String operation) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(operation);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | Amount: " + amount);
			sb.append(" | ReasonCode: " + reasonCode);
			LOGGER.info(sb.toString());
		}
	}
	
	private void logCreditChargeRes(String referenceNumber, String phoneNumber, String operation) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append(operation);
			sb.append(" | PhoneNumber: " + phoneNumber);
			sb.append(" | ReferenceNumber: " + referenceNumber);
			LOGGER.info(sb.toString());
		}
	}

	
	@Override
	public String charge(ChargeInfo pCreditInfo,String userId) throws ApplicationException {
		//Reference to credit(CreditInfo pCreditInfo) for mapping details.
		String phoneNumber = pCreditInfo.getSubscriberId();
		double amount = pCreditInfo.getAmount();
		String reasonCode = pCreditInfo.getReasonCode();
		
		return charge(phoneNumber, amount, reasonCode, null,userId);
	}
	
	@Override
	public String charge(String phoneNumber, double amount, String reasonCode, String transactionId,String userId) throws ApplicationException {
		String referenceNumber = "";
		
		//Log request
		logCreditChargeReq(phoneNumber, amount, reasonCode, "Charge");
		
		Charge parameters = createChargeReqObj(phoneNumber, 
				amount, 
				PrepaidUtils.UNITS_CAD,
				reasonCode, 
				PrepaidUtils.PRIMARY_TYPE,
				transactionId);
		
		ChargeResponse response = sbmsWSClient.charge(parameters, PrepaidUtils.createOriginatingUserTypeWithAppInfoPopulated(userId));
		if (response != null)
			referenceNumber = response.getReferenceNumber();
		
		//Log response
		logCreditChargeRes(referenceNumber, phoneNumber, "Charge");
		
		return referenceNumber;
	}
	
	
	@Override
	public TestPointResultInfo test() {
		return sbmsWSClient.test();
	}
	
	
	
	
	
	
	
	/*
	 * Helper Mapping methods 
	 */
	private  com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Credit createCreditReqObj(String phoneNumber, double amount, String amountUnits, String reasonCode, String balanceType, String transactionId) {
		 com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Credit result = new  com.telus.tmi.xmlschema.srv.cmo.receivablesmgmt.subscriptionbalancemgmtservicerequestresponse_v1.Credit();
		if (StringUtils.isNotBlank(phoneNumber))
			result.setSubscriptionID(phoneNumber);
		if (StringUtils.isNotBlank(balanceType))
			result.setBalanceType(balanceType);
		result.setCredit(createCreditObj(amount, amountUnits, reasonCode, transactionId));
		return result;
	}
	
	private com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Credit createCreditObj(double amount, 
			String amountUnits, 
			String reasonCode,
			String transactionId) {
		com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Credit result = 
				new com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Credit();
		if (StringUtils.isNotBlank(amountUnits))
			result.setAmount(createQuantityObj(amount, amountUnits));
		if (StringUtils.isNotBlank(reasonCode))
			result.setAdjustmentReason(reasonCode);
		if (StringUtils.isNotBlank(transactionId))
			result.setTransactionReference(createTransactionReferenceObj(transactionId));
		return result;
	}
	
	private TransactionReference createTransactionReferenceObj(String referenceCode) {
		TransactionReference result = createTransactionReferenceObj(referenceCode, null, null, null);
		return result;
	}
	
	private TransactionReference createTransactionReferenceObj(String referenceCode, String referenceId, Date referenceDateTime, String sourceSystemCode) {
		TransactionReference result = new TransactionReference();
		if (StringUtils.isNotBlank(referenceCode))
			result.setReferenceCode(referenceCode);
		if (StringUtils.isNotBlank(referenceId))
			result.setReferenceID(referenceId);
		if (referenceDateTime != null)
			result.setReferenceDateTime(referenceDateTime);
		if (StringUtils.isNotBlank(sourceSystemCode))
			result.setSourceSystemCode(sourceSystemCode);
		return result;
	}
	
	private com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Quantity createQuantityObj(double amount, String amountUnits) {
		Quantity result = new Quantity();
		result.setAmount(amount);
		if (StringUtils.isNotBlank(amountUnits))
			result.setUnits(amountUnits);
		return result;
	}

	private Charge createChargeReqObj(String phoneNumber, double amount, String amountUnits, String reasonCode, String balanceType, String transactionId) {
		Charge result = new Charge();
		if (StringUtils.isNotBlank(phoneNumber))
			result.setSubscriptionID(phoneNumber);
		if (StringUtils.isNotBlank(balanceType))
			result.setBalanceType(balanceType);
		result.setCharge(createChargeObj(amount, amountUnits, reasonCode, transactionId));
		return result;
	
	}
	private com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Charge createChargeObj(double amount, 
			String amountUnits, 
			String reasonCode,
			String transactionId) {
		com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Charge result = 
				new com.telus.tmi.xmlschema.xsd.customer.appliedcustomerbillingrate.subscriberratestypes_v1.Charge();
		if (StringUtils.isNotBlank(amountUnits)) {
			result.setAmount(createQuantityObj(amount, amountUnits));
		}
		if (StringUtils.isNotBlank(reasonCode)) {
			result.setAdjustmentReason(reasonCode);
		}
		if (StringUtils.isNotBlank(transactionId))
			result.setTransactionReference(createTransactionReferenceObj(transactionId));
		return result;
	}
}
