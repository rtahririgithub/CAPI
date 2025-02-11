package com.telus.api.account;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMPCSSubscriber;
import com.telus.provider.account.TMSubscriber;

public class TestRCMService_HolbornW2 extends BaseTest {
	
	public TestRCMService_HolbornW2(String name) throws Throwable {
		super(name);
	}


	private static ClientAPI api = null;
	private static TMProvider provider=null;
	private static SubscriberLifecycleFacade subscriberLifeCycleFacade=null;
	 

	 static {
		 setupEASECA_QA();
	//	setupEASECA_PT168();
//		setupD3();
//		overrideEjbUrl(SubscriberLifecycleFacade.class, "t3://localhost:7001");
		
		try {

			System.out.println("Getting instance of ClientAPI...");
			api = ClientAPI.getInstance("18654","apollo","ClientAPITest");
			provider=(TMProvider)api.getProvider();
			subscriberLifeCycleFacade=provider.getSubscriberLifecycleFacade();

			Thread.sleep(15000);     
			System.out.println(new Date() + "All done!");

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			//api.destroy();
		}
		
    }
	 
	 protected static String getJdk14SimpleName(Class businessInterface) {
	    	String longName = businessInterface.getName();
	    	return longName.substring(longName.lastIndexOf(".") + 1);		 
	 }

    protected static void overrideEjbUrl(Class businessInterface, String url) {
		if (url != null) {
			System.setProperty("cmb.services." +  getJdk14SimpleName(businessInterface) + ".url", url);
		}
	}
    
    public void testAll()throws Throwable {
		testAssignTNResources();
		testChangeTN(); 
		testRetrieveTNProvisionAttributes();
		testReleaseTNResources();
		
		testReservePCSSubscriber();
		testUnreservePCSSubscriber();
		testActivatePCSSubscriber();
		testSuspendPCSSubscriber();
		testCancelSubscriber();
		testRestoreSubscriber();
		
		testChangePhoneNumber();
		testGetMIN();
		testChangeEquipment();
		
	}
    
    
    
    

    public void testAssignTNResources() throws Throwable {
    	System.out.println("***** START OF TEST RUN : Assign TN Resources****");
    	
       String phoneNumber="4033946834";
  	   String networkType="C";
  	   String  localIMSI="";
  	   String remoteIMSI="";
    	
    	subscriberLifeCycleFacade.assignTNResources(phoneNumber, networkType, localIMSI, remoteIMSI);
    	System.out.println("***** END OF TEST RUN ****");
    }
    
    public void testChangeTN() throws Throwable {
    	System.out.println("***** START OF TEST RUN : Change TN****");
    	
		String oldPhoneNumber="4037109998";
		String newPhoneNumber="4037109998";
		String networkType="C";
		
		subscriberLifeCycleFacade.changeTN(oldPhoneNumber, newPhoneNumber, networkType);
		System.out.println("***** END OF TEST RUN ****");
		
	}
	
	

	public void testRetrieveTNProvisionAttributes() throws Throwable {
		System.out.println("***** START OF TEST RUN : Retrieve TN Provision Attributes****");
	
    	String phoneNumber="4037109998";
    	String networkType="C";
    	String min = "";
		
    	Object result=subscriberLifeCycleFacade.retrieveTNProvisionAttributes(phoneNumber, networkType) ;
		min = (String) result;
		
		System.out.println("MIN : "+ min);
		System.out.println("***** END OF TEST RUN ****");
	}
	

	public void testReleaseTNResources() throws Throwable {
		System.out.println("***** START OF TEST RUN : Release TN Resources****");
		   subscriberLifeCycleFacade.releaseTNResources("4037109998", "C") ;
		System.out.println("***** END OF TEST RUN ****");
	}
	
	
	
	public void testActivatePCSSubscriber() throws Throwable {
    	
		System.out.println("***** START OF TEST RUN : Activate PCS Subscriber****");
		 PCSSubscriber subscriber = (PCSSubscriber)api.getAccountManager().findSubscriberByPhoneNumber("5871723636");
		 subscriber.activate("CAP1"); //CA4 , CAO, CAP1, CAPO - from table csm_status_act ,soc_activity_reason_code 
		System.out.println("***** END OF TEST RUN ****");

    }
	
