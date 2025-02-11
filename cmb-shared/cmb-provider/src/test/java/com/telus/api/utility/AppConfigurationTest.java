package com.telus.api.utility;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.provider.util.AppConfiguration;



public class AppConfigurationTest  extends TestCase {

	
	static {
		BaseTest.setupP();
		
	}
	
	public void testAll() throws Exception
	{
		testisSRPDSEnabled();
		testisAsyncInvokeConfigurationManagerService();
		testisBlockDirectUpdate();
		testgetTMFormURL();
		testgetFaxServer();
		testgetMailServer();
		testgetFaxAddrPattern();
		testgetMaxBanLimit();
		testgetMaxSubLimit();
		testgetInternationalServiceEligibilityEvaluationStrategy();
		testgetLightWeightSubMax();
		testgetIncludedApnSocs();
		testisRefreshApn();
		testgetCallDetailFeatureExpiryDate();
		testgetVoiceMailFeatureCardServices();
		testgetVoiceMailFeatureCardFollowUpGroup();
		testgetVoiceMailFeatureCardFollowUpGroupBC();
		testgetVoiceMailFeatureCardFollowUpType();
		testgetDefaultHSPAEquipmentType();
		get911CategoryCodesByBrandKeyMap();
		testget911Provinces();
		getInterBrandPortActivityReasonCodesKeyMap();
		testgetPrepaidCDMANGRRestrictions();
//		testgetPrepaidHSPANGRestrictions();
		testgetReplyToEmailAddressKeyMap();
		testgetReplyToEmailAddressKeyMap();
		testgetInterBrandPortActivityReasonCodes();
		testgetInterBrandPortActivityReasonCodesArray();
		testgetDefaultDealerCodeMap();
		testgetAppNamesForAirtimeCard() ;
		testgetDefaultDealerCodeMap();
		testgetDefaultSalesRepCodeMap();
		testgetDefaultKoodoDealerCode();
		testgetDefaultKoodoSalesRepCode();
		testisUseLocalCache();
		testisUseLocalPPCache();
		
		testgetRuleRefreshStartTime();
		testgetRuleRefreshIntervalValue();
		testgetRuleRefreshSleepIntervalValue();
		testgetMaxBanSizeThreshold();
		testgetClientAgentMonitoringSleepInterval();
		testisMethodMonitoringEnabledForClientAgentMonitoring();
		
	}
	
	public void testisSRPDSEnabled() {
		boolean isSRPDSEnabled = AppConfiguration.isSRPDSEnabled();
		System.out.println("isSRPDSEnabled flag value"+isSRPDSEnabled);
	}
	
	public void testisAsyncInvokeConfigurationManagerService() {
		boolean isAsyncInvokeConfigurationManagerService = AppConfiguration.isAsyncInvokeConfigurationManagerService();
		System.out.println("isAsyncInvokeConfigurationManagerService flag value"+isAsyncInvokeConfigurationManagerService);
	}
	
	public void testisBlockDirectUpdate() {
		boolean isBlockDirectUpdate = AppConfiguration.isBlockDirectUpdate();
		System.out.println("isBlockDirectUpdate flag value"+isBlockDirectUpdate);
	}
	
	
	public void testgetInternationalServiceEligibilityEvaluationStrategy() throws TelusAPIException {		
		EligibilityCheckStrategy eligibilityCheckStrategy = AppConfiguration.getInternationalServiceEligibilityEvaluationStrategy();
		System.out.println("EligibilityCheckStrategy  value"+eligibilityCheckStrategy.toString());
	}  
	
	public void testgetTMFormURL() {
		String tMFormURL = AppConfiguration.getTMFormURL();
		System.out.println("tMFormURL value"+tMFormURL);
	}
	
	public void testgetFaxServer() {
		String faxServer = AppConfiguration.getFaxServer();
		System.out.println("faxServer value"+faxServer);
	}
	
	public void testgetMailServer() {
		String mailServer = AppConfiguration.getMailServer();
		System.out.println("mailServer value"+mailServer);
	}
	
	public void testgetFaxAddrPattern() {
		String faxAddrPattern = AppConfiguration.getFaxAddrPattern();
		System.out.println("faxAddrPattern value"+faxAddrPattern);
	}

	public void testgetMaxBanLimit() {
		int maxBanLimit = AppConfiguration.getMaxBanLimit();
		System.out.println("maxBanLimit value"+maxBanLimit);
	}
	
	public void testgetMaxSubLimit() {
		int maxSubLimit = AppConfiguration.getMaxSubLimit();
		System.out.println("maxSubLimit value"+maxSubLimit);
	}
	
	public void testgetLightWeightSubMax() {
		int lightWeightSubMax = AppConfiguration.getLightWeightSubMax();
		System.out.println("lightWeightSubMax value"+lightWeightSubMax);
	}
	
	public void getEnterpriseAddressRollback() {
		boolean enterpriseAddressRollback = AppConfiguration.getEnterpriseAddressRollback();
		System.out.println("enterpriseAddressRollback value"+enterpriseAddressRollback);
	}
	
