package com.telus.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.telus.api.ClientAPI;
import com.telus.api.reference.ApplicationSummary;

import junit.framework.TestCase;

public class TMJMeterBase extends TestCase {
	private static final String PROPERTIES_FILEPATH = "..\\TMJMeter.properties";
	private Properties properties = null;
	protected ClientAPI api;

	protected Properties getProperties() throws IOException {
		if (properties != null) 
			return properties;
		
		InputStream in = null;
		
		String propertiesFilePath = System.getProperty("TMConfigFile");
		if (propertiesFilePath != null) {
			File file = new File(propertiesFilePath);
			try {
				in = new FileInputStream(file);
//				System.out.println("Loading config from (System Property) " + propertiesFilePath);
			} catch (FileNotFoundException e) {
				in = null;
			}
		}  
		
		if (in == null) {
			in = this.getClass().getResourceAsStream(PROPERTIES_FILEPATH);
//			System.out.println("Loading config from (Local Folder) " + PROPERTIES_FILEPATH);
		}
		
		//Added for the testcase report generation, its not the taking the PROPERTIES_FILEPATH and 'in' is null
		if(in==null){
			File file = new File("./src/test/com/telus/provider/TMJMeter.properties");
			try {
				in= new FileInputStream(file);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
		}

		properties = new Properties();
		properties.load(in);
		return properties;
	}

	public void setUp() throws Exception {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", getProperties().getProperty("LDAP.URL"));
		api = ClientAPI.getInstance("18654", "apollo", ApplicationSummary.APP_SD);

//		System.out.println("**** LDAP URL set to: " + System.getProperty("com.telusmobility.config.java.naming.provider.url"));
//		System.out.println("setUp() Completed");
	}
	 
	public void tearDown() throws Exception {
		api.destroy();
		api = null;
//		System.out.println("tearDown() Completed");
	}
}
