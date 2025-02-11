package com.telus.api.subscriber;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestAddress;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.portability.PortRequestName;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.SeatType;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.provider.account.TMSubscriber;

public class TestWNPSubscriber extends BaseTest{

	static {
		localhostWithPT148Ldap();
		//setupCHNLECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url",
//				"t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url",
//				"t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url",
//				"t3://localhost:7001");
//
		System.setProperty("cmb.services.SubscriberLifecycleHelper.url",
				"t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url",
				"t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleManager.url",
				"t3://localhost:7001");
	}
	
	public TestWNPSubscriber(String name) throws Throwable {
		super(name);
	}
	
	public void testChangePhoneNumber() throws Throwable {
		PCSSubscriber subsscriber = (PCSSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("7781762972");
		//TMSubscriber
		//PortRequestManager portRequestManager = provider.getPortRequestManager();
		/*Setting the portinEligibility info*/
		
		PortInEligibilityInfo portinEligibility = new PortInEligibilityInfo();
		portinEligibility.setPortVisibility("EXT_2H");
		portinEligibility.setCurrentServiceProvider("TU07");
		portinEligibility.setPcsCoverage(false);
		portinEligibility.setCDMACoverage(true);
		portinEligibility.setCDMAPrepaidCoverage(true);
		portinEligibility.setHSPACoverage(true);
		portinEligibility.setHSPAPostpaidCoverage(true);
		portinEligibility.setHSPAPrepaidCoverage(true);
		portinEligibility.setIdenCoverage(true);
		portinEligibility.setPrepaidCoverage(false);
		portinEligibility.setPostPaidCoverage(false);
		portinEligibility.setPortDirectionIndicator("C");
		portinEligibility.setIncomingBrandId(1);
		portinEligibility.setOutgoingBrandId(-1);
		portinEligibility.setPhoneNumber("6136135020");
		portinEligibility.setPlatformId(1);

		subsscriber.newPortRequest("6136135020", portinEligibility.getPortDirectionIndicator(), true);
		
		PortRequest portRequest = null;
		portRequest = ((PCSSubscriber) subsscriber).getPortRequest(); //PortRequest details pls refer to the attached screenshot, portRequest.DslInd = 'E'
		// authorization name
		portRequest.setAgencyAuthorizationName("test");
		// authorization indicator
		portRequest.setAgencyAuthorizationIndicator("Y");
		portRequest.setAgencyAuthorizationDate(Calendar.getInstance().getTime());
		portRequest.setAutoActivate(true);
		/*available phone number*/
		 AvailablePhoneNumber availablePhone = null;
	     String productType = api.getReferenceDataManager().PRODUCT_TYPE_PCS;
	     availablePhone = api.getReferenceDataManager().getAvailablePhoneNumber("6136135020", productType, "A001000001");
		
		/*header*/
		ServiceRequestHeaderInfo serviceRequest = new ServiceRequestHeaderInfo();
		serviceRequest.setApplicationId(27);
		serviceRequest.setApplicationName("SMARTDESKTOP");
		serviceRequest.setLanguageCode("EN");
		
		subsscriber.changePhoneNumber(availablePhone, "CR ", false, null, null, portinEligibility, serviceRequest);
		
		//subscriber.changePhoneNumber(num, false, "0000000008", "0000", reasonCode, header)
	}
	
	
	public void testChangePhoneNumber2() throws Throwable {
		PCSSubscriber subscriber = (PCSSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("4161724670");
		PortRequest portRequest = null;
		//TMSubscriber
		//PortRequestManager portRequestManager = provider.getPortRequestManager();
		/*Setting the portinEligibility info*/
		((PCSSubscriber) subscriber).newPortRequest("6136138519", "C", true); //PortInNumber=6136138519, direction: 'C'
		portRequest = ((PCSSubscriber) subscriber).getPortRequest(); //PortRequest details pls refer to the attached screenshot, portRequest.DslInd = 'E'
		portRequest.setAlternateContactNumber("4161231234");
		// authorization name
		portRequest.setAgencyAuthorizationName("test");
		// authorization indicator
		portRequest.setAgencyAuthorizationIndicator("Y");
		// comment
		portRequest.setRemarks("");
		//wireline specific
         // address
        PortRequestAddress address = portRequest.getPortRequestAddress();
        address.setCity("scarborough");
        // country
        address.setCountry("CANADA");
        // postal code
        address.setPostalCode("M1S2Z6");
        // province
        address.setProvince("Ontario");
        // street direction
        address.setStreetDirection("");
        // street name
        address.setStreetName("lockdare st");
        // street number
        address.setStreetNumber("22");
        // name
        PortRequestName name = portRequest.getPortRequestName();
        name.setFirstName("Annie");
        // generation
        name.setGeneration(null);
        // initial
        name.setMiddleInitial("");
        // last name
        name.setLastName("XTEST");
        // title
        name.setTitle("MR");
        // business name
        portRequest.setBusinessName("XTEST");
        //dsl
        portRequest.setDslInd("E");
        portRequest.setDslLineNumber(1);
        portRequest.setEndUserMovingInd(false);
        portRequest.setOldReseller(null);
        portRequest.setOSPPin(null);
         
        /*PortInEligibility*/
        PortInEligibilityInfo portinEligibility = new PortInEligibilityInfo();
		portinEligibility.setPortVisibility("EXT_2H");
		portinEligibility.setCurrentServiceProvider("TU07");
		portinEligibility.setPcsCoverage(false);
		portinEligibility.setCDMACoverage(true);
		portinEligibility.setCDMAPrepaidCoverage(true);
		portinEligibility.setHSPACoverage(true);
		portinEligibility.setHSPAPostpaidCoverage(true);
		portinEligibility.setHSPAPrepaidCoverage(true);
		portinEligibility.setIdenCoverage(true);
		portinEligibility.setPrepaidCoverage(false);
		portinEligibility.setPostPaidCoverage(false);
		portinEligibility.setPortDirectionIndicator("C");
		portinEligibility.setIncomingBrandId(1);
		portinEligibility.setOutgoingBrandId(-1);
		portinEligibility.setPhoneNumber("6136135489");
		portinEligibility.setPlatformId(1);
        /*available phone number*/
        AvailablePhoneNumber availablePhone = null;
        String productType = api.getReferenceDataManager().PRODUCT_TYPE_PCS;
        availablePhone = api.getReferenceDataManager().getAvailablePhoneNumber("6136138520", productType, "A001000001");

		
//		subscriber.changePhoneNumber(num, "CR ", false, null, null, portinEligibility, serviceRequest);
		
		//subscriber.changePhoneNumber(num, false, "0000000008", "0000", reasonCode, header)
	}
	
	
	
	
	 
	 

	
}
