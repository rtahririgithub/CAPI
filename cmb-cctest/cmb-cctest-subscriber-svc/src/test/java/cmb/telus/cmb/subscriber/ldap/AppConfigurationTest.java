package cmb.telus.cmb.subscriber.ldap;


import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.LdapKeys;


public class AppConfigurationTest {

	@Before
	public void setup() {
		String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", url);

	}
	
	@Test
	public void isAsyncPublishEnterpriseDataEnabledtest () {
		boolean isAsyncPublishEnterpriseDataEnabled = AppConfiguration.isAsyncPublishEnterpriseDataEnabled();
		System.out.println("isAsyncPublishEnterpriseDataEnabled :" +isAsyncPublishEnterpriseDataEnabled);
	}
	
	@Test
	public void isUnitOfOrderDisabledForEnterpriseDatatest() {
		boolean isUnitOfOrderDisabledForEnterpriseData = AppConfiguration.isUnitOfOrderDisabledForEnterpriseData();
		System.out.println("isUnitOfOrderDisabledForEnterpriseData :" +isUnitOfOrderDisabledForEnterpriseData);
	}
	
	@Test	
	public void getMinMdnServiceUrltest() {
		String minMdnServiceUrl = AppConfiguration.getMinMdnServiceUrl();
		System.out.println("minMdnServiceUrl :" +minMdnServiceUrl);		
	}
	
	@Test
	public void getProvisioningOrderBeanUrltest() {
		String provisioningOrderBeanUrl = AppConfiguration.getProvisioningOrderBeanUrl();
		System.out.println("rovisioningOrderBeanUrl :" +provisioningOrderBeanUrl);
	}
	
	@Test
	public void getCallDetailFeatureExpiryDatetest() {
		Date date = AppConfiguration.getCallDetailFeatureExpiryDate();
		System.out.println("CallDetailFeatureExpiryDate :"+date);
	}
	@Test
	public void getDefaultHSPAEquipmentTypetest() {
		String defaultHSPAEquipmentType = AppConfiguration.getDefaultHSPAEquipmentType();
		System.out.println("defaultHSPAEquipmentType :"+defaultHSPAEquipmentType);
	}
	
	@Test
	public void getVoiceMailFeatureCardServicestest() {
		String[] mailFeatureCardServices = AppConfiguration.getVoiceMailFeatureCardServices();
				
		for (int i = 0; i < mailFeatureCardServices.length; i++) {
			System.out.println("Values" + mailFeatureCardServices[i]);
		}
	}
	
	@Test
	public void getVoiceMailFeatureCardFollowUpGrouptest() {
		String voiceMailFeatureCardFollowUpGroup = AppConfiguration.getVoiceMailFeatureCardFollowUpGroup();
		System.out.println("voiceMailFeatureCardFollowUpGroup :"+voiceMailFeatureCardFollowUpGroup);
	}
	
	@Test
	public void getVoiceMailFeatureCardFollowUpGroupBCtest() {
		String voiceMailFeatureCardFollowUpGroupBC = AppConfiguration.getVoiceMailFeatureCardFollowUpGroupBC();
		System.out.println("voiceMailFeatureCardFollowUpGroupBC :"+voiceMailFeatureCardFollowUpGroupBC);	
	}
	
	@Test
	public void getVoiceMailFeatureCardFollowUpTypetest() {
		String voiceMailFeatureCardFollowUpType = AppConfiguration.getVoiceMailFeatureCardFollowUpType();
		System.out.println("voiceMailFeatureCardFollowUpType :"+voiceMailFeatureCardFollowUpType);
	}
	
	@Test
	public void  isAsyncPublishPostCommitTasksDisabledtest() {
		boolean isAsyncPublishPostCommitTasksDisabled = AppConfiguration.isRefreshApn();
		System.out.println("isRefisAsyncPublishPostCommitTasksDisabledreshApn :" +isAsyncPublishPostCommitTasksDisabled);
	}
	
	@Test
	public void isRefreshApntest() {	
		boolean isRefreshApn = AppConfiguration.isRefreshApn();
		System.out.println("isRefreshApn :" +isRefreshApn);
	}
	
	@Test
	public void get911Provincestest() {
		HashSet<String> provinces = AppConfiguration.get911Provinces();
		for (String province : provinces) {
			System.out.println("provinces values" + province);
		}
	}
	
	@Test
	public void isAsynchronousInvoke_SRPDS() {
		boolean isAsynchronousInvoke_SRPDS = AppConfiguration.isAsynchronousInvoke_SRPDS();
		System.out.println("isAsynchronousInvoke_SRPDS"+isAsynchronousInvoke_SRPDS);
	}
	
	@Test
	public void getAddOnAPNSocstest() {
		HashMap<String, String> categoryCodesByBrandKeyMap = AppConfiguration.getAddOnAPNSocs();
		for (Map.Entry<String, String> entry : categoryCodesByBrandKeyMap.entrySet()) {
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}
	
	@Test
	public void getIncludedApnSocstest() {
		String[]includedApnSocs = AppConfiguration.getIncludedApnSocs();
		for (int i = 0; i < includedApnSocs.length; i++) {
			System.out.println("includedApnSocs"+includedApnSocs[i]);
		}
		
	}
	
	@Test
	public void get911CategoryCodesByBrandKeyMaptest() {
		HashMap<String, String> categoryCodesByBrandKeyMap = AppConfiguration.get911CategoryCodesByBrandKeyMap();
		for (Map.Entry<String, String> entry : categoryCodesByBrandKeyMap.entrySet()) {
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}
	
	
	@Test
	public void  testisSRPDSLoggingFalloutOn() {
		boolean SRPDSLoggingFalloutOn = AppConfiguration.isSRPDSLoggingFalloutOn();
		System.out.println("SRPDSLoggingFalloutOn"+SRPDSLoggingFalloutOn);		
	}
	@Test
	public void testisConfigurationManagerFalloutOn() {
		boolean isConfigurationManagerFalloutOn = AppConfiguration.isConfigurationManagerFalloutOn();
		System.out.println("isConfigurationManagerFalloutOn"+isConfigurationManagerFalloutOn);		
	}
	@Test
	public void testisPostCommitFalloutOn() {
		boolean isPostCommitFalloutOn = AppConfiguration.isPostCommitFalloutOn();
		System.out.println("isPostCommitFalloutOn"+isPostCommitFalloutOn);			
	}
	@Test
	public void testisProductDataSyncFalloutOn() {
		boolean isProductDataSyncFalloutOn = AppConfiguration.isProductDataSyncFalloutOn();
		System.out.println("isProductDataSyncFalloutOn"+isProductDataSyncFalloutOn);			
	}
	@Test
	public void isKoodoSimOnlySmartPayRollback() {
		boolean isKoodoSimOnlySmartPayRollback = AppConfiguration.isKoodoSimOnlySmartPayRollback();
		System.out.println("isKoodoSimOnlySmartPayRollback flag value"+isKoodoSimOnlySmartPayRollback);			
	}
}
