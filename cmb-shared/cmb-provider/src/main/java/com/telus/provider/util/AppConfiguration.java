package com.telus.provider.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Feature;
import com.telus.eas.framework.config.LdapManager;
import com.telus.eas.framework.config.ValueParser;
import com.telus.eas.framework.config.ValueParserFactory;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.provider.TMProvider;
import com.telus.provider.eligibility.interservice.impl.rules.InternationalServiceEligibilityEvaluationStrategy;

public class AppConfiguration {
	private static EligibilityCheckStrategy internationalServiceEligibilityEvaluationStrategy = null;
	private static Object mutexEligibilityCheckStrategy = new Object();
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final ValueParser stringValueParser = ValueParserFactory.getStringValueParser();

	private static final String defaultHspaEquipmentType = "P";
	private static final String[] default911FeeProvinces = new String[] {"NS", "SK", "PE", "NB", "PQ"};
	private static final int DEFAULT_CLIENT_AGENT_MONITORING_SLEEP_INTERVAL = 1000 * 60 * 30; //30 minutes by default
	private static final String DEFAULT_FORM_URL = "http://cctools/TMForms";
	private static final int DEFAULT_LIGHT_WEIGHT_SUB_MAX = 9000;
	private static final int DEFAULT_MAX_BAN_LIMIT = 1000;
	private static final int DEFAULT_MAX_SUB_LIMIT = 1000;
	private static final int DEFAULT_MAX_BAN_SIZE_THRESHOLD = 200;
	private static final int DEFAULT_RULE_REFRESH_START_TIME = 5;
	private static final int DEFAULT_RULE_REFRESH_INTERVAL_VALUE = -1;
	private static final int DEFAULT_RULE_REFRESH_SLEEP_INTERVAL_VALUE = 60;
	private static final String SMTP_FAX_HOST = "tor-fax-01.corp.ads"; // mailhost.clearnet.com, ex-tr_tabby
	private static final String SMTP_EMAIL_HOST = "mailhost.bctm.com"; // mailhost.bctm.com
	private static final String SMTP_FAX_ADDR_PATTERN = "{0}@tor-fax-01.corp.ads";


	private final static String[] defaultIncludeApnSocs = new String[] {"SRIMAPNO", "SMBAPNO"};
	private final static String[] defaultAddonApnSocs = new String[] {"RIMAPN,SRIMAPN","MBAPN,SMBAPN"};
	private static HashMap addOnAPNSocs = new HashMap();
	private static Date defaultCallDetailFeatureExpiryDate;
	private static HashMap _911CategoryCodesByBrandKeyMap = null;
	private static String[] default911CategoryCodesByBrandKeys ;
	private static HashSet _911FeeProvinces = null;
	private static Object mutex911FeeProvinces = new Object();
	private final static String POSTPAID = "O";
	private final static String PREPAID = "R";
	private static HashSet interBrandPortActivityReasonCodesSet;
	private static String[] interBrandPortActivityReasonCodesArray;
	private static HashMap interBrandPortActivityReasonCodesKeyMap;
	private static HashMap replyToEmailAddressKeysMap;
	private static HashMap defaultDealerCodeMap;
	private static HashMap defaultSalesRepCodeMap;

	static {
		setDefaultCallDetailFeatureExpiryDate();
		setDefault911CategoryCodesByBrandKeys();
	}
	
