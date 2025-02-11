package com.telus.api.reference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.Memo;
import com.telus.api.account.PricePlanChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;

public class TestWL10EJBsPerformance extends TestCase {
	private ClientAPI api;
	private static final String ALT_PROPERTIES_FILEPATH = "..\\test.properties";
	
	private static Properties banListProp =null;
	private static TestWL10EJBsPerformance obj=null;    
    public TestWL10EJBsPerformance(String name) throws Throwable {
        super(name);
    }

    // note:  Added few 148 bans in pt168banslist property file for testing these test cases in pt148,not created any new file for testing
  static{
	
	  try {
			obj=new TestWL10EJBsPerformance("");
			banListProp= loadPropertiesFile();
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
	
		//Added for the testcase report generation scripts , its not the taking the ALT_PROPERTIES_FILEPATH and 'in' is null
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
		assertNotNull(banListProp);
		
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
		System.setProperty("com.telus.provider.providerURL", properties.getProperty("PROVIDER.URL", "t3://wlpt168easeca:8682"));
		
		
    }
    
    

	public void testWL10_ejb_changes()  {
		 
		 Account account = null;
	     int banCode=0;
	     try {
	     
	    	 for(int banCount=1;banCount<101;banCount++){
				
	    		banCode = getBan(banCount);
	    		System.out.println("START for BAN_"+banCount+"="+banCode);
	    		
				System.out.println("	1. Retrieve Account By BAN");
	    		AccountManager acctmgr=api.getProvider().getAccountManager();
	    		account=acctmgr.findAccountByBAN(banCode);
	    		System.out.println("		ActiveSubCount: "+account.getAllActiveSubscribersCount());
	    		
	    		System.out.println("	2. Retrieve Cancellation Penalty");
	    		CancellationPenalty cancellationPenalty=account.getCancellationPenalty();
	    		System.out.println("		Subscriber Number:"+cancellationPenalty.getSubscriberNumber());
	    		
	    		System.out.println("	3. Retrieve Future Status Requests");
	    		FutureStatusChangeRequest[] statusChangeRequests=account.getFutureStatusChangeRequests();
	    		System.out.println("		statusChangeRequests Length :"+statusChangeRequests.length);
	    		
	    		System.out.println("	4. Retrieve Memos");
	    		Memo[] memo=account.getMemos(1);
	    		System.out.println("		memo length : "+memo.length);
	    		
	    		System.out.println("	5. Retrieve the list of subscribers");
	    		Subscriber[]subCount= account.getSubscribers(10);
	    		System.out.println("		Subscriber list count :"+subCount.length);
	    		
	    		if(subCount.length>0){
	    			System.out.println("	6. Retrieve Equipment");
	    			Equipment equipment=subCount[0].getEquipment();
	    			System.out.println("		Equipment SerailNumber: "+equipment.getSerialNumber());
	    			
	    			System.out.println("	7. Retrieve Equipment Change History");
	    			EquipmentChangeHistory[] changeHistories =subCount[0].getEquipmentChangeHistory(getDateInput(1980, 01, 20), getDateInput(2011, 05, 10));
	    			System.out.println("		changeHistories length :"+changeHistories.length);
	    			
	    			System.out.println("	8. Retrieve Price Plan Change History");
	    			PricePlanChangeHistory[]pricePlanChangeHistories= subCount[0].getPricePlanChangeHistory(getDateInput(1980, 01, 20), getDateInput(2011, 05, 10));
	    			System.out.println("		pricePlanChangeHistories length :"+pricePlanChangeHistories.length);
	    		}
	    		
	    		System.out.println("END for BAN_"+banCount+"="+banCode);
			}
	    	} catch (FileNotFoundException e1) {
				System.out.println("FileNotFoundException : "+e1.getMessage());
	    	} catch (IOException e1) {
	    		System.out.println("IOException : "+e1.getMessage());
	    	}catch (TelusAPIException e) {
	    		System.out.println(" Error while calling testWL10_ejb_changes for "+banCode+" : "+ e.getMessage());
	    	}
	     
	 }
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	 
	 private int getBan(int Count) throws FileNotFoundException, IOException{
		 String ban=null;
		 ban= banListProp.getProperty("BAN_"+Count);
		 return Integer.parseInt(ban);
			
	 }
	 
	 private static Properties loadPropertiesFile() throws IOException, FileNotFoundException{
		 String ALT_FILEPATH = "..\\PT168BANList.properties";
		 String propertiesFilePath = System.getProperty("banListFile");
		 File file=null;
		 InputStream inputFile=null;
		 Properties ppProperties = new Properties();
		 
		 try{
			 if (propertiesFilePath != null){
				file = new File(propertiesFilePath);
			 }
						
			if (file == null) {
				System.out.println("Properties file path :"+ALT_FILEPATH);
				inputFile = obj.getClass().getResourceAsStream(ALT_FILEPATH);
			}else {
				try {
					inputFile = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					inputFile = obj.getClass().getResourceAsStream(ALT_FILEPATH);
				}	
			}
			
			//Added for testcase report generation, its not the taking the ALT_FILEPATH and 'inputFile' is null
			if(inputFile==null){
				file = new File("./src/test/com/telus/api/PT168BANList.propertie");
				try {
					inputFile= new FileInputStream(file);
				} catch (Exception e) {
					System.out.println(e.getMessage());
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
	 
	 

}  


