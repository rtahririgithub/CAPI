package com.telus.cmb.utility.ldap;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.framework.config.ConfigurationManager;
import com.telus.cmb.framework.config.ConfigurationManagerFactory;
import com.telus.cmb.utility.utilities.LdapKeys;

public class LdapConfigurationTest {
	private ConfigurationManager ldapManager;

	@Before
	public void setup() {
		String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", url);
//		System.setProperty("cmbLdapRollback", "true");
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://aaaaaa");
	}
	
	private void init() {
		ldapManager = ConfigurationManagerFactory.getInstance();
	}
	
	@Test
	public void testLdapKeys() throws Exception {
		init ();
		Field[] fields = LdapKeys.class.getFields();
		for (Field field : fields) {
			if (field.getType().equals(String.class)) {
				int modifiers = field.getModifiers();
				if ((modifiers & (Modifier.FINAL + Modifier.STATIC)) > 0) {
					String key = (String) field.get(null);
					System.out.println("Value for key ["+key+"]="+ldapManager.getStringValue(key));
				}
			}
		}
	}

/**	TFS version doesn't support getKeys()
	@Test
	public void listConfigurations() {
		init();
		
		String[] keys = ldapManager.getKeys();
			
		for (String key : keys) {
			if ("familyTypeQueryException".equals(key)) {
				String[] values = ldapManager.getStringArrayValues(key);
				System.out.println(key + ":");
				for (String value : values) {
					System.out.println(value);
				}
			}else {
				System.out.println(key + ":" + ldapManager.getStringValue(key));
			}
		}
	}
	
	@Test
	public void listConfigurationsClassic() {
		System.setProperty("cmbLdapRollback", "true");
		listConfigurations();
	}
*/
	
	@Test
	public void testCachedArray() {
		init ();
		List<String> values = ldapManager.getStringValues("BillCyclesProvinceRestrictions");
		for (String value : values) {
			System.out.println(value);
		}
		
		System.out.println("Cache:");
		List<String> values2 = ldapManager.getStringValues("BillCyclesProvinceRestrictions");
		for (String value : values2) {
			System.out.println(value);
		}
	}
	
	@Test
	public void testBoolean() throws Exception {
		init ();
		System.out.println(ldapManager.getBooleanValue("disableAsyncPublish_enterpriseData"));
		System.out.println(ldapManager.getBooleanValue("disableAsyncPublish_enterpriseData"));
		
//		Thread.sleep(1 * 65 * 1000);
//
//		System.out.println(ldapManager.getBooleanValue("disableAsyncPublish_enterpriseData"));
//		System.out.println(ldapManager.getBooleanValue("disableAsyncPublish_enterpriseData"));

	}
	
	@Test
	public void testLoad() throws Exception {
		int loops = 5000;
		final String key = "disableAsyncPublish_enterpriseData";
		
		for (int i = 0 ; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {

				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				@Override
				public void run() {
					System.out.println("Thread " + threadIndex + ": " + ConfigurationManagerFactory.getInstance().getBooleanValue(key));
				}
				
			};
			
			t.start();
		}
		
		Thread.sleep(100L * 1000L);
	}
}
