package com.telus.cmb.subscriber.utilities;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.telus.api.reference.Brand;
import com.telus.cmb.common.util.BaseAppConfiguration;

public class AppConfiguration extends BaseAppConfiguration {

	private static Date defaultCallDetailFeatureExpiryDate;
	private static HashMap<String, String> addOnAPNSocs = new HashMap<String, String>();
	private static HashMap<String, String> interBrandPortActivityReasonCodesKeyMap;
	private static HashSet<String> interBrandPortActivityReasonCodesSet;
	private static HashMap<String, String> marketingEligibleRulesMap;
	private static HashSet<Integer> supportedDataSharingBrands;

	private static final int DEFAULT_LIGHT_WEIGHT_SUB_MAX = 9000;
	private static final int DEFAULT_MAX_SUB_LIMIT = 1000;
	private static final String defaultHspaEquipmentType = "P";
	private static final List<String> defaultIncludeApnSocs = Arrays.asList(new String[] { "SRIMAPNO", "SMBAPNO" });
	private static final List<String> defaultAddonApnSocs = Arrays.asList(new String[] { "RIMAPN,SRIMAPN", "MBAPN,SMBAPN" });

	private static final List<Integer> defaultSharedSIMIncomingBrands = Arrays.asList(new Integer[] { Brand.BRAND_ID_KOODO });
	private static final List<Integer> defaultSharedSIMOutgoingBrands = Arrays.asList(new Integer[] { Brand.BRAND_ID_TELUS,Brand.BRAND_ID_KOODO,Brand.BRAND_ID_PC_MOBILE, Brand.BRAND_ID_PUBLIC_MOBILE });



	static {
		setDefaultCallDetailFeatureExpiryDate();
	}

