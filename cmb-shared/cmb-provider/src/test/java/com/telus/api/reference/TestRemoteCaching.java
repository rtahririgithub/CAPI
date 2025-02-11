package com.telus.api.reference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;

public class TestRemoteCaching extends TestCase {
	private ClientAPI api;
	private static final String ALT_PROPERTIES_FILEPATH = "..\\test.properties";
	
	private static int callCount=1;
	private static Properties ppProp =null;
	private static TestRemoteCaching obj=null;    
    public TestRemoteCaching(String name) throws Throwable {
        super(name);
    }

  static{
	
	  try {
			obj=new TestRemoteCaching("");
			ppProp= loadPropertiesFile();
		} catch (FileNotFoundException e1) {
			System.out.println("FileNotFoundException : "+e1.getMessage());
		} catch (IOException e1) {
			System.out.println("IOException : "+e1.getMessage());
		} catch (Throwable e) {
			System.out.println("Error while creating TestRemoteCaching "+e.getMessage());
		}
	  
  }
	
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
		
		
			assertNotNull(ppProp);
			
//			System.out.println("**** LDAP URL  : " + System.getProperty("com.telusmobility.config.java.naming.provider.url"));
//			System.out.println("**** PROVIDER URL : " + System.getProperty("com.telus.provider.providerURL"));
//			System.out.println("**** HELPER URL : " + System.getProperty("cmb.services.ReferenceDataHelper.url"));
//			System.out.println("**** FACADE URL : " + System.getProperty("cmb.services.ReferenceDataFacade.url"));
		
		try {			
			if (api == null) 
				api = ClientAPI.getInstance("18654", "apollo", "OLN");			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println("setup completed");
	}
    
    public void tearDown(){
//        System.out.println("tearDown called");
    	api = null;
        
//        System.out.println("tearDown completed");
    }
   
    private void initSystemPropertiesFromInputStream(InputStream in) throws Throwable {

    	Properties properties = new Properties();
    	properties.load(in);
    	System.setProperty("com.telusmobility.config.java.naming.provider.url", properties.getProperty("LDAP.URL", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration"));
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wls1smartdteca:5382"));
		//System.setProperty("cmb.services.ReferenceDataHelper.url", properties.getProperty("REF.DATA.HELPER.URL", "t3://sn25127:40152"));
		//System.setProperty("cmb.services.ReferenceDataFacade.url", properties.getProperty("REF.DATA.FACADE.URL", "t3://sn25127:40152"));
        
    }
    
    
    

	public void testRetrievepricePlan()  {
		 
		 PricePlanSummary pricePlanSummary = null;
	     String pricePlanCode=null;
	     int ppCount=0;
			try {
				ppCount= generateRandomNumbers(4069);
	    		pricePlanCode = getPriceplanCode(ppCount);
				
	    		ReferenceDataManager refmgr=api.getProvider().getReferenceDataManager();
	    		pricePlanSummary=refmgr.getPricePlan(pricePlanCode);
	    		
	    		System.out.println("\n CallCount ->"+callCount+", Priceplan "+ppCount+"->"+pricePlanSummary.getCode()+":"+pricePlanSummary.getDescription());
	    		
	    		String[] equipmentTypes = pricePlanSummary.getEquipmentTypes();
	    		String[] provinces = pricePlanSummary.getProvinces();
	    		
	    		int equipmentNo= generateRandomNumbers(equipmentTypes.length);
	    		int provinceNo= generateRandomNumbers(provinces.length);
	    		
	    		System.out.println("Equ_Length :"+equipmentTypes.length+ ",Random_equipmentNo : "+equipmentNo);
	    		System.out.println("pro_length :" +provinces.length+",Random_ProvinceNo :"+provinceNo);
	    		
	    		PricePlan pPlan = refmgr.getPricePlan(pricePlanSummary.getCode(), equipmentTypes[equipmentNo], provinces[provinceNo], 'I', 'R', 1);
	    		System.out.println("PricePlanCode : "+pPlan.getCode());
	    		
	    		assertNotNull(pricePlanSummary);
	    		assertNotNull(pPlan);
	    		
	    		
	    		callCount++;
	    	} catch (FileNotFoundException e1) {
				System.out.println("FileNotFoundException : "+e1.getMessage());
	    	} catch (IOException e1) {
	    		System.out.println("IOException : "+e1.getMessage());
	    	}catch (TelusAPIException e) {
	    		System.out.println(" Error while calling testRetrievepricePlan for "+pricePlanCode+" : "+ e.getMessage());
	    	}
	 }
	 
	 private String getPriceplanCode(int Count) throws FileNotFoundException, IOException{
		 String priceplan=null;
		
		 priceplan= ppProp.getProperty("priceplan"+Count);
		
		 if(priceplan==null){
			System.out.println("############count reset to 1");
			Count=1;
			priceplan= ppProp.getProperty("priceplan"+Count);
					
		 }
		return priceplan;
			
	 }
	 
	 private static Properties loadPropertiesFile() throws IOException, FileNotFoundException{
		 String ALT_FILEPATH = "..\\priceplanlist.properties";
		 String propertiesFilePath = System.getProperty("priceplanFile");
		 File file=null;
		 InputStream inputFile=null;
		 Properties ppProperties = new Properties();
		 
		 try{
			 if (propertiesFilePath != null){
				file = new File(propertiesFilePath);
			 }
						
			if (file == null) {
				inputFile = obj.getClass().getResourceAsStream(ALT_FILEPATH);
			}else {
				try {
					inputFile = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					inputFile = obj.getClass().getResourceAsStream(ALT_FILEPATH);
				}	
			}
			ppProperties.load(inputFile);
		 } catch (Exception e1) {
			 System.out.println("Exception while loading properties file : "+e1.getMessage());
		 }finally {
			 inputFile = null;
			 file = null;			
		}
		return ppProperties;
		
	 }
	 
	 private int generateRandomNumbers(int maxNumber){
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(maxNumber);
		 return randomInt;
	 }

}  


