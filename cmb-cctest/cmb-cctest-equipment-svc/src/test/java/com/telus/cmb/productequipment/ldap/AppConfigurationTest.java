package com.telus.cmb.productequipment.ldap;


import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.telus.cmb.productequipment.utilities.AppConfiguration;


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
	public void getPricePlanCreditFamilyPlans() {
		List<String> emptyList  = AppConfiguration.getPricePlanCreditFamilyPlans();
		for (String familyplan : emptyList) {
		System.out.println("PricePlanCreditFamilyPlans are : "+familyplan);	
		}
	}
	
	@Test
	public void isUseAcmetest() {
		boolean isUseAcme = AppConfiguration.isUseAcme();
		System.out.println("isUseAcme  : " + isUseAcme);
	}
	
	@Test
	public void getEquipmentInfoServiceUrltest() {
		
		String equipmentInfoServiceUrl = AppConfiguration.getEquipmentInfoServiceUrl();	
		System.out.println("EquipmentInfoServiceUrl  : "+ equipmentInfoServiceUrl);			
	}
	
	@Test
	public void getEqupimentLifecycleManagementServiceUrltest() {
		String equpimentLifecycleManagementServiceUrl = AppConfiguration.getEqupimentLifecycleManagementServiceUrl();	
		System.out.println("equpimentLifecycleManagementServiceUrl  : "+ equpimentLifecycleManagementServiceUrl);
	}
	
	
	@Test
	public void getP3MSproductFacadeEjbUrltest() {
		String P3MSproductFacadeEjbUrl = AppConfiguration.getP3MSproductFacadeEjbUrl();	
		System.out.println("P3MSproductFacadeEjbUrl  : "+ P3MSproductFacadeEjbUrl);
	}
	
	@Test
	public void getNrtEligibilityManagerEJBUrltest() {
		String NrtEligibilityManagerEJBUrl = AppConfiguration.getNrtEligibilityManagerEJBUrl();	
		System.out.println("NrtEligibilityManagerEJBUrl  : "+ NrtEligibilityManagerEJBUrl);
	}
}
