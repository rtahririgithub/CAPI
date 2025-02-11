package com.telus.cmb.account.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.util.BaseAppConfiguration;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.eas.framework.eligibility.interservice.impl.rules.InternationalServiceEligibilityEvaluationStrategy;

public class AppConfiguration extends BaseAppConfiguration {

	private static final int DEFAULT_SUBSCRIBER_LIMIT = 200;
	private static final int DEFAULT_DATA_SHARING_SUBSCRIBER_LIMIT = 200;
	private static final String SOA_SVC_BK_DATAUSAGE_SUMMARY_SERVICE_BK = "SummarizedDataServicesUsageService_v2_0_vs0_BK";
	private static EligibilityCheckStrategy internationalServiceEligibilityEvaluationStrategy = null;
	private static HashMap<String, String> marketingEligibleRulesMap;
	private static Object mutexEligibilityCheckStrategy = new Object();
	private static final List<String> familyTypeQueryExceptions = Arrays.asList(new String[] { "IU" });
	private static final int DEFAULT_SSPA_NEXT_STEP_COLLECTION_PATH_DAYS = 5;
	private static final int DEFAULT_SSPN_CLP_PAYMENT_DAYS = 6;
	private static final int DEFAULT_SSPN_NON_CLP_PAYMENT_DAYS = 10;

	private AppConfiguration() {
	}

	public static int getSubscriberQueryLimit() {
		return getConfigurationManager().getIntegerValue(LdapKeys.SUBSCRIBER_QUERY_LIMIT, DEFAULT_SUBSCRIBER_LIMIT).intValue();
	}

	public static int getDataSharingSubscriberLimit() {
		return getConfigurationManager().getIntegerValue(LdapKeys.DATA_SHARING_SUBSCRIBER_LIMIT, DEFAULT_DATA_SHARING_SUBSCRIBER_LIMIT).intValue();		
	}

	public static boolean useDataSharingInfoSqlPackage() {
		return getConfigurationManager().getBooleanValue(LdapKeys.USE_DATA_SHARING_INFO_SQL_PACKAGE, false);
	}
	
	public static List<String> getFamilyTypeQueryExceptions() {

		return getConfigurationManager().getStringValues(LdapKeys.FAMILY_TYPE_QUERY_EXCEPTION, ",", familyTypeQueryExceptions);
	}

	public static String getDataUsageServiceBK() {
		return getConfigurationManager().getStringValue(LdapKeys.DSAL_SVC_BK, SOA_SVC_BK_DATAUSAGE_SUMMARY_SERVICE_BK);
	}

	public static List<String> getAppNamesForAirtimeCard() {
		List<String> emptyList = new ArrayList<String>();

		return getConfigurationManager().getStringValues(LdapKeys.APPNAMES_FOR_AIRTIME_CARD, ",", emptyList);
	}

	public static boolean isBlockDirectUpdate() {
		return getConfigurationManager().getBooleanValue(LdapKeys.BLOCK_DIRECT_UPDATE, false);
	}

	public static boolean isAsyncPublishEnterpriseDataEnabled() {
		return !getConfigurationManager().getBooleanValue(LdapKeys.DISABLE_ASYNC_PUBLISH_ENTERPRISE_DATA, true);
	}
	
