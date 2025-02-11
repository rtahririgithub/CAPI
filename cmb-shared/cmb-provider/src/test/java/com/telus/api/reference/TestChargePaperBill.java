package com.telus.api.reference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;

public class TestChargePaperBill extends TestCase {
	
        
    public TestChargePaperBill(String name) throws Throwable {
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
		//System.setProperty("cmb.services.ReferenceDataHelper.url", properties.getProperty("REF.DATA.HELPER.URL", "t3://um-generalutilities-dv103.tmi.telus.com:20152"));
		//System.setProperty("cmb.services.ReferenceDataFacade.url", properties.getProperty("REF.DATA.FACADE.URL", "t3://um-generalutilities-dv103.tmi.telus.com:20152"));
        
    }
    
    
//    public void testAll() throws Throwable{
//    	testRetrievePaperBillChargeType_null_input();
//    	testRetrievePaperBillChargeType_all_input();
//    	testRetrievePaperBillChargeType_few_input();
//    	testRetrievePaperBillChargeType_Empty_ChargeType();
//    	testRetrievePaperBillChargeType_GT1_ChargeType();
//    }
    
    
		// Sample input values...   	
    	// brand id= 1, 3, 0
		// provincecode= ON, SK , PQ, NB , NULL
		// accountType= I , B , C, '\u0000'
    	// accountSubType = R , P, O , Q, B , C, 
    	// segment= BMA , TCSO , TCSQ , OTHR, TBSO, 
    	
    	
   /* public void testRetrievePaperBillChargeType_null_input() {
    	
    	try {
    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
    		refmgr.getPaperBillChargeType(0, "", '\u0000', '\u0000', "", "");
    		
    	}catch (TelusAPIException e) {
    		assertTrue("All input values are empty", true);
    	}
    	
    }*/
    
	 public void testRetrievePaperBillChargeType_all_input() {
	    	
		 ChargeType chargeType = null;
		 long start = System.currentTimeMillis();
	    	try {
	    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
	    		chargeType=refmgr.getPaperBillChargeType(3, "SK", 'I', 'Q', "TCSO", "1");
	    		 System.out.println("\n\n *********** Time taken in testRetrievePaperBillChargeType_all_input :  [" + (System.currentTimeMillis() - start) + "] msec.\n\n ");
	    		assertNotNull(chargeType);
	    		System.out.println("ChargeType :"+chargeType.toString());
	    		
	    	}catch (TelusAPIException e) {
	    		System.out.println("Exception [" + e.getMessage()+ "] after [" + (System.currentTimeMillis() - start) + "] msec.");
	    		e.printStackTrace();
			}
	 
	 }
	 
	/* public void testRetrievePaperBillChargeType_few_input() {
	 	
		 ChargeType chargeType = null;
	    	try {
	    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
	    		chargeType=refmgr.getPaperBillChargeType(3, "SK", '\u0000', 'Q', "", "2");
	    		assertNotNull(chargeType);
	    	}catch (TelusAPIException e) {
				System.out.println(" Error at testRetrievePaperBillChargeType_few_input "+e);
			}
		 
		 
	 }
	 
	 public void testRetrievePaperBillChargeType_Empty_ChargeType() {
		 ChargeType chargeType = null;
	    	
	    	try {
	    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
	    		chargeType=refmgr.getPaperBillChargeType(1, "SK", 'I', 'Q', "", "3");
	    		assertNull(chargeType);
	    	}catch (TelusAPIException e) {
	    		System.out.println(" Error at testRetrievePaperBillChargeType_Empty_ChargeType "+e);
	    	}
	 }
	 
	 public void testRetrievePaperBillChargeType_GT1_ChargeType() {
		
	    	try {
	    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
	    		refmgr.getPaperBillChargeType(1, "", '\u0000', '\u0000', "", "");
	    		
	    	}catch (TelusAPIException e) {
	    		assertTrue("More than one matching found", true);
	    	}	
		 
	 }*/

}

   


