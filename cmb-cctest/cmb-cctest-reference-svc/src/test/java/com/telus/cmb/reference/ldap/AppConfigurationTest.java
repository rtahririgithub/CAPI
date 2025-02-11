package com.telus.cmb.reference.ldap;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.reference.utilities.AppConfiguration;
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
	public void isEnterpriseDataSyncDisabled() {
		boolean isEnterpriseDataSyncDisabled = AppConfiguration.isEnterpriseDataSyncDisabled();	
		System.out.println("isEnterpriseDataSyncDisabled  : "+ isEnterpriseDataSyncDisabled);
	}

	@Test
	public void getEhcacheConfig() {
		String ehcacheConfig = AppConfiguration.getEhcacheConfig();	
		System.out.println("ehcacheConfig  : "+ ehcacheConfig);
	}

	@Test
	public void getCacheClearSchedule() {
		String cacheClearSchedule = AppConfiguration.getCacheClearSchedule();	
		System.out.println("cacheClearSchedule  : "+ cacheClearSchedule);
	}

	@Test
	public void getBillCycleProvinceRestrictions() {
		HashMap<String, String> billCycleProvinceRestrictions = AppConfiguration.getBillCycleProvinceRestrictions();
		for (Map.Entry<String, String> entry : billCycleProvinceRestrictions.entrySet()) {
		    System.out.println("billCycleProvinceRestrictions Are : Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	@Test
	public void getDefaultDealerCodeMap() {
		HashMap<String, String> defaultDealerCodeMap = AppConfiguration.getDefaultDealerCodeMap();
		for (Map.Entry<String, String> entry : defaultDealerCodeMap.entrySet()) {
		    System.out.println("defaultDealerCodeMap Are : Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	@Test
	public void  getDefaultSalesRepCodeMap() {
		HashMap<String, String> defaultSalesRepCodeMap = AppConfiguration.getDefaultSalesRepCodeMap();
		for (Map.Entry<String, String> entry : defaultSalesRepCodeMap.entrySet()) {
		    System.out.println("defaultSalesRepCodeMap Are : Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}

	@Test
	public void getDefaultKoodoDealerCode() {
		String defaultKoodoDealerCode = AppConfiguration.getDefaultKoodoDealerCode();	
		System.out.println("defaultKoodoDealerCode  : "+ defaultKoodoDealerCode);
	}

	@Test
	public void getDefaultKoodoSalesRepCode() {
		String defaultKoodoSalesRepCode = AppConfiguration.getDefaultKoodoSalesRepCode();	
		System.out.println("defaultKoodoSalesRepCode  : "+ defaultKoodoSalesRepCode);
	}

	@Test
	public void getInvoiceSuppressoinLevelUpdateLevels() {
		String invoiceSuppressoinLevelUpdateLevels = AppConfiguration.getInvoiceSuppressoinLevelUpdateLevels();	
		System.out.println("invoiceSuppressoinLevelUpdateLevels  : "+ invoiceSuppressoinLevelUpdateLevels);
	}
}