	private static void setDefaultCallDetailFeatureExpiryDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2004, 6, 1, 0, 0, 0);
		defaultCallDetailFeatureExpiryDate = calendar.getTime();
	}
	
	private static void setDefault911CategoryCodesByBrandKeys() {
		default911CategoryCodesByBrandKeys = new String[] {
				String.valueOf(Brand.BRAND_ID_TELUS)+","+Feature.CATEGORY_CODE_TELUS_911,
				String.valueOf(Brand.BRAND_ID_AMPD)+","+Feature.CATEGORY_CODE_TELUS_911,
				String.valueOf(Brand.BRAND_ID_KOODO)+","+Feature.CATEGORY_CODE_KOODO_911,
				String.valueOf(Brand.BRAND_ID_WALMART), Feature.CATEGORY_CODE_WALMART_911
		};
	}
	
	public static void main(String[] args) throws Exception {
		String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", url);
//		System.setProperty("useLocalCache", "false");
		
	}
	
	protected static LdapManager getLdapConfigurationManager() {
		return ProviderLdapConfigurationManager.getInstance();
	}
	
	protected static ValueParser getStringValueParser() {
		return stringValueParser;
	}
	
	public static boolean isSRPDSEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.SRPDS_ENABLED, false).booleanValue();
	}
		
	public static boolean isAsyncInvokeConfigurationManagerService() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.ASYNC_INVOKE_CONFIGURATION_MANAGER_SVC, true).booleanValue();
	}
	
	public static boolean isBlockDirectUpdate() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.BLOCK_DIRECT_UPDATE, false).booleanValue();
	}
	
	private static String getInternationalServiceEligibilityXml() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.INTERNATIONAL_SVC_ELIGIBILITY_XML);
	}
	
	public static EligibilityCheckStrategy getInternationalServiceEligibilityEvaluationStrategy() throws TelusAPIException {
		if (internationalServiceEligibilityEvaluationStrategy == null) {
			synchronized (mutexEligibilityCheckStrategy) {
				try {
					String configXml = getInternationalServiceEligibilityXml();
					internationalServiceEligibilityEvaluationStrategy = new InternationalServiceEligibilityEvaluationStrategy(configXml);

				} catch (Exception e) {
					throw new TelusAPIException("Unable to create international service eligibility evaluation strategy: " + e.getMessage());
				}
			}
		}
		return internationalServiceEligibilityEvaluationStrategy;
	}
	
	public static String getTMFormURL() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.TM_FORM_URL, DEFAULT_FORM_URL);
	}
	
	public static String getFaxServer() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.FAX_SERVER, SMTP_FAX_HOST);
	}
	
	public static String getMailServer() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.MAIL_SERVER, SMTP_EMAIL_HOST);
	}
	
	public static String getFaxAddrPattern() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.FAX_ADDR_PATTERN, SMTP_FAX_ADDR_PATTERN);
	}

	public static int getMaxBanLimit() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.MAX_BAN_LIMIT, DEFAULT_MAX_BAN_LIMIT).intValue();
	}
	
	public static int getMaxSubLimit() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.MAX_SUB_LIMIT, DEFAULT_MAX_SUB_LIMIT).intValue();
	}
	
	public static int getLightWeightSubMax() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.LIGHT_WEIGHT_SUB_MAX, DEFAULT_LIGHT_WEIGHT_SUB_MAX).intValue();
	}
	
	public static boolean getEnterpriseAddressRollback() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.ENTERPRISE_ADDRESS_ROLLBACK, false).booleanValue();
	}
	
	public static HashMap getAddOnAPNSocs() {
		if (addOnAPNSocs.size() == 0) {
			String[] ldapValues = getLdapConfigurationManager().getStringArrayValues(LdapKeys.ADDON_APN_SOCS, defaultAddonApnSocs);
			for (int i = 0; i < (ldapValues != null ? ldapValues.length : 0); i++) {
				String[] values = ldapValues[i].split(",");
				addOnAPNSocs.put(values[0], values[1]);
			}
		}
		return addOnAPNSocs;
	}
	
	public static String[] getIncludedApnSocs() {
		return getLdapConfigurationManager().getStringArrayValues(LdapKeys.INCLUDED_APN_SOCS, defaultIncludeApnSocs);
	}
	
	public static boolean isRefreshApn() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.REFRESH_APN, true).booleanValue();
	}
	
	public static Date getCallDetailFeatureExpiryDate() {
		return getLdapConfigurationManager().getDateValue(LdapKeys.CALLDETAIL_FEATURE_EXPIRY_DATE, defaultCallDetailFeatureExpiryDate);
	}
	

	public static String[] getVoiceMailFeatureCardServices() {
		return getLdapConfigurationManager().getStringArrayValues(LdapKeys.VOICEMAIL_FEATURE_CARD_SERVICES);
	}
	
	public static String getVoiceMailFeatureCardFollowUpGroup() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_GROUP);
	}
	
	public static String getVoiceMailFeatureCardFollowUpGroupBC() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_GROUP_BC);
	}
	
	public static String getVoiceMailFeatureCardFollowUpType() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_TYPE);
	}
	
	public static String getDefaultHSPAEquipmentType() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.DEFAULT_HSPA_EQUIPMENT_TYPE, defaultHspaEquipmentType);
	}
	
	public synchronized static HashMap get911CategoryCodesByBrandKeyMap() {
		 if (_911CategoryCodesByBrandKeyMap == null) {
			 HashMap hashMap = new HashMap();		 
			 // retrieve the key-value pairs as a String array from configuration
			 String[] stringArray = getLdapConfigurationManager().getStringArrayValues(LdapKeys.CATEGORY_CODE_BY_BRANDID_FOR911, default911CategoryCodesByBrandKeys);
			 hashMap = TMProvider.convertKeyValuePairsToHashMap(stringArray, ",");
			 _911CategoryCodesByBrandKeyMap = hashMap;
		 }

		 return _911CategoryCodesByBrandKeyMap;
	 }
	
	public static HashSet get911Provinces() {
		if (_911FeeProvinces == null) {
			synchronized (mutex911FeeProvinces) {
				HashSet provinces = new HashSet();
				provinces.addAll(Arrays.asList(getLdapConfigurationManager().getStringArrayValues(LdapKeys.FEEPROVINCES_FOR_911, default911FeeProvinces)));
				_911FeeProvinces = provinces;
			}
		}
		return _911FeeProvinces;
	}
	
	public static HashMap getInterBrandPortActivityReasonCodesKeyMap() {
		if (interBrandPortActivityReasonCodesKeyMap == null) {
			HashMap hashMap = new HashMap();
			try {
				String valueFromLdap = getLdapConfigurationManager().getStringValue(LdapKeys.INTERBRAND_PORT_ACTIVITY_REASON_CODES_KEYS);
				String[] stringArray = TMProvider.stringToArray(valueFromLdap.toUpperCase());
				hashMap = TMProvider.convertKeyValuePairsToHashMap(stringArray, ",");
			} catch (Throwable t) {
				// use these as default values
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "PO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "PR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_AMPD) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "PO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_AMPD) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "PR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_IDEN + POSTPAID, "MK");
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS) + Subscriber.PRODUCT_TYPE_IDEN + PREPAID, "MK");
				hashMap.put(String.valueOf(Brand.BRAND_ID_KOODO) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "KO");
				hashMap.put(String.valueOf(Brand.BRAND_ID_KOODO) + Subscriber.PRODUCT_TYPE_PCS + PREPAID, "KR");
				hashMap.put(String.valueOf(Brand.BRAND_ID_CLEARNET) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "CL");
				hashMap.put(String.valueOf(Brand.BRAND_ID_WALMART) + Subscriber.PRODUCT_TYPE_PCS + POSTPAID, "WM");
			}
			interBrandPortActivityReasonCodesKeyMap = hashMap;
		}

		return interBrandPortActivityReasonCodesKeyMap;
	}
	
	public static String[] getPrepaidCDMANGRRestrictions() {
		return getLdapConfigurationManager().getStringArrayValues(LdapKeys.PREPAID_CDMA_NG_RESTRICTIONS, EMPTY_STRING_ARRAY);
	}
	
	public static HashMap getReplyToEmailAddressKeyMap() {
		if (replyToEmailAddressKeysMap == null) {
			HashMap hashMap = new HashMap();
			try {
				String[] stringArray = getLdapConfigurationManager().getStringArrayValues(LdapKeys.REPLY_TO_EMAILADDRESS_KEYS);
				hashMap = TMProvider.convertKeyValuePairsToHashMap(stringArray, ",");

			} catch (Throwable t) {
				// use these as default values
				hashMap.put(String.valueOf(Brand.BRAND_ID_TELUS), "Telus<DoNotReply@telus.com>");
				hashMap.put(String.valueOf(Brand.BRAND_ID_AMPD), "Amp'd<DoNotReply@ampd.com>");
				hashMap.put(String.valueOf(Brand.BRAND_ID_KOODO), "Koodo Mobile<DoNotReply@koodo.com>");
				hashMap.put(String.valueOf(Brand.BRAND_ID_CLEARNET), "CLEARNET<DoNotReply@clearnet.com>");
				hashMap.put(String.valueOf(Brand.BRAND_ID_WALMART), "Walmart Wireless<DoNotReply@walmartwireless.com>");
			}
			replyToEmailAddressKeysMap = hashMap;

		}
		return replyToEmailAddressKeysMap;
	}
	
	public static HashSet getInterBrandPortActivityReasonCodes() {
		if (interBrandPortActivityReasonCodesSet == null) {
			HashSet set = new HashSet();

			try {
				String ldapValue = getLdapConfigurationManager().getStringValue(LdapKeys.INTERBRAND_PORT_ACTIVITY_REASON_CODES);
				String[] stringArray = TMProvider.stringToArray(ldapValue.toUpperCase());

				for (int i = 0; i < stringArray.length; i++) {
					set.add(stringArray[i]);
				}

			} catch (Throwable t) {
				// use these as default values
				set.add("POKO");
				set.add("PRKO");
				set.add("KOPO");
				set.add("KOPR");
				set.add("MKKO");
				set.add("KOMK");
				set.add("CLKO");
				set.add("KOCL");
				set.add("CLMK");
				set.add("MKCL");
				set.add("CLPO");
				set.add("POCL");
				set.add("CLPR");
				set.add("PRCL");
				set.add("CLWM");
				set.add("WMCL");
				set.add("WMKO");
				set.add("KOWM");
				set.add("WMMK");
				set.add("MKWM");
				set.add("WMPO");
				set.add("POWM");
				set.add("WMPR");
				set.add("PRWM");

			}
			interBrandPortActivityReasonCodesSet = set;
		}

		return interBrandPortActivityReasonCodesSet;
	}
	
	public static String[] getInterBrandPortActivityReasonCodesArray() {
		if (interBrandPortActivityReasonCodesArray == null) {
			HashSet reasonCodeSet = getInterBrandPortActivityReasonCodes();
			interBrandPortActivityReasonCodesArray = (String[]) reasonCodeSet.toArray(new String[reasonCodeSet.size()]);
		}
		
		return interBrandPortActivityReasonCodesArray;
	}

	public static List getAppNamesForAirtimeCard() {
		List emptyList = new ArrayList();
		
		return getLdapConfigurationManager().getValueAsList(LdapKeys.APPNAMES_FOR_AIRTIME_CARD, ",", emptyList, getStringValueParser());
	}
	
	public static HashMap getDefaultDealerCodeMap() {
		if (defaultDealerCodeMap == null) {
			String[] dealerCodeArray = getLdapConfigurationManager().getStringArrayValues(LdapKeys.DEFAULT_DEALER_CODE);
			defaultDealerCodeMap = TMProvider.convertKeyValuePairsToHashMap(dealerCodeArray, ",");
		}
		
		return defaultDealerCodeMap;
	}
	
	public static HashMap getDefaultSalesRepCodeMap() {
		if (defaultSalesRepCodeMap == null) {
			String[] salesRepCodeArray = getLdapConfigurationManager().getStringArrayValues(LdapKeys.DEFAULT_SALESREP_CODE);
			defaultSalesRepCodeMap = TMProvider.convertKeyValuePairsToHashMap(salesRepCodeArray, ",");
		}
		
		return defaultSalesRepCodeMap;
	}
	
	public static String getDefaultKoodoDealerCode() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.DEFAULT_KOODO_DEALER);
	}
	
	public static String getDefaultKoodoSalesRepCode() {
		return getLdapConfigurationManager().getStringValue(LdapKeys.DEFAULT_KOODO_SALESREP);
	}
	
	public static boolean isUseLocalCache() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.USE_LOCAL_CACHE, false).booleanValue();
	}
	
	public static boolean isUseLocalPPCache() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.USE_LOCAL_PP_CACHE, false).booleanValue();
	}

	public static int getRuleRefreshStartTime() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.RULE_REFRESH_START_TIME, DEFAULT_RULE_REFRESH_START_TIME).intValue();
	}
	
	public static int getRuleRefreshIntervalValue() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.RULE_REFRESH_INTERVAL_VALUE, DEFAULT_RULE_REFRESH_INTERVAL_VALUE).intValue();
	}
	
	public static int getRuleRefreshSleepIntervalValue() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.RULE_REFRESH_SLEEP_INTERVAL_VALUE, DEFAULT_RULE_REFRESH_SLEEP_INTERVAL_VALUE).intValue();
	}
	
	public static int getMaxBanSizeThreshold() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.MAX_BAN_SIZE_THRESHOLD, DEFAULT_MAX_BAN_SIZE_THRESHOLD).intValue();
	}
	
	public static int getClientAgentMonitoringSleepInterval() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.CLIENT_AGENT_MONITORING_SLEEP_INTERVAL, DEFAULT_CLIENT_AGENT_MONITORING_SLEEP_INTERVAL).intValue();
	}
	
	public static boolean isMethodMonitoringEnabledForClientAgentMonitoring() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.CLIENT_AGENT_MONITORING_IS_METHOD_MONITORING_ENABLED, true).booleanValue();
	}
	
	
	public static int getMaxDaysBeforeInvoice() {
		return getLdapConfigurationManager().getIntegerValue(LdapKeys.MAX_NUMBER_DAYS_BEFORE_NEXT_INVOICE,-1000).intValue();
	}
	
	public static boolean getDefaultDealerSalesRepInd() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.DEFAULT_DEALER_SALESREP_IND, true).booleanValue();
	}
	
	public static boolean isKoodoSimOnlySmartPayRollback() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.KOODO_SIMONLY_SMARTPAY_ROLLBACK, false).booleanValue();
	}
	
	
	public static boolean ischeckInternationalServiceEligibilityProviderRollback() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.CHECK_INTERNATIONAL_SERVICE_ELIGIBILITY_ROLLBACK, false).booleanValue();
	}
	public static boolean isSurepayRetirementRollback() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.SUREPAY_RETIREMENT_ROLLBACK, false).booleanValue();
	}
	public static boolean isPerformCommSuiteTNCPostTaskEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.ENABLE_COMM_SUITE_CTN_CHANGE_POST_TASK, true);
	}
	
	public static boolean isLMSAPIEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.ENABLE_LMS, false);
	}
	
	public static boolean isEsimSupportEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.ENABLE_ESIM_SUPPORT, true);
	}
	
	public static boolean isKafkaChangeEquipmentEventEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.KAFKA_CHANGE_EQUIPMENT_EVENT_ENABLED, false);
	}
	
	public static boolean isKafkaChangePhoneNumberEventEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.KAFKA_CHANGE_PHONENUMBER_EVENT_ENABLED, true);
	}
	
	public static boolean isKafkaChangeEventSmartspeakerEnabled() {
		return getLdapConfigurationManager().getBooleanValue(LdapKeys.KAFKA_CHANGE_EVENT_SMARTSPEAKER_ENABLED, true);
	}

}
