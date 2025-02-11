//package com.telus.provider;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.Hashtable;
//
//import javax.naming.Context;
//
//import com.telus.api.BaseTest;
//import com.telus.api.ClientAPI;
//import com.telus.api.TelusAPIException;
//import com.telus.api.account.Account;
//import com.telus.api.account.AccountManager;
//import com.telus.api.account.AccountSummary;
//import com.telus.api.account.AvailablePhoneNumber;
//import com.telus.api.account.IDENAccount;
//import com.telus.api.account.IDENSubscriber;
//import com.telus.api.account.PCSAccount;
//import com.telus.api.account.PCSSubscriber;
//import com.telus.api.portability.PRMSystemException;
//import com.telus.api.portability.PortInEligibility;
//import com.telus.api.portability.PortRequest;
//import com.telus.api.portability.PortRequestManager;
//import com.telus.api.portability.PortRequestSummary;
//import com.telus.api.reference.NumberGroup;
//import com.telus.api.reference.Province;
//import com.telus.api.reference.ReferenceDataManager;
//import com.telus.eas.account.info.AvailablePhoneNumberInfo;
//import com.telus.eas.account.info.PhoneNumberReservationInfo;
//import com.telus.eas.account.info.ReservePortInMemberIdentityInfo;
//import com.telus.eas.portability.info.PortInEligibilityInfo;
//import com.telus.eas.portability.info.PortRequestInfo;
//import com.telus.eas.subscriber.ejb.SubscriberManagerEJBHome;
//import com.telus.eas.subscriber.ejb.SubscriberManagerEJBRemote;
//import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
//import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
//import com.telus.eas.utility.dao.ReferenceDataRefDAO;
//import com.telus.eas.utility.info.NumberGroupInfo;
//import com.telus.eas.utility.info.PricePlanInfo;
//import com.telus.provider.account.TMPhoneNumberReservation;
//import com.telus.provider.portability.TMPortRequest;
//import com.telus.provider.reference.TMPricePlan;
//import com.telus.provider.reference.TMReferenceDataManager;
//
//
//public class TestWLNP extends BaseTest {
//	static {
//		//setupD3();
//		setupEASECA_QA();
//		
//	}
//    static ReferenceDataRefDAO referenceDataRefDAO = null;
//    static Connection con = null;
//    static ClientAPI api = null;
//    static ClientAPI.Provider provider = null;
//    static AccountManager accountManager = null;
//    static ReferenceDataManager referenceMngr = null;
//    static Account account = null; 
//    
//	public TestWLNP(String name) throws Throwable {
//		super(name);
//	}
//	
//	private static void startClientAPI(int ban){
//    	getConnection();
//    	referenceDataRefDAO = new ReferenceDataRefDAO(con);
//    	try { 	
//	        api = ClientAPI.getInstance("18654", "apollo", "OLN");
//	        
//	        System.out.println("getting provider...");
//	        provider = api.getProvider();
//	        System.out.println("after getting provider...");
//	
//	        System.out.println("getting AccountManager...");
//	        accountManager = provider.getAccountManager();
//	        System.out.println("after getting AccountManager...");
//	
//	        System.out.println("getting ReferenceDataManager...");
//	        referenceMngr = provider.getReferenceDataManager();
//	        referenceMngr.refresh();
//	        System.out.println("after getting ReferenceDataManager...");
//	        
//	        System.out.println("getting account by BAN...");
//	        account = accountManager.findAccountByBAN(ban);
//        
//    	}catch(Exception e) {
//            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//	}
//	
//    private static void getConnection() {
//		// DEV
//    	String url="jdbc:oracle:thin:@new-hippo:1521:dkb10";
//	    String username = "amdocs_extd3_ref";
//	    String password = "amdocs_extd3_ref";
//
//	    try{
//		   DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
//		   con = DriverManager.getConnection(url,username,password);
//	    } catch (SQLException t){
//	      System.out.println(t.getMessage());
//	      t.printStackTrace();
//	    }
//	 }
//
//    
//    private static void releaseConnection() {
//	    try {
//	      con.close();
//	    } catch(Throwable t){
//	      t.printStackTrace();
//	    }
//     }	
//
//  
//	public void _testCase01() throws Throwable {
//		System.out.println("tsetCase01..");
//		int ban = 6006493;
//		startClientAPI (ban);
//		getAvailableNumberGroups(account);
//		releaseConnection();
//	}
//	
//	public void _testCase02() throws Throwable {
//		System.out.println("tsetCase02..");
//		int ban = 6006493;
//		startClientAPI (ban);
//		getAvailablePhoneNumber(referenceMngr);
//		releaseConnection();
//	}
//	
//	public void _testCase03() throws Throwable {
//		System.out.println("tsetCase03..");
//		int ban = 6006493;
//		startClientAPI (ban);
//		getProvinces(referenceMngr);
//		releaseConnection();
//	}
//	
//	public void _testCase04() throws Throwable {
//		System.out.println("tsetCase04..");
//		int ban = 6025139;
//		startClientAPI (ban);
//		copyNameAndAddressToPortRequest(account);
//		releaseConnection();
//	}
//
//	public void _testCase05() throws Throwable {
//		System.out.println("tsetCase05..");
//		int ban = 6025139;
//		startClientAPI (ban);
//		validatePortInRequest(account);
//		releaseConnection();
//	}	
//	
//	public void _testCase06() throws Throwable {
//		System.out.println("tsetCase06..");
//		int ban = 6025139;
//		startClientAPI (ban);
//		activatePortInRequest(accountManager);
//		releaseConnection();
//	}		
//
//	public void _testCase07() throws Throwable {
//		System.out.println("tsetCase07..");
//		int ban = 6025139;
//		startClientAPI (ban);
//		checkPortInEligibility(provider);
//		releaseConnection();
//	}
//	
//	
//	public void _testCase08() throws Throwable {
//		System.out.println("tsetCase08..");
//		int ban = 6025139;
//		startClientAPI (ban);
//		checkPortOutEligibility(provider);
//		releaseConnection();
//	}
//	
//	public void _testCase09() throws Throwable {
//		System.out.println("tsetCase09..");
//		int ban = 6029299;
//		startClientAPI (ban);
//		getPortRequestSummary(accountManager);
//		releaseConnection();
//	}
//	
//	public void testCase10() throws Throwable {
//		System.out.println("tsetCase10..");
//		int ban = 6029299;
//		startClientAPI (ban);
//		getPortRequestsByBan(account);
//		releaseConnection();
//	}
//	
//	
//	public void testCase11() throws Throwable {
//		System.out.println("tsetCase11..");
//		int ban = 6029299;
//		startClientAPI (ban);
//		getPortRequestsPhoneNumber(accountManager);
//		releaseConnection();
//	}	
//  //fail
//	public void _testCase12() throws Throwable {
//		System.out.println("tsetCase12..");
//		int ban = 70402322;
//		startClientAPI (ban);
//		createPortInRequestReserveAndSavePCSPortInSub(account,provider);
//		releaseConnection();
//	} 
//	
//	
//	public void _testCase13() throws Throwable {
//		System.out.println("tsetCase13..");
//		int ban = 6018080;
//		startClientAPI (ban);
//		createPortInRequestReserveAndSaveIDENPortInSub(account,provider);
//		releaseConnection();
//	} 
//	//fail
//	public void _testCase14() throws Throwable {
//		System.out.println("tsetCase14..");
//		int ban = 6018080;
//		startClientAPI (ban);
//		changePCStoPortIn(account, accountManager, referenceMngr);
//		releaseConnection();
//	} 
//	
//	public void _testCase15() throws Throwable {
//		System.out.println("tsetCase15..");
//		int ban = 6376486;
//		startClientAPI (ban);
//		changeIDENtoPortIn(account, accountManager, referenceMngr);
//		releaseConnection();
//	} 
//
//	public void _testCase16() throws Throwable {
//		System.out.println("tsetCase16..");
//		int ban = 6001399;
//		startClientAPI (ban);
//		setKnowbilityPortIndicatorForPortedInNumber();
//		releaseConnection();
//	} 	
//	
//	public void _testCase17() throws Throwable {
//		System.out.println("tsetCase17..");
//		int ban = 6001399;
//		startClientAPI (ban);
//		cancelSubscriber(account);
//		releaseConnection();
//	} 
//	
//	public void _testCase18() throws Throwable {
//		System.out.println("tsetCase18..");
//		int ban = 6001399;
//		startClientAPI (ban);
//		cancelOrSuspendPortedInSubscriber();
//		releaseConnection();
//	}
//	
//	public void _testCase19() throws Throwable {
//		System.out.println("tsetCase19..");
//		int ban = 6008914;
//		startClientAPI (ban);
//		resumeOrRestorePortedInSubscriber(account);
//		releaseConnection();
//	}
// 
//	public void _testCase20() throws Throwable {
//		System.out.println("tsetCase20..");
//		int ban = 6008914;
//		startClientAPI (ban);
//		searchSubscriberByAdditionalMsiSdn();
//		releaseConnection();
//	}
//	
//	public void _testCase21() throws Throwable {
//		System.out.println("tsetCase21..");
//		int ban =6008914;
//		startClientAPI (ban);
//		cancelAdditionalMsisdn();
//		releaseConnection();
//	}
//	
//	public void _testCase22() throws Throwable {
//		System.out.println("tsetCase22..");
//		int ban = 6008914;
//		startClientAPI (ban);
//		deleteMsisdnFeature();
//		releaseConnection();
//	}	
//	
//    private static PortRequestInfo createPortInRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate, PCSSubscriber subscriber) throws PRMSystemException,  TelusAPIException {
//	   	  
//	   	  PortRequestInfo portRequest = new PortRequestInfo();
//	   	  TMPortRequest tmPortRequest = null;
//	      try {
//	    	  tmPortRequest = (TMPortRequest)subscriber.newPortRequest(phoneNumber,NPDirectionIndicator, prePopulate);
//	    	  portRequest = tmPortRequest.getDelegate();
//	    	  portRequest.setPortRequestId(""); 
//	    	     
//	          portRequest.setOSPAccountNumber("123456");
//	          portRequest.setOSPSerialNumber("1234567890");
//	          portRequest.setOSPPin("1234");
//	          portRequest.setAlternateContactNumber("4164165577");
//	          portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2010-10-25"));
//	          portRequest.setAgencyAuthorizationName("ASDF");
//	          portRequest.setAgencyAuthorizationIndicator("Y");
//	          portRequest.setAutoActivate(false);
//	          //portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
//	          portRequest.setPhoneNumber(phoneNumber);
//	          portRequest.setRemarks("");
//	          portRequest.setBanId(subscriber.getAccount().getBanId());
//	          portRequest.setCanBeActivate(true);
//	          portRequest.setCanBeCancel(true);
//	          portRequest.setCanBeModify(true);
//	          portRequest.setCanBeSubmit(true);
//	          portRequest.setCreationDate(java.sql.Date.valueOf("2010-10-25"));
//	          portRequest.setIncomingBrandId(1);
//	          portRequest.setOutgoingBrandId(1);
//
//	          
//	          if (!prePopulate){
//	        	  Account  account = subscriber.getAccount();
//				  portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
//				  portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);
//			  
//				  portRequest.setAlternateContactNumber(account.getOtherPhone());
//				  portRequest.setPhoneNumber(phoneNumber);
//				  portRequest.setPortDirectionIndicator(NPDirectionIndicator);
//	          }
//	     
//	//          PortRequestNameInfo name = new PortRequestNameInfo();
//	//          name.setFirstName("À honnêteté");
//	//          name.setLastName("À honnêteté");   
//	//          portRequest.setPortRequestName(name);
//	       }catch(Exception e) {
//	    		  throw new TelusAPIException(e);
//	       }
//	       System.out.println("N E W     P O R T     I N     R E Q U E S T");
//	       System.out.println("\n portRequest = " + portRequest);
//	       return portRequest;
//	     }
//	     
//	     private static PortRequestInfo createIDENPortInRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate, IDENSubscriber subscriber) throws PRMSystemException,  TelusAPIException {
//		   	  
//		   	  PortRequestInfo portRequest = new PortRequestInfo();
//		   	  TMPortRequest tmPortRequest = null;
//		      try {
//		    	  tmPortRequest = (TMPortRequest)subscriber.newPortRequest(phoneNumber,NPDirectionIndicator, prePopulate);
//		    	  portRequest = tmPortRequest.getDelegate();
//		    	  portRequest.setPortRequestId(""); 
//		    	     
//		          portRequest.setOSPAccountNumber("10000184");
//		          portRequest.setOSPSerialNumber("17906751502");
//		          portRequest.setOSPPin("1111");
//		          portRequest.setAlternateContactNumber("416-416-5577");
//		          portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2006-01-20"));
//		          portRequest.setAgencyAuthorizationName("TEST");
//		          portRequest.setAgencyAuthorizationIndicator("Y");
//		          portRequest.setAutoActivate(true);
//		       //   Calendar toDay = Calendar.getInstance();
//		       //   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-ddGhh:mm:ss.Sz");
//		       //   String desiredDateTime = formatter.format(toDay.getTime());
//		       //   toDay.setTime(java.sql.Date.valueOf(desiredDateTime));
//		          portRequest.setDesiredDateTime(java.sql.Date.valueOf("2006-10-20"));
//		          portRequest.setPhoneNumber(phoneNumber);
//		          portRequest.setRemarks("Create port In request - testing");
//		          portRequest.setBanId(10000184);
//		          portRequest.setCanBeActivate(true);
//		          portRequest.setCanBeCancel(true);
//		          portRequest.setCanBeModify(true);
//		          portRequest.setCanBeSubmit(true);
//		          portRequest.setCreationDate(java.sql.Date.valueOf("2006-10-23"));
//		          
//		       }
//		       catch(Exception e) {
//		          throw new TelusAPIException(e);
//		       }
//		       System.out.println("N E W     P O R T     I N     R E Q U E S T");
//		       System.out.println("\n portRequest = " + portRequest);
//		       return portRequest;
//	     }
//	
//	
//	
//		private static void getAvailableNumberGroups(Account account){
//			System.out.println("getAvailableNumberGroups Start...");
//			
//			try {
//	            PCSSubscriber sub =  ((PCSAccount)account).newPCSSubscriber("4033180402", true, "EN");
//	            NumberGroup[] tmp = sub.getAvailableNumberGroups(null);
//	            System.out.println("NumberGroup.length = " + tmp.length);
//	            for (int i=0; i<tmp.length;i++){
//	            	System.out.println("NumberGroup[" + i + "] = " + tmp[i]);
//	            }
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getAvailableNumberGroups End...");
//			
//		}
//		
//		
//		private static void getAvailablePhoneNumber(ReferenceDataManager referenceMngr){
//			System.out.println("getAvailablePhoneNumber Start...");
//			
//			try {
//	            AvailablePhoneNumber tmp = referenceMngr.getAvailablePhoneNumber("6473770319","I", "0000029016");
//	            System.out.println("AvailablePhoneNumber = " + tmp);
//
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getAvailablePhoneNumber End...");
//			
//		}
//		
//		
//		private static void getProvinces(ReferenceDataManager referenceMngr){
//			System.out.println("getProvinces Start...");
//			
//			try {
//	            Province[] tmp = referenceMngr.getProvinces();
//	            for (int i=0; i<tmp.length;i++){
//	            	System.out.println("Province[" + i + "] = " + tmp[i]);
//	            }
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getProvinces End...");
//			
//		}
//		
//		
//		private static void copyNameAndAddressToPortRequest(Account account){
//			System.out.println("copyNameAndAddressToPortRequest Start...");
//			
//			try {
//	            
//	            PortRequestInfo portRequest = new PortRequestInfo();
//	            System.out.println("copy name...");
//	            PortRequest portRequestName = PortRequestManager.Helper.copyName(account,portRequest);
//	            System.out.println("after copy name...");
//	            System.out.println("portRequestName = " + portRequestName);
//	            
//	            System.out.println("copy address...");
//	            PortRequest portRequestAddress = PortRequestManager.Helper.copyAddress(account,portRequest);
//	            System.out.println("after copy address...");
//	            System.out.println("portRequestAddress = " + portRequestAddress);
//	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("copyNameAndAddressToPortRequest End...");
//			
//		}
//		
//		
//		private static void validatePortInRequest(Account account){
//			System.out.println("validatePortRequest Start...");
//			
//			try {
//	            String pNumber = "4033180402";
//	            PCSSubscriber sub =  ((PCSAccount)account).newPCSSubscriber("4033180402", true, "EN");
//	            createPortInRequest(pNumber, "A", true , sub);
//	            sub.getPortRequest().validate();
//	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("validatePortRequest End...");
//			
//		}	
//		
//		private static void activatePortInRequest(AccountManager accountManager){
//			System.out.println("activatePortInRequest Start...");
//			
//			try {
//	            String pNumber = "4033180402";
//	            PCSSubscriber subscriber =  (PCSSubscriber)accountManager.findSubscriberByPhoneNumber(pNumber);
//	            subscriber.getPortRequest().activate();
//	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("activatePortInRequest End...");
//			
//		}	
//		
//		
//		private static void checkPortInEligibility(ClientAPI.Provider provider){
//			System.out.println("checkPortInEligibility Start...");
//			
//			try {
//				String pNumber = "4284444234";
//				int brand = 1;
//	            PortRequestManager portRequestManager = provider.getPortRequestManager();
//	            PortInEligibility elig = portRequestManager.testPortInEligibility(pNumber,PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2C, brand);
//	            System.out.println("PortInEligibility" + elig);	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("checkPortInEligibility End...");
//		}
//		
//		private static void checkPortOutEligibility(ClientAPI.Provider provider){
//			System.out.println("checkPortOutEligibility Start...");
//			
//			try {
//				String pNumber = "4033180402";
//				String ndpInd = "1";  // Not sure what is that
//	            PortRequestManager portRequestManager = provider.getPortRequestManager();
//	            portRequestManager.testPortOutEligibility(pNumber, ndpInd);
//	            	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("checkPortOutEligibility End...");
//		}
//		
//		
//		private static void getPortRequestSummary(AccountManager accountManager){
//			System.out.println("getPortRequestSummary Start...");
//			
//			try {
//				String pNumber = "4033180402";
//	            PCSSubscriber subscriber =  (PCSSubscriber)accountManager.findSubscriberByPhoneNumber(pNumber);
//	            PortRequestSummary sum = subscriber.getPortRequestSummary();
//	            System.out.println("PortRequestSummary = " + sum); 
//	            	            	        
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getPortRequestSummary End...");
//		}
//		
//		
//		private static void getPortRequestsByBan(Account account){
//			System.out.println("getPortRequestsByBan Start...");
//			
//			try {
//	            PortRequest[] portRequests = account.getPortRequests();
//	            if (portRequests != null){
//	            	System.out.println("PortRequest length = " + portRequests.length);
//	            	for (int i=0; i<portRequests.length;i++){
//	            		System.out.println("PortRequest[" + i + "] = " + portRequests[i]);
//	            	}
//	            }else
//	            	System.out.println("PortRequest[] = NULL");
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getPortRequestsByBan End...");
//		}
//
//		
//		private static void getPortRequestsPhoneNumber(AccountManager accountManager){
//			System.out.println("getPortRequestsPhoneNumber Start...");
//			
//			try {
//				String pNumber = "7053210051";
//	            PCSSubscriber subscriber =  (PCSSubscriber)accountManager.findSubscriberByPhoneNumber(pNumber);
//	            PortRequest tmp = subscriber.getPortRequest();
//	            if (tmp != null)
//	            	System.out.println("PortRequestSummary = " + tmp);
//	            else
//	            	System.out.println("PortRequest = NULL");
//	            	
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("getPortRequestsPhoneNumber End...");
//		}
//		
//		
//		private static void createPortInRequestReserveAndSavePCSPortInSub(Account account, ClientAPI.Provider provider){
//			System.out.println("createPortInRequestReserveAndSavePCSPortInSub Start...");
//			
//			try {
//	            String phoneNumber = "4163160002";
//	            PCSSubscriber subscriber =  ((PCSAccount)account).newPCSSubscriber("15603173789", true, "EN");
//	            createPortInRequest(phoneNumber, "A", true , subscriber);
//	            System.out.println("RESERVE PCS Ported In subscriber = " + phoneNumber);
//
//	            subscriber.setAddress(account.getAddress());
//	            subscriber.setFirstName(account.getContactName().getFirstName());
//	            subscriber.setMiddleInitial(account.getContactName().getMiddleInitial());
//	            subscriber.setLastName(account.getContactName().getLastName());
//	            subscriber.setLanguage("CA");
//	            
//	            TMPhoneNumberReservation phoneNumberReservation = new TMPhoneNumberReservation();
//	            phoneNumberReservation.setProductType("C");
//	            phoneNumberReservation.setPhoneNumberPattern(phoneNumber);
//	            phoneNumberReservation.setWaiveSearchFee(true);
//	            NumberGroupInfo ngp = new NumberGroupInfo();
//	            ngp.setCode("FPC");
//	            ngp.setProvinceCode("ON");
//	            ngp.setNumberLocation("TLS");
//	            ngp.setDefaultDealerCode("0000029016");
//	            phoneNumberReservation.setNumberGroup(ngp);
//
//	            
//	            PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
//	            portInEligibility.setPhoneNumber(phoneNumber);
//	            portInEligibility.setIncomingBrandId(1);
//	            
//	            subscriber.reservePhoneNumber(phoneNumberReservation,portInEligibility);
//	            System.out.println("End Rreservation of PCS Ported In subscriber " );
//	            
////	            System.out.println("Start Save Reserved PCS Ported In subscriber " );
////	            
////	      	  	PricePlanInfo tmpPP = referenceDataRefDAO.retrievePricePlan("1XNVPN250"); //DEV
////	      	  	//System.out.println(" tmpPP = " + tmpPP);
////	      	    TMPricePlan pricePlan = new TMPricePlan((TMReferenceDataManager)provider.getReferenceDataManager(),tmpPP);
////	            
////	            subscriber.newContract(pricePlan,12);
////	            subscriber.setActivityReasonCode("ACOR");
////	            
////	            subscriber.save(true,null,portInEligibility);
////	            System.out.println("End Save Reserved PCS Ported In subscriber " );
//
//	            
////	            System.out.println("Start Activation of the Reserved PCS Ported In subscriber " );
////	            Date startServiceDate = new Date();
////	            subscriber.activate("ACOR", startServiceDate, "Dimitry's Test",true, true);
////	            System.out.println("End Activation of the  Reserved PCS Ported In subscriber " );
////		            
////	     
////	            Thread.sleep(30000);
////		            
////	            System.out.println("RELEASE PCS Ported In subscriber in One session with reserve");
////	            subscriber.unreserve(true);
////	            System.out.println("End Release of PCS Ported In subscriber in One session with reserve" );
//      	            
////	            System.out.println("RELEASE PCS Ported In subscriber");
////	            PCSSubscriber sub = (PCSSubscriber)account.getSubscriber("4164160044");
////	            //PCSSubscriber sub1 = (PCSSubscriber)accountManager.findSubscriberByPhoneNumber("4163335564");
////	            sub.unreserve(true);
////	            System.out.println("End Release of PCS Ported In subscriber" );	            
//	            
//	            
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("createPortInRequestReserveAndSavePCSPortInSub End...");
//		}
//
//		
//		private static void createPortInRequestReserveAndSaveIDENPortInSub(Account account, ClientAPI.Provider provider){
//			System.out.println("createPortInRequestReserveAndSaveIDENPortInSub Start...");
//			
//			try {
//	            String phoneNumber = "4167919002";
//	            //String phoneNumber = "4167919002";
//	            //String phoneNumber = "4164163120";
//	            System.out.println("Create Port In request *** Start");
//	            IDENSubscriber iDENsubscriber =  ((IDENAccount)account).newIDENSubscriber("000500000777110", true,"EN") ;
//	            createIDENPortInRequest(phoneNumber, "A", true, iDENsubscriber);
//	            System.out.println("Create Port In request *** End");
//	            int fleetId ;
//	            int urbanId ;
//	            String memberId;	            
//	            
//	            System.out.println("RESERVE Iden Ported In subscriber = " + phoneNumber);
//	            TMPhoneNumberReservation phoneNumberReservation = new TMPhoneNumberReservation();
//	            phoneNumberReservation.setProductType("I");
//	            phoneNumberReservation.setPhoneNumberPattern(phoneNumber);
//	            NumberGroupInfo ngp = new NumberGroupInfo();
//	            //ngp.setCode("FME");
//	            ngp.setCode("TO1");
//	            ngp.setProvinceCode("ON");
//	            ngp.setNumberLocation("TLS");
//	            ngp.setDefaultDealerCode("0000029016");
//	            ngp.setNetworkId(1);
//	            phoneNumberReservation.setNumberGroup(ngp);
//
//	            PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
//	            portInEligibility.setPhoneNumber(phoneNumber);
//	            portInEligibility.setIncomingBrandId(1);
//
//	            iDENsubscriber.reservePhoneNumber(phoneNumberReservation,portInEligibility);
//	            fleetId = 250;
//	            urbanId = 250;
//	            memberId = "5584";
//	            iDENsubscriber.setAddress(account.getAddress());
//	            iDENsubscriber.setFirstName(account.getContactName().getFirstName());
//	            iDENsubscriber.setMiddleInitial(account.getContactName().getMiddleInitial());
//	            iDENsubscriber.setLastName(account.getContactName().getLastName());
//	            ReservePortInMemberIdentityInfo reservetionMemberIdentity = new ReservePortInMemberIdentityInfo();
//	            reservetionMemberIdentity.setFleetId(fleetId);
//	            reservetionMemberIdentity.setMemberId(memberId);
//	            reservetionMemberIdentity.setUrbanId(urbanId);
//	            
//	            iDENsubscriber.reserveIDENResourcesForPortIn((PhoneNumberReservationInfo)phoneNumberReservation.getPhonenumberReservation0(), true, false,reservetionMemberIdentity, "0000029016");
//	            System.out.println("End Rreservation of Iden Ported In subscriber " );
//
//
//	           
//	            System.out.println("Start Save Reserved Iden Ported In subscriber " );
//	            
//	      	  	PricePlanInfo tmpPP = referenceDataRefDAO.retrievePricePlan("MPDON80M ");
//	      	  	//System.out.println(" tmpPP = " + tmpPP);
//	      	    TMPricePlan pricePlan = new TMPricePlan( (TMReferenceDataManager)provider.getReferenceDataManager(),tmpPP);
//	            
//	      	    iDENsubscriber.newContract(pricePlan,12);
//	      	    iDENsubscriber.setActivityReasonCode("ACOR");
//	      	    
//	      	    iDENsubscriber.save(true,null,portInEligibility);
//	            System.out.println("End Save Reserved Iden Ported In subscriber " );
//
////	            Thread.sleep(30000);
////	            
////	            System.out.println("RELEASE Iden Ported In subscriber in One session with reserve");
////	            iDENsubscriber.unreserve(true);
//// 	            System.out.println("End Iden of PCS Ported In subscriber in One session with reserve" );         
////   
////	        	System.out.println("RELEASE IDEN Ported In subscriber");
////	        	IDENSubscriber sub = (IDENSubscriber)account.getSubscriber("M000002766");
////	        	sub.unreserve(true);
////	        	System.out.println("End Release of IDEN Ported In subscriber" );	            
//
////	            System.out.println("Start Activate Reserved IDEN Ported In subscriber " );
////	        	IDENSubscriber sub = (IDENSubscriber)account.getSubscriber("M000002768");
////	        	sub.setActivityReasonCode("ACOR");
////	        	Date startServicwDate = new Date();
////	        	sub.activate("ACOR",startServicwDate, "TEST", true,true);
////	            System.out.println("End Activate Reserved IDEN Ported In subscriber " );
//	        	
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("createPortInRequestReserveAndSaveIDENPortInSub End...");
//		}
//		
//		
//		
//		private static void changePCStoPortIn(Account account ,AccountManager accountManager, ReferenceDataManager referenceMngr){
//			System.out.println("changePCStoPortIn Start...");
//			
//			try {
//	            System.out.println("Start Change PCS Number to Ported In Number " );
//	            String phoneNumber = "4184180771";
//
//	            AccountSummary  summary = account.getAccount();
//	            String defaultDealer = referenceMngr.getAccountType(summary).getDefaultDealer();
//	   
//	            AvailablePhoneNumberInfo availablePhoneNumber = new AvailablePhoneNumberInfo();
//	            availablePhoneNumber = (AvailablePhoneNumberInfo)referenceMngr.getAvailablePhoneNumber(phoneNumber,"C", "C00RS05806");
//	           
//	            //availablePhoneNumber.setPhoneNumber(phoneNumber);
//	            //availablePhoneNumber.setNumberLocationCode("TLS");
//	            
//	            //NumberGroupInfo ngp = new NumberGroupInfo();	            
//	            //ngp.setCode("TOR");
//	            //ngp.setProvinceCode("ON");
//	            //ngp.setNumberLocation("TLS");
//	            //ngp.setDefaultDealerCode("0000029016");
//	            //availablePhoneNumber.setNumberGroup(ngp);
//	             
//	            PCSSubscriber subscriber =  (PCSSubscriber)accountManager.findSubscriberByPhoneNumber("4184180771");
//	            createPortInRequest(phoneNumber, "A", false , subscriber);
//	            String subdealer  = subscriber.getDealerCode();
//	            PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
//	            portInEligibility.setPostPaidCoverage(true);
//	            
//	            subscriber.changePhoneNumber(availablePhoneNumber,"CR", false,null,null,portInEligibility);
//
//	            System.out.println("End Change PCS Number to Ported In Number " );
//
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("changePCStoPortIn End...");
//		}		
//		
//		private static void changeIDENtoPortIn(Account account ,AccountManager accountManager, ReferenceDataManager referenceMngr){
//			System.out.println("changeIDENtoPortIn Start...");
//			
//			try {
//	            System.out.println("Start Change IDEN Number to Ported In Number " );
//
//	            
//	            AvailablePhoneNumberInfo availablePhoneNumber = new AvailablePhoneNumberInfo();
//	            availablePhoneNumber = (AvailablePhoneNumberInfo)referenceMngr.getAvailablePhoneNumber("4167913158","I", "0000029016");
//	            
//	            //availablePhoneNumber.setPhoneNumber("4167913121");
//	            //availablePhoneNumber.setNumberLocationCode("TLS");
//	            
//	            //NumberGroupInfo ngp = new NumberGroupInfo();	            
//	            //ngp.setCode("TO1");
//	            //ngp.setProvinceCode("ON");
//	            //ngp.setNumberLocation("TLS");
//	            //ngp.setDefaultDealerCode("0000029016");
//	            //availablePhoneNumber.setNumberGroup(ngp);
//	            System.out.println("findSubscriberByPhoneNumber4168948214");
//	            IDENSubscriber subscriber =  (IDENSubscriber)accountManager.findSubscriberByPhoneNumber("4168948214");
//	            
//	            PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
//	            portInEligibility.setPostPaidCoverage(true);
//	            
//	            subscriber.changePhoneNumber(availablePhoneNumber,"CR", true,"0000029016",null,portInEligibility);
//
//	            System.out.println("End Change IDEN Number to Ported In Number " );
//
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("changeIDENtoPortIn End...");
//		}	
//		
//		
//		private static void setKnowbilityPortIndicatorForPortedInNumber(){
//			System.out.println("setKnowbilityPortIndicatorForPortedInNumber Start...");
//			
//			try {
//	            System.out.println("Start Setting  Knowbility Port Indicator for Ported In Number" );
//	        	
//	            Hashtable env = new Hashtable();
//	    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//	    		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//
//	    		javax.naming.Context context = new javax.naming.InitialContext(env);
//
//	    		SubscriberManagerEJBHome subManagerEJBHome = (SubscriberManagerEJBHome) context.lookup("telus-eca/ejb/stateful/SubscriberManager");
//	    		SubscriberManagerEJBRemote subscriberManagerEJB = (SubscriberManagerEJBRemote)subManagerEJBHome.create("18654", "apollo", "OL");
//	    		//subscriberManagerEJB.setSubscriberPortIndicator("4167913010");  // IDEN
//	    		//subscriberManagerEJB.snapBack("4167913010");  // IDEN
//	            //subscriberManagerEJB.setSubscriberPortIndicator("4163335591");  // PCS
//	            subscriberManagerEJB.snapBack("4168941178");  // PCS
//
//	            System.out.println("End Setting  Knowbility Port Indicator for Ported In Number" );
//
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("setKnowbilityPortIndicatorForPortedInNumber End...");
//		}	
//	
//		
//		
//		private static void cancelSubscriber(Account account){
//			System.out.println("cancelSubscriber Start...");
//			
//			try {
//	  			System.out.println("Start Cancel Subscriber" );
//	        	PCSSubscriber sub = (PCSSubscriber)account.getSubscriber("6047355740");
//	    		Date activityDate = java.sql.Date.valueOf("2007-02-15");
//	        	sub.cancel(activityDate,"AREA",'1',"","");
//
//	  			System.out.println("End Cancel Subscriber" );
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("cancelSubscriber End...");
//		}	
//		
//		private static void cancelOrSuspendPortedInSubscriber(){
//			System.out.println("cancelOrSuspendPortedInSubscriber Start...");
//			
//			try {
//	  			System.out.println("Start Cancel or Suspend Ported In Subscriber" );
//	        	
//	            Hashtable env = new Hashtable();
//	    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//	    		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//	    		env.put(Context.SECURITY_PRINCIPAL, "ejb_user");
//	    		env.put(Context.SECURITY_CREDENTIALS, "ejb_user");
//
//	    		javax.naming.Context context = new javax.naming.InitialContext(env);
//
//	    		SubscriberManagerEJBHome subManagerEJBHome = (SubscriberManagerEJBHome) context.lookup("telus-eca/ejb/stateful/SubscriberManager");
//	    		SubscriberManagerEJBRemote subscriberManagerEJB = (SubscriberManagerEJBRemote)subManagerEJBHome.create("18654", "apollo", "OL");
//	    		Date activityDate = java.sql.Date.valueOf("2007-07-18");
//	            subscriberManagerEJB.cancelPortedInSubscriber(70020495,"4168941367","PRTO", activityDate, "Y", false);  //PCS
//	            //subscriberManagerEJB.suspendPortedInSubscriber(20007202,"9057160072","PRTO", activityDate, "Y");  //PCS
//	            //subscriberManagerEJB.cancelPortedInSubscriber(55673955,"4163335591","AREA", activityDate, "Y");  // 
//	            //subscriberManagerEJB.suspendPortedInSubscriber(55673955,"4163335586","AIE", activityDate, "Y");  // 
//
//	            System.out.println("End Cancel or Suspend Ported In Subscriber" );
//			
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("cancelOrSuspendPortedInSubscriber End...");
//		}	
//		
//		
//		private static void resumeOrRestorePortedInSubscriber(Account account){
//			System.out.println("resumeOrRestorePortedInSubscriber Start...");
//			
//			try {
//	    		System.out.println("Start Resume or Restore Ported In Subscriber" );
//	            Date activityDate = java.sql.Date.valueOf("2006-12-15");
//	            //IDENSubscriber sub = (IDENSubscriber)account.getSubscriber("M000002767");
//	            //sub.restore(activityDate, "PR","", "W");
//	            //IDENSubscriber sub1 = (IDENSubscriber)account.getSubscriber("M000002768");
//	            //sub1.restore(activityDate, "CRQ","", "W");
//
//	            PCSSubscriber sub = (PCSSubscriber)account.getSubscriber("9057160072");
//	            createPortInRequest("9057160072", "A", true , sub);
//	            sub.restore(activityDate, "PR","", "W", null);
//	          //  PCSSubscriber sub1 = (PCSSubscriber)account.getSubscriber("4164160010");
//	          //  sub1.restore(activityDate, "CRQ","", "I");
//	            
//	            System.out.println("End Resume or Restore Ported In Subscriber" );
//			
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("resumeOrRestorePortedInSubscriber End...");
//		}	
//		
//		
//		
//		private static void searchSubscriberByAdditionalMsiSdn(){
//			System.out.println("searchSubscriberByAdditionalMsiSdn Start...");
//			
//			try {
//	            Hashtable env = new Hashtable();
//	    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//	    		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//
//	    		javax.naming.Context context = new javax.naming.InitialContext(env);
//
//	    		SubscriberManagerEJBHome subManagerEJBHome = (SubscriberManagerEJBHome) context.lookup("telus-eca/ejb/stateful/SubscriberManager");
//	    		SubscriberManagerEJBRemote subscriberManagerEJB = (SubscriberManagerEJBRemote)subManagerEJBHome.create("18654", "apollo", "OL");
//       		SearchResultByMsiSdn ret = subscriberManagerEJB.searchSubscriberByAdditionalMsiSdn("4166760321");
//	    		System.out.println("SearchResultByMsiSdn = " + ret );
//			
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("searchSubscriberByAdditionalMsiSdn End...");
//		}	
//		
//		
//		
//		private static void cancelAdditionalMsisdn(){
//			System.out.println("cancelAdditionalMsisdn Start...");
//			
//			try {
//	            Hashtable env = new Hashtable();
//	    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//	    		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//
//	    		javax.naming.Context context = new javax.naming.InitialContext(env);
//
//	    		SubscriberManagerEJBHome subManagerEJBHome = (SubscriberManagerEJBHome) context.lookup("telus-eca/ejb/stateful/SubscriberManager");
//	    		SubscriberManagerEJBRemote subscriberManagerEJB = (SubscriberManagerEJBRemote)subManagerEJBHome.create("18654", "apollo", "OL");
//	    		AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo = new AdditionalMsiSdnFtrInfo[1];
//	    		additionalMsiSdnFtrInfo[0] = new AdditionalMsiSdnFtrInfo();
//	    		additionalMsiSdnFtrInfo[0].setBan(20007096);
//	    		additionalMsiSdnFtrInfo[0].setSubscriberNumber("M000002768");
//              additionalMsiSdnFtrInfo[0].setSoc("MSFAXM");
//	    		additionalMsiSdnFtrInfo[0].setFeature("MFAXM");
//	    		additionalMsiSdnFtrInfo[0].setSocSeqNo(178466899);
//	    		additionalMsiSdnFtrInfo[0].setFtrSocVerNo(0);
//	    		additionalMsiSdnFtrInfo[0].setServFtrSeqNo(997221356);
//	    		Date tmpDate = java.sql.Date.valueOf("2006-08-21");
//	    		additionalMsiSdnFtrInfo[0].setFtrEffDate(tmpDate);
//	    		additionalMsiSdnFtrInfo[0].setFtrExpDate(tmpDate);
//	    		//additionalMsiSdnFtrInfo[0].setFtrEffDate(new Date("Mon Aug 21 00:00:00 EDT 2006"));
//	    		//additionalMsiSdnFtrInfo[0].setFtrExpDate(new Date("Mon Aug 21 00:00:00 EDT 2006"));
//	    	    additionalMsiSdnFtrInfo[0].setFtrTrxId(61538795);
//	    		subscriberManagerEJB.cancelAdditionalMsisdn(additionalMsiSdnFtrInfo, "4166760321");
//			
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("cancelAdditionalMsisdn End...");
//		}
//		
//		
//		private static void deleteMsisdnFeature(){
//			System.out.println("deleteMsisdnFeature Start...");
//			
//			try {
//	            System.out.println("Start Delete Msisdn Feature" );
//	        	
//	            Hashtable env = new Hashtable();
//	    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
//	    		env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//
//	    		javax.naming.Context context = new javax.naming.InitialContext(env);
//
//	    		SubscriberManagerEJBHome subManagerEJBHome = (SubscriberManagerEJBHome) context.lookup("telus-eca/ejb/stateful/SubscriberManager");
//	    		SubscriberManagerEJBRemote subscriberManagerEJB = (SubscriberManagerEJBRemote)subManagerEJBHome.create("18654", "apollo", "OL");
//	    		
//	    		AdditionalMsiSdnFtrInfo additionalMsiSdnFtrInfo = new AdditionalMsiSdnFtrInfo();
//	    		additionalMsiSdnFtrInfo.setBan(10000184);
//	    		additionalMsiSdnFtrInfo.setSubscriberNumber("M001878550");
//              additionalMsiSdnFtrInfo.setSoc("FAXSOC");
//	    		additionalMsiSdnFtrInfo.setFeature("MFAXM");
//	    		Date tmpDate = java.sql.Date.valueOf("2006-08-21");
//	    		additionalMsiSdnFtrInfo.setFtrEffDate(tmpDate);
//		
//	            subscriberManagerEJB.deleteMsisdnFeature(additionalMsiSdnFtrInfo);  
//	            System.out.println("End Delete Msisdn Feature" );
//			
//			} catch(Exception e) {
//	            System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
//	            e.printStackTrace();
//	        }
//			System.out.println("deleteMsisdnFeature End...");
//		}
//}
