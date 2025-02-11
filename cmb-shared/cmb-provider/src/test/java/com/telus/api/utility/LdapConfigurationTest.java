package com.telus.api.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.telus.eas.framework.config.LdapManager;
import com.telus.provider.util.LdapKeys;
import com.telus.provider.util.ProviderLdapConfigurationManager;

public class LdapConfigurationTest extends TestCase {
	private LdapManager ldapManager;

	static {
		setup();
	}
	
	public static void setup() {
		String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", url);
		System.setProperty("cacheEvictionRate", "1");
		System.setProperty("dataEntryTimeToLive", "2");
		System.setProperty("dataEntryIdleTime", "3");
	}
	
	private void init() {
		ldapManager = ProviderLdapConfigurationManager.getInstance();
	}
	
	public void testLdapKeys() throws Exception {
		init ();
		Field[] fields = LdapKeys.class.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (field.getType().equals(String.class)) {
				int modifiers = field.getModifiers();
				if ((modifiers & (Modifier.FINAL + Modifier.STATIC)) > 0) {
					String key = (String) field.get(null);
					System.out.println("Value for key ["+key+"]="+ldapManager.getStringValue(key));
				}
			}
		}
		String testKey = "koodoSimOnlySmartPayRollback";
		String testKey2 = "TMFormURL";
		String testKey3 = "lightWeightSubMax";
		String testKey4 = "callDetailFeatureExpiryDate";
		Date effectiveDate = new Date();
		Calendar expiryDate = Calendar.getInstance();
		expiryDate.add(Calendar.MINUTE, 5);
		
		ldapManager.overrideValue(testKey, true, effectiveDate, expiryDate.getTime());
		ldapManager.overrideValue(testKey2, "http://localhost:7001", effectiveDate, expiryDate.getTime());
		ldapManager.overrideValue(testKey3, 100, effectiveDate, effectiveDate);
		ldapManager.overrideValue(testKey4, new Date(), effectiveDate, effectiveDate);
		System.out.println(testKey+": " +ldapManager.getBooleanValue(testKey));
		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
		System.out.println(testKey3+": " +ldapManager.getIntegerValue(testKey3));
		System.out.println(testKey4+": " +ldapManager.getDateValue(testKey4));
		ldapManager.refresh();
		System.out.println(testKey+": " +ldapManager.getBooleanValue(testKey));
		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
		System.out.println(testKey3+": " +ldapManager.getIntegerValue(testKey3));
		System.out.println(testKey4+": " +ldapManager.getDateValue(testKey4));
//		Thread.currentThread().sleep(4 * 1000L);
//		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
//		Thread.currentThread().sleep(4 * 1000L);
//		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
//		Thread.currentThread().sleep(4 * 1000L);
//		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
		ldapManager.clearOverride(testKey);
		ldapManager.clearOverride(testKey2);
		ldapManager.clearOverride(testKey3);
		ldapManager.clearOverride(testKey4);
		System.out.println("clear override");
		System.out.println(testKey+": " +ldapManager.getBooleanValue(testKey));
		System.out.println(testKey2+": " +ldapManager.getStringValue(testKey2));
		System.out.println(testKey3+": " +ldapManager.getIntegerValue(testKey3));
		System.out.println(testKey4+": " +ldapManager.getDateValue(testKey4));

	}
	
	public void listConfigurations() {
		init();
		
		String[] keys = ldapManager.getKeys();
			
//		for (String key : keys) {
//			if ("familyTypeQueryException".equals(key)) {
//				List<String> valueList = ldapManager.getValueAsList(LdapKeys.FAMILY_TYPE_QUERY_EXCEPTION, ",", null, new ValueParser() {
//					
//					@Override
//					public Object parse(Object value) {
//						return String.valueOf(value);
//					}
//				});
//				System.out.println(key + ":");
//				for (String value : valueList) {
//					System.out.println(value);
//				}
//			}else if ("appNamesForAirtimeCard".equals(key)) {
//				List<String> valueList = ldapManager.getValueAsList(key, ",", null, new ValueParser() {
//					
//					@Override
//					public Object parse(Object value) {
//						return String.valueOf(value);
//					}
//				});
//				System.out.println(key + ":");
//				for (String value : valueList) {
//					System.out.println(value);
//				}
//			} else {
//				System.out.println(key + ":" + ldapManager.getStringValue(key));
//			}
//		}
	}
	
	public void listConfigurationsClassic() {
		System.setProperty("cmbLdapRollback", "true");
		listConfigurations();
	}
	
	
	public void testLoad() throws Exception {
		int loops = 5000;
		final String key = "disableAsyncPublish_enterpriseData";
		
		for (int i = 0 ; i < loops; i++) {
			final int threadIndex = i;
			Thread t = new Thread() {

				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				public void run() {
					System.out.println("Thread " + threadIndex + ": " + ProviderLdapConfigurationManager.getInstance().getBooleanValue(key));
				}
				
			};
			
			t.start();
		}
		
		Thread.sleep(100L * 1000L);
	}
}
