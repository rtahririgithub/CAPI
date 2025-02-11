package com.telus.cmb.utility.ldap;
import org.junit.Before;
import org.junit.Test;
import com.telus.cmb.utility.utilities.AppConfiguration;


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
	public void getSrpsEASUrl() {
		String srpsEASUrl = AppConfiguration.getSrpsEASUrl();
		System.out.println("srpsEASUrl"+srpsEASUrl);
		
	}
   @Test
	public void getAmdocsUrl() {
		String amdocsUrl = AppConfiguration.getAmdocsUrl();
		System.out.println("amdocsUrl"+amdocsUrl);
	}
}
