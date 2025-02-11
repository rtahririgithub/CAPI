package com.telus.api.reference;

/*
 * Created on Jul 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


//import com.telus.api.BaseTest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;


public class TestReferenceDataRetrieval extends TestCase {


	public TestReferenceDataRetrieval(String name) throws Throwable {
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
		System.out.println("**** VALUE of pathname is :" + propertiesFilePath);
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
    
//    public void testRetrieveRegularServices() {
//    	System.out.println("START");
//    	Service[] regularService=null;
//		try {
//			regularService = api.getProvider().getReferenceDataManager().getRegularServices();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		assertNotNull(regularService);
//    	System.out.println("# of Regular Services Retrieved: " + regularService.length);
//    }
    
    public void testRetrieveFeatures() {
    	Feature[] features = null;
    	try {
			features = api.getProvider().getReferenceDataManager().getFeatures();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(features);
    	System.out.println("# of Features Retrieved: " + features.length);
    }
    
    public void testRetrieveAccountTypes() {
    	AccountType[] accountTypes = null;
    	try {
    		accountTypes = api.getProvider().getReferenceDataManager().getAccountTypes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(accountTypes);
    	System.out.println("# of Account Types Retrieved: " + accountTypes.length);
    }
    
    public void testRetrieveAdjustmentReasons() {
    	AdjustmentReason[] adjustmentReasons = null;
    	try {
    		adjustmentReasons = api.getProvider().getReferenceDataManager().getAdjustmentReasons();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(adjustmentReasons);
    	System.out.println("# of Adjustment Reasons Retrieved: " + adjustmentReasons.length);
    }
    
    public void testRetrieveTaxationPolicies() {
    	TaxationPolicy[] taxPolicies = null;
    	try {
    		taxPolicies = api.getProvider().getReferenceDataManager().getTaxationPolicies();
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(taxPolicies);
    	System.out.println("# of Taxation Policies Retrieved: " + taxPolicies.length);
    }
    
    public void testRetrievePricePlans() {
    	PricePlanSummary[] plans = null;
    	PricePlanSummary plan = null;
    	PricePlanSelectionCriteria crit = new PricePlanSelectionCriteria();
    	
    	try {
    		plans = api.getProvider().getReferenceDataManager().findPricePlans(crit );
    		assertNotNull(plans);
    		if (plans.length>0)
    			plan = api.getProvider().getReferenceDataManager().getPricePlan(plans[0].getCode(), Equipment.EQUIPMENT_TYPE_DIGITAL, "BC", Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_REGULAR, Brand.BRAND_ID_TELUS);
    	} catch (Exception e) {
			e.printStackTrace();
		}
		
    	System.out.println("# of Price Plans Retrieved: " + plans.length);
    	if (plan != null)
    		System.out.println("Price Plan Details retrieved: [" + plan.getCode() + "]");
    }
        
    public void testRetrieveWPSServices() {
    	Service[] services = null;
    	try {
			services = api.getProvider().getReferenceDataManager().getWPSServices();
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
		assertNotNull(services);
		System.out.println("# of WPS Services Retrieved: " + services.length);
    }
    
    public void testRetrieveLogicalDate() {
    	Date logicalDate = null;
    	try {
			logicalDate = api.getProvider().getReferenceDataManager().getLogicalDate();
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
		assertNotNull(logicalDate);
		System.out.println("Logical Date retrieved: " + logicalDate);
    }
    
    public void testRetrieveAvailablePhoneNumber() {
    	AvailablePhoneNumber number = null;
    	
    	try {
    		Account acct = api.getAccountManager().findAccountByBAN(17605);
        	Subscriber sub = acct.getSubscriberByPhoneNumber("4034850238");
    		number = api.getProvider().getReferenceDataManager().getAvailablePhoneNumber("4034850238", sub.getProductType(), sub.getDealerCode());
		} catch (TelusAPIException e) {
			e.printStackTrace();
		}
		assertNotNull(number);
		System.out.println("Available Phone Number retrieved: [" + number.toString() + "]");
    }
    
    private void initSystemPropertiesFromInputStream(InputStream in) throws Throwable {

    	Properties properties = new Properties();
    	properties.load(in);
    	System.setProperty("com.telusmobility.config.java.naming.provider.url", properties.getProperty("LDAP.URL", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration"));
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wlqaeaseca:8682"));
        
    }
    
    public void testGetPaymentSourceTypes() {
    	PaymentSourceType pst = null;
    	try{
    		pst = api.getReferenceDataManager().getPaymentSourceType("PREACRCD", "C");
    	}catch (TelusAPIException e) {
    		e.printStackTrace();
    	}
    	assertNotNull(pst);
    	System.out.println(pst);
    }
}
