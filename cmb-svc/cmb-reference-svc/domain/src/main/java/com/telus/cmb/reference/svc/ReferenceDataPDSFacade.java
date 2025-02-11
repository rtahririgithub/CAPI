package com.telus.cmb.reference.svc;

import com.telus.eas.framework.exception.TelusException;

public interface ReferenceDataPDSFacade {
	String getSubscriptionTypeByKBServiceType (String kbServiceType) throws TelusException;
	String getServiceInstanceStatusByKBSubscriberStatus (String kbSubscribreStatus) throws TelusException;
	String getBillingAccountStatusByKBAccountStatus (String kbAccountStatus) throws TelusException;
	String getPaymentMethodTypeByKBPaymentMethodType (String kbPaymentMethodType) throws TelusException;
	String getCreditCardTypeByKBCreditCardType (String kbCreditCardType) throws TelusException;
	String getBillCycleCodeByKBBillCycleCode (String kbBillCycleCode) throws TelusException;
	String getNameSuffixByKBNameSuffix (String kbNameSuffix) throws TelusException;
	String getSaluationCodeByKBSaluationCode (String kbSaluationCode) throws TelusException;
	String getEquipmentGroupTypeBySEMSEquipmentGroupType (String semsEquipmentGroupType) throws TelusException;
	String getProvinceCodeByKBProvinceCode (String kbProvinceCode) throws TelusException;
	String getCountryCodeByKBCountryCode(String kbCountryCode) throws TelusException;
	boolean isNotificationEligible(String transactionType, String originatingeApp,  int brandId, String accountType, String banSegment, String productType ) throws TelusException;

}
