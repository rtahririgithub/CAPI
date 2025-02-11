package com.telus.api.reference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;

public class TestServiceGroupRelation extends TestCase {
	
        
    public TestServiceGroupRelation(String name) throws Throwable {
        super(name);
    }

    private ClientAPI api;
	
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
		
        System.out.println("setup called");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	
		//Added for the ant build script, its not the taking the ALT_PROPERTIES_FILEPATH and 'in' is null
		if(in==null){
			file = new File("./src/test/com/telus/api/test.properties");
			try {
				in= new FileInputStream(file);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} 
		}
	
		assertNotNull(in);
		try {
			initSystemPropertiesFromInputStream(in);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}finally {
			in = null;
			file = null;			
		}
		
		System.out.println("**** LDAP URL set to: " + System.getProperty("com.telusmobility.config.java.naming.provider.url"));
		System.out.println("**** PROVIDER URL set to: " + System.getProperty("com.telus.provider.providerURL"));
		System.out.println("**** HELPER URL set to: " + System.getProperty("cmb.services.ReferenceDataHelper.url"));
		System.out.println("**** FACADE URL set to: " + System.getProperty("cmb.services.ReferenceDataFacade.url"));
		
		try {			
			if (api == null) 
				api = ClientAPI.getInstance("18654", "apollo", "OLN");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("setup completed");
	}
    
    public void tearDown(){
        System.out.println("tearDown called");
        api = null;
        System.out.println("tearDown completed");
    }
   
    private void initSystemPropertiesFromInputStream(InputStream in) throws Throwable {

    	Properties properties = new Properties();
    	properties.load(in);
    	System.setProperty("com.telusmobility.config.java.naming.provider.url", properties.getProperty("LDAP.URL", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration"));
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wlqaeaseca:8682"));
//		System.setProperty("cmb.services.ReferenceDataHelper.url", properties.getProperty("REF.DATA.HELPER.URL", "t3://localhost:7001"));
//		System.setProperty("cmb.services.ReferenceDataFacade.url", properties.getProperty("REF.DATA.FACADE.URL", "t3://localhost:7001"));
        
    }
    
    
    public void testRetrieveServiceCodesByGroup_Valid() {
    	System.out.println("Start testRetrieveServiceCodesByGroup ");
    	try {
    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
    		
    		System.out.println("1. Get from DAO");
    		String[] socValues=refmgr.getServiceCodesByGroup("FPASFP ");
    		assertNotNull(socValues);
    		System.out.println("SOC length :"+socValues.length);
    		
    		System.out.println("2. Get from cache");
    		String[] socValues1=refmgr.getServiceCodesByGroup("FPASFP ");
    		assertNotNull(socValues1);
    		System.out.println("SOC length :"+socValues1.length);
    		
    		System.out.println("End testRetrieveServiceCodesByGroup ");
    	}catch (TelusAPIException e) {
    		e.printStackTrace();
		}
 
 }
    public void testRetrieveServiceCodesByGroup_nullCode() {
    	System.out.println("Start testRetrieveServiceCodesByGroup ");
    	try {
    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
    		String[] socValues=refmgr.getServiceCodesByGroup("");
    		assertNotNull(socValues);
    		System.out.println("SOC length (should be 0) :"+socValues.length);
    		System.out.println("End testRetrieveServiceCodesByGroup ");
    	}catch (TelusAPIException e) {
    		e.printStackTrace();
		}
 
 }
   
}

   