	public void testUnreservePCSSubscriber() throws Throwable {
		System.out.println("***** START OF TEST RUN : Unreserve PCS Subscriber****");
		 PCSSubscriber subscriber = (PCSSubscriber)api.getAccountManager().findSubscriberByPhoneNumber("9028020395");
		 subscriber.unreserve(true);
		 System.out.println("***** END OF TEST RUN ****");
	}
	
	public void testSuspendPCSSubscriber() throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Resume PCS Subscriber ****");
    	String phoneNumber="4033409179";
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.suspend("CNV");
//		subscriber.suspend(subscriber.getAvailableSuspensionReasons()[0].getCode());

    	System.out.println("***** END OF TEST RUN ****");
	}
	
	public void testRestoreSubscriber() throws Throwable  {
		System.out.println("***** START OF TEST RUN : Restore Subscriber  ****");
		
		String phoneNumber="4033039979";
		PCSSubscriber subscriber =(PCSSubscriber) api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.restore(new Date(), "PVR", null, "N", null) ;
		System.out.println("***** END OF TEST RUN ****");
	
	}
	
	
	
	public void testReservePCSSubscriber() throws Throwable {
    	
		System.out.println("***** START OF TEST RUN : Reserve PCS Subscriber ****");
		
		 String serialNumber="15603173774";
		 int banId_PT148=70436449;
		 PCSSubscriber subscriber = (PCSSubscriber)reserveSubscriber(banId_PT148, serialNumber);
		 activateSubscriber(subscriber);
		 System.out.println("***** END OF TEST RUN ****");

    }
	
	 public void testCancelSubscriber() throws Throwable  {
	    	System.out.println("***** START OF TEST RUN : Cancel Non HSPA Subscriber ****");
	    	String phoneNumber="4184185522";
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
			if(subscriber.isPCS())
				subscriber.cancel(subscriber.getAvailableCancellationReasons()[0].getCode(),'R');
			
			System.out.println("***** END OF TEST RUN ****");
		}	
	
	public static Subscriber reserveSubscriber(int ban, String serialNumber) throws TelusAPIException {
		
		
		String Province="NS";
		String NumberGroupCode="HFX";
		String PhoneNumberPattern="902802";
		
		Account account = api.getAccountManager().findAccountByBAN(ban);
		Subscriber reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, false, "EN");
	     NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups(Province);
	      NumberGroup numberGroup = null;
	      for (int i=0; i < numberGroups.length; i++) {
	    	  if (numberGroups[i].getCode().equals(NumberGroupCode)){
	    		  numberGroup = numberGroups[i];
	    	  break;
	    	  }
	      }		
	      PhoneNumberReservation pnr = api.getAccountManager().newPhoneNumberReservation();
	      pnr.setNumberGroup(numberGroup);
	      pnr.setPhoneNumberPattern(PhoneNumberPattern);
	      AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
	      // take first one
	      pnr.setPhoneNumberPattern(phoneNumbers[0].getPhoneNumber());
	      reservedSubscriber.reservePhoneNumber(pnr);
	      return reservedSubscriber;
	}
	
	
	public static void activateSubscriber(Subscriber subscriber) throws TelusAPIException {
	      // Setup mandatory info for save
		subscriber.setFirstName("Test");
		subscriber.setLastName("QAAT");
		subscriber.setDealerCode("A001000001");
		subscriber.setSalesRepId("0000");
		subscriber.setBirthDate(new Date(1963, 01, 01));
		subscriber.setEmailAddress("anitha.duraisamy@telus.com");
	      
	      System.out.println(" Account type is  " + 
	    		  subscriber.getAccount().getAccountType() + "/" + 
	    		  subscriber.getAccount().getAccountSubType());
	      PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
					"FETA100",
					String.valueOf(subscriber.getEquipment().getEquipmentType()),
					subscriber.getAccount().getAddress().getProvince(),
					subscriber.getAccount().getAccountType(),
					subscriber.getAccount().getAccountSubType(),
					Brand.BRAND_ID_TELUS);
	      
	      

	      Contract contract = subscriber.newContract(pricePlan, 12);
	      System.out.println("Save Subscriber");
	     
	      // Activate Subscriber