	private static void setDefaultCallDetailFeatureExpiryDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2004, 6, 1, 0, 0, 0);
		defaultCallDetailFeatureExpiryDate = calendar.getTime();
	}

	public static boolean isAsyncPublishEnterpriseDataEnabled() {
		return !getConfigurationManager().getBooleanValue(LdapKeys.DISABLE_ASYNC_PUBLISH_ENTERPRISE_DATA, true);
	}

	public static boolean isUnitOfOrderDisabledForEnterpriseData() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ENTERPRISE_DATA_DISABLE_UNIT_OF_ORDER, true);
	}

	public static String getMinMdnServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.MINMDN_SVC_URL);
	}

	public static String getProvisioningOrderBeanUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.PROVISIONING_ORDER_BEAN_URL);
	}

	public static Date getCallDetailFeatureExpiryDate() {
		return getConfigurationManager().getDateValue(LdapKeys.CALLDETAIL_FEATURE_EXPIRY_DATE, defaultCallDetailFeatureExpiryDate);
	}

	public static String getDefaultHSPAEquipmentType() {
		return getConfigurationManager().getStringValue(LdapKeys.DEFAULT_HSPA_EQUIPMENT_TYPE, defaultHspaEquipmentType);
	}

	public static String[] getVoiceMailFeatureCardServices() {
		return getConfigurationManager().getStringValues(LdapKeys.VOICEMAIL_FEATURE_CARD_SERVICES).toArray(new String[0]);
	}

	public static String getVoiceMailFeatureCardFollowUpGroup() {
		return getConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_GROUP);
	}

	public static String getVoiceMailFeatureCardFollowUpGroupBC() {
		return getConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_GROUP_BC);
	}

	public static String getVoiceMailFeatureCardFollowUpType() {
		return getConfigurationManager().getStringValue(LdapKeys.VOICEMAIL_FEATURE_CARD_FOLLOWUP_TYPE);
	}

	public static boolean isAsyncPublishPostCommitTasksDisabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.DISABLE_ASYNC_PUBLISH_POSTCOMMIT_TASKS, true);
	}

	public static boolean isRefreshApn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.REFRESH_APN, true);
	}

	public static boolean isAsynchronousInvoke_SRPDS() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ACTIVITY_LOGGING_SERVICE_IS_ASYNCHRONOUS_INVOKE, true);
	}

	public static HashMap<String, String> getAddOnAPNSocs() {

		if (addOnAPNSocs.size() == 0) {
			for (String value : getConfigurationManager().getStringValues(LdapKeys.ADDON_APN_SOCS, defaultAddonApnSocs)) {
				String[] values = value.split(",");
				addOnAPNSocs.put(values[0], values[1]);
			}
		}

		return addOnAPNSocs;
	}

	public static String[] getIncludedApnSocs() {
		return getConfigurationManager().getStringValues(LdapKeys.INCLUDED_APN_SOCS, defaultIncludeApnSocs).toArray(new String[0]);
	}

	public static String getActivatePortInRequestServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.ACTIVATE_PORT_IN_REQUEST_SVC_URL);
	}

	public static String getCancelPortInRequestServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.CANCEL_PORT_IN_REQUEST_SVC_URL);
	}

	public static String getCreatePortInRequestServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.CREATE_PORT_IN_REQUEST_SVC_URL);
	}

	public static String getEligibilityCheckServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.ELIGIBILITY_CHECK_SVC_URL);
	}

	public static String getModifyPortInRequestServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.MODIFY_PORT_IN_REQUEST_SVC_URL);
	}

	public static String getSubmitPortInRequestServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.SUBMIT_PORT_IN_REQUEST_SVC_URL);
	}

	public static String getPortRequestRetrievalServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.PORT_REQUEST_RETRIEVAL_SVC_URL);
	}

	public static String getPortRequestStatusCheckServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.PORT_REQUEST_STATUS_CHECK_SVC_URL);
	}

	public static String getPreportRequestValidationServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.PREPORT_REQUEST_VALIDATION_SVC_URL);
	}

	public static String getReferenceServiceUrl() {
		return getConfigurationManager().getStringValue(LdapKeys.REFERENCE_SVC_URL);
	}

	public static boolean isSRPDSLoggingFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_SRPDS_LOGGING, true);
	}

	public static boolean isConfigurationManagerFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_CONFIGURATION_MANAGER, true);
	}

	public static boolean isPostCommitFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_POST_COMMIT, true);
	}

	public static boolean isProductDataSyncFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_PRODUCT_DATASYNC, true);
	}

	public static boolean isActivatePortInRequestServiceViaSOA() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ACTIVATE_PORT_IN_REQUEST_SVC_VIA_SOA, true);
	}

	public static boolean isMemoCreationFalloutOn() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FALLOUT_FLAG_MEMO_CREATION, true);
	}

	public static String getLocalServiceProviderIDTELUS() {
		return getConfigurationManager().getStringValue(LdapKeys.LOCALSERVICEPROVIDER_ID_TELUS);
	}

	public static boolean isHPAAsynchronousEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.HPA_ASYNCHRONOUS_ENABLED, true);
	}

	public static int getLightWeightSubMax() {
		return getConfigurationManager().getIntegerValue(LdapKeys.LIGHT_WEIGHT_SUB_MAX, DEFAULT_LIGHT_WEIGHT_SUB_MAX);
	}

	public static int getMaxSubLimit() {
		return getConfigurationManager().getIntegerValue(LdapKeys.MAX_SUB_LIMIT, DEFAULT_MAX_SUB_LIMIT);
	}

	public static HashMap<String, String> getInterBrandPortActivityReasonCodesKeyMap() {

		if (interBrandPortActivityReasonCodesKeyMap == null) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			try {
				String valueFromLdap = getConfigurationManager().getStringValue("interBrandPort_activityReasonCodes_keys");
				String tokens[] = valueFromLdap.toUpperCase().split(" ");
				String as[];
				int j = (as = tokens).length;
				for (int i = 0; i < j; i++) {
					String token = as[i];
					String keyValues[] = token.split(",");
					hashMap.put(keyValues[0], keyValues[1]);
				}
			} catch (Throwable _ex) {
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(1)))).append("C").append("O").toString(), "PO");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(1)))).append("C").append("R").toString(), "PR");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(2)))).append("C").append("O").toString(), "PO");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(2)))).append("C").append("R").toString(), "PR");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(1)))).append("I").append("O").toString(), "MK");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(1)))).append("I").append("R").toString(), "MK");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(3)))).append("C").append("O").toString(), "KO");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(3)))).append("C").append("R").toString(), "KR");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(4)))).append("C").append("O").toString(), "CL");
				hashMap.put((new StringBuilder(String.valueOf(String.valueOf(5)))).append("C").append("O").toString(), "WM");
			}
			interBrandPortActivityReasonCodesKeyMap = hashMap;
		}

		return interBrandPortActivityReasonCodesKeyMap;
	}

	public static HashSet<String> getInterBrandPortActivityReasonCodes() {

		if (interBrandPortActivityReasonCodesSet == null) {
			HashSet<String> set = new HashSet<String>();
			try {
				String ldapValue = getConfigurationManager().getStringValue("interBrandPort_activityReasonCodes");
				String stringArray[] = ldapValue.toUpperCase().split(" ");
				String as[];
				int j = (as = stringArray).length;
				for (int i = 0; i < j; i++) {
					String value = as[i];
					set.add(value);
				}
			} catch (Throwable _ex) {
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

	public static boolean isMarketingToCancelPortOutSub() {
		return getConfigurationManager().getBooleanValue(LdapKeys.MARKETING_TO_PORTOUT_SUB_CANCELLATION, true);
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

	private static HashMap<String, String> convertKeyValuePairsToHashMap(Collection<String> pairs, String delimiters) {

		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (String pair : pairs) {
			String[] keyValue = pair.split(",");
			hashMap.put(keyValue[0], keyValue[1]);
		}

		return hashMap;
	}

	public static String getBusinessConnectFalloutWirelessEmailAddress() {
		return getConfigurationManager().getStringValue(LdapKeys.BC_FALLOUT_WLS_EMAIL, "SBCXBusinessConnectWLS@telus.com");
	}

	public static String getBusinessConnectFalloutWirelineEmailAddress() {
		return getConfigurationManager().getStringValue(LdapKeys.BC_FALLOUT_WLN_EMAIL, "SBCXBusinessConnect@telus.com");
	}

	public static HashSet<Integer> getSupportedDataSharingBrands() {
		
		if (supportedDataSharingBrands == null) {
			HashSet<Integer> set = new HashSet<Integer>();
			try {
				String ldapValue = getConfigurationManager().getStringValue(LdapKeys.SUPPORTED_DATA_SHARING_BRANDS);
				String stringArray[] = ldapValue.split(",");
				String as[];
				int j = (as = stringArray).length;
				for (int i = 0; i < j; i++) {
					String value = as[i].trim();
					if (!value.equals("")) {
						set.add(Integer.valueOf(value));
					}
				}
			} catch (Throwable _ex) {
				set.add(1);
			}
			supportedDataSharingBrands = set;
		}
		
		return supportedDataSharingBrands;
	}

	public static boolean isFdDiscountRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.FUTURE_DATED_DISCOUNT_ROLLBACK, false).booleanValue();
	}

	public static boolean isSurepayRetirementRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUREPAY_RETIREMENT_ROLLBACK, false);
	}

	public static boolean isKoodoSimOnlySmartPayRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KOODO_SIMONLY_SMARTPAY_ROLLBACK, false);
	}

	public static boolean isWCCAuditProjectRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WCC_AUDIT_ROLLBACK, false);
	}

	public static boolean isTelusVolteEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.TELUS_VOLTE_SOC_ENABLED, true);
	}

	public static boolean isKoodoVolteEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KOODO_VOLTE_SOC_ENABLED, true);
	}

	public static boolean isAutoAddVolteEnabled(int brandId) {
		
		if (brandId == Brand.BRAND_ID_TELUS) {
			return isTelusVolteEnabled();
		} else if (brandId == Brand.BRAND_ID_KOODO) {
			return isKoodoVolteEnabled();
		}

		return false;
	}

	public static String getTelusVolteProductFeature() {
		return getConfigurationManager().getStringValue(LdapKeys.TELUS_VOLTE_PRODUCT_FEATURE, "VOLTE");
	}

	public static String getKoodoVolteProductFeature() {
		return getConfigurationManager().getStringValue(LdapKeys.KOODO_VOLTE_PRODUCT_FEATURE, "KVOLTE");
	}

	public static String getVolteProductFeatureByBrand(int brandId) {
		
		if (brandId == Brand.BRAND_ID_TELUS) {
			return getTelusVolteProductFeature();
		} else if (brandId == Brand.BRAND_ID_KOODO) {
			return getKoodoVolteProductFeature();
		}
		
		return null;
	}

	public static String getTelusVolteSoc() {
		return getConfigurationManager().getStringValue(LdapKeys.TELUS_VOLTE_SOC, "SVOLTE");
	}

	public static String getKoodoVolteSoc() {
		return getConfigurationManager().getStringValue(LdapKeys.KOODO_VOLTE_SOC, "3SVOLTE");
	}

	public static String getVolteSocByBrand(int brandId) {
		
		if (brandId == Brand.BRAND_ID_KOODO) {
			return getKoodoVolteSoc();
		} else if (brandId == Brand.BRAND_ID_TELUS) {
			return getTelusVolteSoc();
		}
		
		return null;
	}

	public static String getVolteSwitchCode() {
		return getConfigurationManager().getStringValue(LdapKeys.VOLTE_SWITCH_CODE, "VOLTE");
	}

	public static boolean iswrpSubscriberListBySubscriberIDRuleHintRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUBSCRIBER_LIST_BY_SUBSCRIBER_ID_RULE_HINT_ROLLBACK, false);
	}

	public static boolean iswrpSubscriberListByPhoneNumbersRuleHintRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUBSCRIBER_LIST_BY_PHONENUMBERS_RULE_HINT_ROLLBACK, false);
	}

	public static boolean iswrpSubscriberByBanAndSeatResourceNumberRuleHintRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.SUBSCRIBER_BY_BAN_AND_SEAT_RESOURCE_NUM_RULE_HINT_ROLLBACK, false);
	}

	public static boolean isWRPPh3Rollback() {
		// TODO: Just to make sure that we don't pre-maturely flip the switch, let's force it to true for now.
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_ROLLBACK, true);
	}

	public static boolean isWRPPh3GetVoiceUsageSummaryRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_VOICE_USAGE_SUMMARY_ROLLBACK, true);
	}
	
	public static boolean isWRPPh3GetResourceByBanAndOrPhoneNumRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_RESOURCE_BY_BAN_AND_OR_PHONE_NUM_ROLLBACK, false);
	}
	
	public static boolean isWRPPh3GetSubByBANandPhoneNumberRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_SUB_BY_AND_PHONE_NUMBER_ROLLBACK, false);
	}
	
	public static boolean isWRPPh3ExternalizedSQLRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_EXTERNALIZED_SQL_ROLLBACK, false);
	}

	public static boolean isWRPPh3GetIdentifierRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_IDENTIFIER_ROLLBACK, false);
	}
	
	public static boolean isEaaEbillRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.EAA_EBILL_ROLLBACK, false);
	}
	
	public static boolean isActivationPostTaskRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ACTIVATION_POST_TASK_ROLLBACK, false);
	}
	
	public static boolean isWRPPh3GetSubscriberListByBANRollBack() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_SUB_LIST_BY_BAN_ROLLBACK, true);
	}
	public static boolean isWRPPH3GetSecondarySerialNumbersRollBack() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_SECONDARY_SERIAL_NUMBER_ROLLBACK, true);
	}
	public static boolean isWRPPH3GetIdenResourcesRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_IDEN_RESOURCE_ROLLBACK, true);
	}
	public static boolean isWRPPH3GetHSPAResourcesRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_HSPA_RESOURCE_ROLLBACK, true);
	}
	public static boolean isWRPPH3GetMarketProvincesByPhoneNumbersRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.WRP_PH3_GET_MRKT_PROV_PHONE_NUMBER_ROLLBACK, true);
	}
	
	public static List<Integer> getSupportedSharedSIMIncomingBrands() {
		return getConfigurationManager().getValues(LdapKeys.SUPPORTED_SHARED_SIM_INCOMING_BRANDS, Integer.class ,",", defaultSharedSIMIncomingBrands);
	}
	
	public static List<Integer> getSupportedSharedSIMOutgoingBrands() {
		return getConfigurationManager().getValues(LdapKeys.SUPPORTED_SHARED_SIM_OUTGOING_BRANDS, Integer.class ,",", defaultSharedSIMOutgoingBrands);
	}
	
	public static boolean isEcnmsSubscriberCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ECNMS_SUBSCRIBER_CANCEL_ENABLED, false);
	}

	public static boolean isKafkaSubscriberCancelEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_SUBSCRIBER_CANCEL_ENABLED, true);
	}

	public static boolean isEcnmsSubscriberMoveEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.ECNMS_SUBSCRIBER_MOVE_ENABLED, false);
	}

	public static boolean isKafkaSubscriberMoveEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_SUBSCRIBER_MOVE_ENABLED, true);
	}

	public static boolean isKafkaChangeEquipmentEventEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_CHANGE_EQUIPMENT_EVENT_ENABLED, false);
	}
	
	public static boolean isKafkaChangePhoneNumberEventEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_CHANGE_PHONENUMBER_EVENT_ENABLED, true);
	}
	
	public static boolean isKafkaChangeEventSmartspeakerEnabled() {
		return getConfigurationManager().getBooleanValue(LdapKeys.KAFKA_EVENT_SMARTSPEAKER_ENABLED, true);
	}
	
	public static boolean isRedKneeSubMgmtWsRollback() {
		return getConfigurationManager().getBooleanValue(LdapKeys.REDKNEE_SUB_MGMT_WS_ROLLBACK, false);
	}

}