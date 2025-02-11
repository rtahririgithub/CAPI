package com.telus.provider.portability;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestManager;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.provider.account.TMAccountManager;

public class TMPortRequestTest extends BaseTest {

	private TMAccountManager accountManager;
	private TMPortRequestManager portRequestManager;
	String avalonErrorCode = "AV100051";
	int banToTest =12474;
	
	static {
		setupLocalhostWithCSI();
		//setupINTECA_CSI();
		//setupEASECA_QA();
		//setupCHNLECA_STG();
		/*System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");*/
		
	/*	System.setProperty("cmb.services.AccountManagerEJBRemote.url", "t3://localhost:7002");
		System.setProperty("cmb.services.AccountHelperEJBRemote.url", "t3://localhost:7002");
		
		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
		System.setProperty("cmb.services.ReferenceDataFacade.usedByProvider", "true");
		System.setProperty("cmb.services.ReferenceDataHelper.usedByProvider", "true");

		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");
		
		System.setProperty("cmb.services.AccountManagerEJBRemote.usedByProvider", "true");
		System.setProperty("cmb.services.AccountHelperEJBRemote.usedByProvider", "true");
*/
		
		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
	}
	
	public TMPortRequestTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		portRequestManager = (TMPortRequestManager)super.provider.getPortRequestManager();
	}
	
	public void testPortInEligibility() throws UnknownBANException, TelusAPIException{
		try{
			PortInEligibility result = portRequestManager.testPortInEligibility("5871901868", PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2C, 1);
			System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testActivatePortInRequest() throws UnknownBANException, TelusAPIException{
		try{
			
		//	PortRequestInfo[] portRequest = accountManager.getProvider().getCurrentPortRequestsByBan(70615536);

              

			String phNum = "6471221013"; // TELUS Active PhNum in KB
			PCSAccount account = (PCSAccount)accountManager.findAccountByBAN(70719676);
			PCSSubscriber subscriber = account.newPCSSubscriber("900099989667114", false, "en");
			
			PortRequestInfo portRequestInfo = createPortInRequest(phNum, "A", true , subscriber);
			TMPortRequest tmPortRequest = new TMPortRequest(provider, portRequestInfo);
			tmPortRequest.validate();
			tmPortRequest.activate();
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public void isValidPortInRequest() throws UnknownBANException, TelusAPIException{
		 PortRequestInfo portRequest = new PortRequestInfo();
	   	  TMPortRequest tmPortRequest = null;
		try{
		//	tmPortRequest = (TMPortRequest)subscriber.newPortRequest(phoneNumber,NPDirectionIndicator, prePopulate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private static PortRequestInfo createPortInRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate, PCSSubscriber subscriber) throws PRMSystemException,  TelusAPIException {
	   	  
	   	  PortRequestInfo portRequest = new PortRequestInfo();
	   	  TMPortRequest tmPortRequest = null;
	      try {
	    	  tmPortRequest = (TMPortRequest)subscriber.newPortRequest(phoneNumber,NPDirectionIndicator, prePopulate);
	    	  portRequest = tmPortRequest.getDelegate();
	    	  portRequest.setPortRequestId(""); 
	    	     
	          portRequest.setOSPAccountNumber(Integer.toString(subscriber.getAccount().getBanId()));
	          portRequest.setOSPSerialNumber("900099989667114");
	          portRequest.setOSPPin("1111");
	          portRequest.setAlternateContactNumber("416527881");
	          portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2012-03-22"));
	          portRequest.setAgencyAuthorizationName("ASDF");
	          portRequest.setAgencyAuthorizationIndicator("Y");
	          portRequest.setAutoActivate(false);
	          //portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
	          portRequest.setPhoneNumber(phoneNumber);
	          portRequest.setRemarks("");
	          portRequest.setBanId(subscriber.getAccount().getBanId());
	          portRequest.setCanBeActivate(true);
	          portRequest.setCanBeCancel(true);
	          portRequest.setCanBeModify(true);
	          portRequest.setCanBeSubmit(true);
	          portRequest.setCreationDate(java.sql.Date.valueOf("2015-02-19"));
	          portRequest.setIncomingBrandId(3);
	          portRequest.setOutgoingBrandId(1);

	          
	          if (!prePopulate){
	        	  Account  account = subscriber.getAccount();
				  portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
				  portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);
			  
				  portRequest.setAlternateContactNumber(account.getOtherPhone());
				  portRequest.setPhoneNumber(phoneNumber);
				  portRequest.setPortDirectionIndicator(NPDirectionIndicator);
	          }
	       }catch(Exception e) {
	    		  throw new TelusAPIException(e);
	       }
	       System.out.println("N E W     P O R T     I N     R E Q U E S T");
	       System.out.println("\n portRequest = " + portRequest);
	       return portRequest;
	     }
}