	public static boolean isAsyncUpdateBillingAccountEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ENABLE_ASYNC_UPDATE_BILLING_ACCOUNT, false);
	}

	public static boolean isAsyncInsertBillingAccountEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ENABLE_ASYNC_INSERT_BILLING_ACCOUNT, false);
	}
	
	public static boolean isUnitOfOrderDisabledForEnterpriseData() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ENTERPRISE_DATA_DISABLE_UNIT_OF_ORDER, true);
	}

	public static String getCardPaymentSvcBK() {
		return getConfigurationManager().getStringValue(LdapKeys.CARDPAYMENT_SVC_BK);
	}

	public static boolean isBillingAccountDataSyncFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_BILLINGACCOUNT_DATASYNC, true);
	}

	public static boolean isMemoCreationFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_MEMO_CREATION, true);
	}

	public static boolean isCTNChangeSearchEnabledForHandsetReturn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.CTN_CHG_SEARCH_ENABLED_FOR_EQUIP_RETURN, true);
	}

	public static boolean useNewRetrieveAccountByBanMethod() {
		return getConfigurationManager().getBooleanValue(LdapKeys.USE_NEW_RETRIEVE_ACCOUNT_BY_BAN_METHOD, true);
	}

	public static boolean isRetrieveLwAccountByBanRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.LW_ACCOUNT_RETRIEVAL_METHOD_ROLLBACK, false);
	}
	
	private static String getInternationalServiceEligibilityXml() {
		return getConfigurationManager().getStringValue(LdapKeys.INTERNATIONAL_SVC_ELIGIBILITY_XML);
	}

	public static EligibilityCheckStrategy getInternationalServiceEligibilityEvaluationStrategy() throws ApplicationException {
		if (internationalServiceEligibilityEvaluationStrategy == null) {
			synchronized (mutexEligibilityCheckStrategy) {
				try {
					String configXml = getInternationalServiceEligibilityXml();
					internationalServiceEligibilityEvaluationStrategy = new InternationalServiceEligibilityEvaluationStrategy(configXml);
				} catch (Exception e) {
					throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "", "Unable to create international service eligibility evaluation strategy: " + e.getMessage());
				}
			}
		}
		return internationalServiceEligibilityEvaluationStrategy;
	}

	public static boolean isMarketingToNonPAP() {
		return getConfigurationManager().getBooleanValue(LdapKeys.MARKETING_TO_NON_PAP, true);
	}

	public static boolean isSurepayRetirementRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUREPAY_RETIREMENT_ROLLBACK, false);
	}

	public static boolean isWPSSpringRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WPS_SPRING_ROLLBACK, true);
	}

	public static boolean isSMSNotificationRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SMS_NOTIFICATION_ROLLBACK, false);
	}

	public static boolean isWRPRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_ROLLBACK, false);
	}

	public static boolean isWRPPh3Rollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_ROLLBACK, false);
	}
	
	public static HashMap<String, String> getMarketingEligibleRulesMap() {
		if (marketingEligibleRulesMap == null) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			List<String> stringArray = getConfigurationManager().getStringValues(LdapKeys.MARKETING_ELIGIBLE_RULES);
			hashMap = convertKeyValuePairsToHashMap(stringArray, ",");
			if (stringArray.isEmpty()) {
				hashMap.put("PAPMarketingEligibleAccountTypes", "IR");
				hashMap.put("PAPMarketingEligibleBanSegments", "TCSO");
				hashMap.put("PAPMarketingEligibleAccountBrands", "1");
				hashMap.put("portoutMarketingEligibleAccountTypes", "IR");
				hashMap.put("portoutMarketingEligibleBanSegments", "TCSO");
				hashMap.put("portoutMarketingEligibleAccountBrands", "1");
			}
			marketingEligibleRulesMap = hashMap;
		}
		return marketingEligibleRulesMap;
	}

	private static HashMap<String, String> convertKeyValuePairsToHashMap(Collection<String> values, String delimiters) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (String value : values) {
			String[] keyValue = value.split(",");
			hashMap.put(keyValue[0], keyValue[1]);
		}
		return hashMap;
	}

	public static double getConsumerPaperBillCharge() {
		String paperChargeString = getConfigurationManager().getStringValue(LdapKeys.CONSUMER_PAPER_BILL_CHARGE);
		double paperCharge = 2.00; // default value
		try {
			paperCharge = Double.parseDouble(paperChargeString);
		} catch (Exception exp) {
			// Do nothing, return the default value.
		}
		return paperCharge;
	}

	public static int getSSPANextStepCollectionPathDays() {
		return getConfigurationManager().getIntegerValue(LdapKeys.SSPA_NEXT_STEP_COLLECTION_PATH_DAYS, DEFAULT_SSPA_NEXT_STEP_COLLECTION_PATH_DAYS).intValue();
	}

	public static int getSSPNCLPPaymentDays() {
		return getConfigurationManager().getIntegerValue(LdapKeys.SSPN_CLP_PAYMENT_DAYS, DEFAULT_SSPN_CLP_PAYMENT_DAYS).intValue();
	}

	public static int getSSPNNonCLPPaymentDays() {
		return getConfigurationManager().getIntegerValue(LdapKeys.SSPN_NON_CLP_PAYMENT_DAYS, DEFAULT_SSPN_NON_CLP_PAYMENT_DAYS).intValue();
	}

	public static boolean isEaaEbillRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.EAA_EBILL_ROLLBACK, false);
	}
	
	public static boolean isWRPPh3GetInvoiceHistoryRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_INVOICE_HISTORY_ROLLBACK, true);
	}
	public static boolean isWRPPh3GetDepositHistoryRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_DEPOSIT_HISTORY_ROLLBACK, true);
	}
	public static boolean isWRPPh3GetBilledCreditRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_BILLED_CREDIT_ROLLBACK, true);
	}
	public static boolean isWRPPh3GetUnBilledCreditRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_UNBILLED_CREDIT_ROLLBACK, true);
	}
	
	public static boolean isEcnmsAccountCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ECNMS_ACCOUNT_CANCEL_ENABLED, false);
	}
	public static boolean isKafkaAccountCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_ACCOUNT_CANCEL_ENABLED, true);
	}
	
	public static boolean isEcnmsMultiSubscriberCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ECNMS_MULTI_SUBSCRIBER_CANCEL_ENABLED, false);
	}
	public static boolean isKafkaMultiSubscriberCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_MULTI_SUBSCRIBER_CANCEL_ENABLED, true);
	}
}