	public void getAddOnAPNSocs() {
		HashMap addOnAPNSocs = AppConfiguration.getAddOnAPNSocs();
		Iterator entries = addOnAPNSocs.entrySet().iterator();
		System.out.println("getAddOnAPNSocs values are");
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key =  (String) entry.getKey();
			String value =  (String) entry.getValue();
			System.out.println("Key = " + key + ", Value = " + value);
		}
	}
	

	public void testgetIncludedApnSocs() {
		String[] includedApnSocs  = AppConfiguration.getIncludedApnSocs();
		System.out.println("\n includedApnSocs are \n ");
		for (int i = 0; i < includedApnSocs.length; i++) {
			System.out.println(includedApnSocs[i]);	
		}
	}
	
	public void testisRefreshApn() {
		boolean isRefreshApn = AppConfiguration.isRefreshApn();
		System.out.println("\n isRefreshApn flag value"+isRefreshApn);
	}
	
	public void  testgetCallDetailFeatureExpiryDate() {
		Date date = AppConfiguration.getCallDetailFeatureExpiryDate();
		System.out.println("\n CallDetailFeatureExpiryDate value"+date);
	}
	
	
	
	public void testgetVoiceMailFeatureCardServices() {
		String[] voiceMailFeatureCardServices  = AppConfiguration.getVoiceMailFeatureCardServices();
		System.out.println("\n voiceMailFeatureCardServices are \n ");
		for (int i = 0; i < voiceMailFeatureCardServices.length; i++) {
			System.out.println(voiceMailFeatureCardServices[i]);	
		}
	}
	
	public void  testgetVoiceMailFeatureCardFollowUpGroup() {
		String voiceMailFeatureCardFollowUpGroup = AppConfiguration.getVoiceMailFeatureCardFollowUpGroup();
		System.out.println("\n voiceMailFeatureCardFollowUpGroup value"+voiceMailFeatureCardFollowUpGroup);
	}
	
	public void testgetVoiceMailFeatureCardFollowUpGroupBC() {
		String voiceMailFeatureCardFollowUpGroupBC = AppConfiguration.getVoiceMailFeatureCardFollowUpGroupBC();
		System.out.println("\n voiceMailFeatureCardFollowUpGroupBC value"+voiceMailFeatureCardFollowUpGroupBC);
	}
	
	public void testgetVoiceMailFeatureCardFollowUpType() {
		String voiceMailFeatureCardFollowUpType = AppConfiguration.getVoiceMailFeatureCardFollowUpType();
		System.out.println("\n voiceMailFeatureCardFollowUpType value"+voiceMailFeatureCardFollowUpType);
	}
	
	public void testgetDefaultHSPAEquipmentType() {
		String defaultHSPAEquipmentType = AppConfiguration.getDefaultHSPAEquipmentType();
		System.out.println("\n defaultHSPAEquipmentType value"+defaultHSPAEquipmentType);
	}
	
	public void get911CategoryCodesByBrandKeyMap() {
		HashMap _911CategoryCodesByBrandKeyMap = AppConfiguration.get911CategoryCodesByBrandKeyMap();
		Iterator entries = _911CategoryCodesByBrandKeyMap.entrySet().iterator();
		System.out.println(" \n 911CategoryCodesByBrandKeyMap values are");
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key =  (String) entry.getKey();
			String value =  (String) entry.getValue();
			System.out.println("Key = " + key + ", Value = " + value);
	          }
	}
	
	public void testget911Provinces() {
		
		HashSet _911Provincesset = AppConfiguration.get911Provinces();
		Iterator itr = _911Provincesset.iterator();

		System.out.println("\n  _911Provincesset values are : ");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	public void getInterBrandPortActivityReasonCodesKeyMap() {

		 HashMap interBrandPortActivityReasonCodesKeyMap = AppConfiguration.getInterBrandPortActivityReasonCodesKeyMap();		
		Iterator entries = interBrandPortActivityReasonCodesKeyMap.entrySet().iterator();			
		System.out.println(" \n  interBrandPortActivityReasonCodesKeyMap values are");		
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			System.out.println("Key = " + key + ", Value = " + value);
		}
	}
	
     public void testgetPrepaidCDMANGRRestrictions() {
		String[] prepaidCDMANGRRestrictions  = AppConfiguration.getPrepaidCDMANGRRestrictions();
		System.out.println("\n prepaidCDMANGRRestrictions are \n ");
		for (int i = 0; i < prepaidCDMANGRRestrictions.length; i++) {
			System.out.println(prepaidCDMANGRRestrictions[i]);	
		}
	}
