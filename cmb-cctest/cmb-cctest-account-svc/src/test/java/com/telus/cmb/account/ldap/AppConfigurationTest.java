package com.telus.cmb.account.ldap;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.account.utilities.AppConfiguration;


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
	public void getSubscriberQueryLimittest() {
		int subscriberlimit = AppConfiguration.getSubscriberQueryLimit();
		System.out.println("subscriberlimit" + subscriberlimit);
	}

	@Test
	public void getAppNamesForAirtimeCardtest() {
		System.out.println("getAppNamesForAirtimeCard start");
		List<String> emptyList = AppConfiguration.getAppNamesForAirtimeCard();
		for (int i = 0; i < emptyList.size(); i++) {
			System.out.println(emptyList.get(i));
		}
		System.out.println("getAppNamesForAirtimeCard End");
	}
	@Test
	public void getFamilyTypeQueryExceptionstest() {
		System.out.println("getFamilyTypeQueryExceptions start");
		List<String> familyTypeQueryExceptions = AppConfiguration.getFamilyTypeQueryExceptions();
		
		for (int i = 0; i < familyTypeQueryExceptions.size(); i++) {
			System.out.println(familyTypeQueryExceptions.get(i));
		}
		System.out.println("getFamilyTypeQueryExceptions End");
	}

	@Test
	public void isBlockDirectUpdatetest() {
		boolean BlockDirectupadteflag = AppConfiguration.isBlockDirectUpdate();
		System.out.println("isBlockDirectUpdate flag" + BlockDirectupadteflag);

	}

	@Test
	public void isAsyncPublishEnterpriseDataEnabledtest() {
		boolean isAsyncPublishEnterpriseDataEnabled = AppConfiguration.isAsyncPublishEnterpriseDataEnabled();	
		System.out.println("isAsyncPublishEnterpriseDataEnabled flag"+ isAsyncPublishEnterpriseDataEnabled);
				
	}

	@Test
	public void isUnitOfOrderDisabledForEnterpriseDatatest() {
		
		boolean isUnitOfOrderDisabledForEnterpriseData = AppConfiguration.isUnitOfOrderDisabledForEnterpriseData();	
		System.out.println("isUnitOfOrderDisabledForEnterpriseData flag"+ isUnitOfOrderDisabledForEnterpriseData);
				
	}

	@Test
	public void getDataUsageServiceBKtest() {
		String getDataUsageServiceBK = AppConfiguration.getDataUsageServiceBK();
		System.out.println("getDataUsageServiceBK Value : " + getDataUsageServiceBK);
	}
	
	@Test
	public void getCardPaymentSvcBKtest() {
		String cardPaymentSvcBK = AppConfiguration.getCardPaymentSvcBK();
		System.out.println("cardPaymentSvcBK Value : " + cardPaymentSvcBK);
	}
	
	@Test
	public void isBillingAccountDataSyncFalloutOn() {
		boolean isBillingAccountDataSyncFalloutOn = AppConfiguration.isBillingAccountDataSyncFalloutOn();	
		System.out.println("isBillingAccountDataSyncFalloutOn flag"+ isBillingAccountDataSyncFalloutOn);		
	}
	
	@Test
	public void isMemoCreationFalloutOn() {
		boolean isMemoCreationFalloutOn = AppConfiguration.isMemoCreationFalloutOn();	
		System.out.println("isMemoCreationFalloutOn flag"+ isMemoCreationFalloutOn);				
	}	

	@Test
	public void isCDRNotificationProject() {
		System.out.println("isCDRNotificationProjectRollback flag: "+ AppConfiguration.isCDRNotificationProjectRollback());				
	
		System.out.println("cdrNotificationUseTestingEmailAddress flag:"+ AppConfiguration.isCDRNotificationUseTestingEmailAddress());				

		String email = "TESTONLY-CustomerNotificationManagement@telus.com";
		System.out.println( email + " is in testing address: " + AppConfiguration.isInCDRNotificationTestingEmailList(email) );
		
		email = " TESTONLY-CustomerNotificationManagement@telus.Com ";
		System.out.println( email + " is in testing address: " + AppConfiguration.isInCDRNotificationTestingEmailList(email) );
		
	}	
}
