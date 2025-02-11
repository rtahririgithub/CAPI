package com.telus.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

public class JMeterBaseTest extends TestCase{
	private static final String ALT_PROPERTIES_FILEPATH = "..\\test.properties";
	
	public void setUp(){
		String propertiesFilePath = System.getProperty("propertiesFile");
		File file=null;
		InputStream in=null;
		
		if (propertiesFilePath != null)
			file = new File(propertiesFilePath);
		
		if (file == null) {
			in = this.getClass().getResourceAsStream(ALT_PROPERTIES_FILEPATH);
		}else {
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				in = this.getClass().getResourceAsStream(ALT_PROPERTIES_FILEPATH);
			}	
		}
		
//        System.out.println("setup called");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	
		assertNotNull(in);
		try {
			initSystemPropertiesFromInputStream(in);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}finally {
			in = null;
			file = null;			
		}
		
//		System.out.println("**** LDAP URL set to: " + System.getProperty("com.telusmobility.config.java.naming.provider.url"));
//		System.out.println("**** PROVIDER URL set to: " + System.getProperty("com.telus.provider.providerURL"));
//		System.out.println("**** MonitoringFacade URL set to: " + System.getProperty("cmb.services.MonitoringFacade.url"));
//		System.out.println("**** VALUE of pathname is :" + propertiesFilePath);
//		System.out.println("setup completed");
	}

    
	private void initSystemPropertiesFromInputStream(InputStream in) throws Throwable {

    	Properties properties = new Properties();
    	properties.load(in);
    	System.setProperty("com.telusmobility.config.java.naming.provider.url", properties.getProperty("LDAP.URL", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"));
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wlqaeaseca:8682"));
        if (properties.getProperty("MONITORING.FACADE.URL") != null) {
        	System.setProperty("cmb.services.MonitoringFacade.url", properties.getProperty("MONITORING.FACADE.URL"));
        }
    }
}