/*	
	public void testgetPrepaidHSPANGRestrictions() {
		String[] getPrepaidHSPANGRestrictions  = AppConfiguration.getPrepaidHSPANGRestrictions();
		System.out.println("\n PrepaidHSPANGRestrictions are \n ");
		for (int i = 0; i < getPrepaidHSPANGRestrictions.length; i++) {
			System.out.println(getPrepaidHSPANGRestrictions[i]);	
		}
	}
	
*/	public void testgetReplyToEmailAddressKeyMap() {
		 HashMap replyToEmailAddressKeyMap = AppConfiguration.getReplyToEmailAddressKeyMap();		
			Iterator entries = replyToEmailAddressKeyMap.entrySet().iterator();			
			System.out.println(" \n  replyToEmailAddressKeyMap values are \n");		
			while (entries.hasNext()) {
				Map.Entry entry = (Map.Entry) entries.next();
				String key = (String) entry.getKey();
				String value = (String) entry.getValue();
				System.out.println("Key = " + key + ", Value = " + value);
			}
	}
	
	public void testgetInterBrandPortActivityReasonCodes() {
		HashSet interBrandPortActivityReasonCodes = AppConfiguration.getInterBrandPortActivityReasonCodes();
		Iterator itr = interBrandPortActivityReasonCodes.iterator();

		System.out.println("\n  interBrandPortActivityReasonCodes values are : ");
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}
	
	public void testgetInterBrandPortActivityReasonCodesArray() {
		String[] interBrandPortActivityReasonCodesArray  = AppConfiguration.getInterBrandPortActivityReasonCodesArray();
		System.out.println("\n interBrandPortActivityReasonCodesArray are \n ");
		for (int i = 0; i < interBrandPortActivityReasonCodesArray.length; i++) {
			System.out.println(interBrandPortActivityReasonCodesArray[i]);	
		}
	}

	public void testgetAppNamesForAirtimeCard() {
		List airtimeCardlist = AppConfiguration.getAppNamesForAirtimeCard();		
		 Iterator iterator = airtimeCardlist.iterator();
		 System.out.println(" \n  AppNamesForAirtimeCard values are \n");
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}

	}
	
	public void testgetDefaultDealerCodeMap() {
		HashMap defaultDealerCodeMap = AppConfiguration.getDefaultDealerCodeMap();		
		Iterator entries = defaultDealerCodeMap.entrySet().iterator();			
		System.out.println(" \n  defaultDealerCodeMap values are \n");		
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			System.out.println("Key = " + key + ", Value = " + value);
		}
	}
	
	public void  testgetDefaultSalesRepCodeMap() {
		HashMap defaultSalesRepCodeMap = AppConfiguration.getDefaultSalesRepCodeMap();		
		Iterator entries = defaultSalesRepCodeMap.entrySet().iterator();			
		System.out.println(" \n  defaultSalesRepCodeMap values are \n");		
		while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			System.out.println("Key = " + key + ", Value = " + value);
		}
	}
	
	public void testgetDefaultKoodoDealerCode() {
		String defaultKoodoDealerCode = AppConfiguration.getDefaultKoodoDealerCode();
		System.out.println("\n defaultKoodoDealerCode value"+defaultKoodoDealerCode);
	}
	
	public void testgetDefaultKoodoSalesRepCode() {
		String defaultKoodoSalesRepCode = AppConfiguration.getDefaultKoodoSalesRepCode();
		System.out.println("\n defaultKoodoSalesRepCode value"+defaultKoodoSalesRepCode);
	}
	
	public void testisUseLocalCache() {
		boolean isUseLocalCache = AppConfiguration.isUseLocalCache();
		System.out.println("\n isUseLocalCache flag value"+isUseLocalCache);
	}
	
	public void testisUseLocalPPCache() {
		boolean isUseLocalPPCache = AppConfiguration.isUseLocalPPCache();
		System.out.println("\n isUseLocalPPCache flag value"+isUseLocalPPCache);
	}

	
	
	
	
	public void testgetRuleRefreshStartTime() {
		int ruleRefreshStartTime = AppConfiguration.getRuleRefreshStartTime();
		System.out.println("\n ruleRefreshStartTime value"+ruleRefreshStartTime);
	}
	
	public void  testgetRuleRefreshIntervalValue() {
		int ruleRefreshIntervalValue = AppConfiguration.getRuleRefreshIntervalValue();
		System.out.println("\n ruleRefreshIntervalValue value"+ruleRefreshIntervalValue);
	}
	
	public void testgetRuleRefreshSleepIntervalValue() {
		int ruleRefreshSleepIntervalValue = AppConfiguration.getRuleRefreshSleepIntervalValue();
		System.out.println("\n ruleRefreshSleepIntervalValue value"+ruleRefreshSleepIntervalValue);
	}
	
	public void  testgetMaxBanSizeThreshold() {
		int maxBanSizeThreshold = AppConfiguration.getMaxBanSizeThreshold();
		System.out.println("\n maxBanSizeThreshold value"+maxBanSizeThreshold);
	}
	
	public void  testgetClientAgentMonitoringSleepInterval() {
		int clientAgentMonitoringSleepInterval = AppConfiguration.getClientAgentMonitoringSleepInterval();
		System.out.println("\n clientAgentMonitoringSleepInterval value"+clientAgentMonitoringSleepInterval);
	}
	
	public void  testisMethodMonitoringEnabledForClientAgentMonitoring() {
		boolean isMethodMonitoringEnabledForClientAgentMonitoring = AppConfiguration.isMethodMonitoringEnabledForClientAgentMonitoring();
		System.out.println("\n isMethodMonitoringEnabledForClientAgentMonitoring flag value"+isMethodMonitoringEnabledForClientAgentMonitoring);
	}
}
	