//	      restoredSubscriber.save(true);

	      // Reserve subscriber
	      subscriber.save(false);
	    
	      System.out.println("Subscriber details saved successfully");
	      
	}
	
    
	public void testChangePhoneNumber() throws Throwable {
		System.out.println("***** START OF TEST RUN : Change phone Number ****");
		
		String phoneNumber="4188917224";
		Subscriber sub = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		AvailablePhoneNumber apn = findAvailablePhoneNumber(sub);
		System.out.println ("Current Phone Number : "+sub.getPhoneNumber());
		sub.changePhoneNumber(apn, false);
		System.out.println ("New PhoneNumber : "+sub.getPhoneNumber());
		
		System.out.println("***** END OF TEST RUN ****");
	}
	
	private AvailablePhoneNumber findAvailablePhoneNumber(Subscriber subscriber) throws Throwable {
		PhoneNumberReservation reservation = api.getAccountManager().newPhoneNumberReservation();

		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(
				subscriber.getAccount().getAccountType(), 
				subscriber.getAccount().getAccountSubType(), 
				subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType()));

		NumberGroup numberGroup = null;
		AvailablePhoneNumber[] nums = null;
		for (int i = 0; i < numberGroups.length; i++) {
			if ("NS".equals(numberGroups[i].getProvinceCode())) {
				reservation.setNumberGroup(numberGroups[i]);
				reservation.setAsian(false);
				reservation.setLikeMatch(false);
				reservation.setWaiveSearchFee(true);
				reservation.setPhoneNumberPattern("902802****");
				reservation.setProductType(subscriber.getProductType());

				try {
					nums = subscriber.findAvailablePhoneNumbers(reservation, 10);
					if (nums != null) {
						for (int j = 0; j < nums.length; j++) {
							System.out.println(nums[j]);
						}
						break;
					}
				} catch (PhoneNumberException pne) {
				}
			}
		}

		if (nums == null || nums.length < 1) {
			throw new PhoneNumberException("No phone # found.");
		}else {
			return nums[0];
		}
	}
	
	
	 public void testGetMIN() throws Throwable {
			System.out.println("***** START OF TEST RUN : Get MIN for phone Number ****");
			String phoneNumber="4037109998";
			Subscriber subscriber =  api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
	    	String min= ((TMPCSSubscriber)subscriber).getMIN();
	    	System.out.println("MIN :"+min);
	    	
	    	System.out.println("***** END OF TEST RUN ****");
	    }
	 
	
	 
	 
	 public void testChangeEquipment() throws Throwable {
		 System.out.println("***** START OF TEST RUN : Change Equipment ****");
	       Subscriber  subscriber = (TMSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("2046210267");
	       
	       Equipment old =subscriber.getEquipment();
	       System.out.println("SerialNumber : "+old.getSerialNumber());
	       System.out.println("isCDMA()->"+old.isCDMA());
		   System.out.println("isHSPA ->"+old.isHSPA());
	      
	 	   Equipment newEquipment=api.getEquipmentManager().getEquipment("15603173809");
	 	    System.out.println("isCDMA()->"+newEquipment.isCDMA());
			System.out.println("isHSPA ->"+newEquipment.isHSPA());
			System.out.println("isAvailableForActivation()-> : "+newEquipment.isAvailableForActivation());
			System.out.println("isInUse()-> :"+newEquipment.isInUse());											
	 	   
	 	  subscriber.changeEquipment(newEquipment, "A001000001", subscriber.getSalesRepId(), "18654", "", Equipment.SWAP_TYPE_REPLACEMENT);
	 	  Contract contract=subscriber.getContract();
	 	  contract.save();
	 	
	 	  System.out.println("***** END OF TEST RUN ****");  
	 	  
	    }
}
