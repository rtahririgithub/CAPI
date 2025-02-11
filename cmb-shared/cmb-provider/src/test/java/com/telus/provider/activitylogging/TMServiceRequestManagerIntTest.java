package com.telus.provider.activitylogging;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Address;
import com.telus.api.account.Contract;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.servicerequest.TelusServiceRequestException;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.account.TMContract;
import com.telus.provider.servicerequest.TMServiceRequestManager;

public class TMServiceRequestManagerIntTest extends BaseTest {

	private static String dealerCode = null;
	private static String salesRepCode = null;
	private static String userId = null;
	static {
//		setupD3();
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
		setupEASECA_QA();
		dealerCode = "A001000001";
		salesRepCode = "0000";
		userId = "18654";
//		System.setProperty("cmb.services.ActivityLoggingService.url", "t3://localhost:7001");
		
	}

	public TMServiceRequestManagerIntTest(String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;
	private TMServiceRequestManager serviceRequestManager;
	private TestTMProvider testTMProvider;

	public void setUp() throws Exception{
		super.setUp();

		accountManager = super.provider.getAccountManager0();
		serviceRequestManager = (TMServiceRequestManager) super.provider.getServiceRequestManager();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
	}

	public void testReportChangeAccountAddressActivity() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 17605;
		Address address = new AddressInfo();
		address.setProvince("ON");
		address.setCity("TORONTO");
		address.setPostalCode("M1M1M1");
		address.setStreetName("CONSILIUM PL.");
		address.setStreetNumber("300");
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);		
		serviceRequestManager.reportChangeAccountAddress(banId, dealerCode, salesRepCode, userId, address, header);
	}

//	public void test() throws Exception
//	{
//		testReportChangeAccountPin();
//		testReportChangePaymentMethod();
//		testReportChangeAccountType();
//	}
	public void testReportChangeAccountPin() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 17605;
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);		
		serviceRequestManager.reportChangeAccountPin(banId, dealerCode, salesRepCode, userId, header);
	}
	
	public void testReportChangePaymentMethod() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 17605;
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		PaymentMethod paymentMethod = new PaymentMethodInfo();
		paymentMethod.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_REGULAR);
		serviceRequestManager.reportChangePaymentMethod(banId, dealerCode, salesRepCode, userId, paymentMethod, header);
	}	
	
	public void testReportChangeAccountType() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 17605;
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		serviceRequestManager.reportChangeAccountType(banId, dealerCode, salesRepCode, userId, 'O', 'I', 'R', 'B', 'P', header);
	}	
	
	public void testReportChangeContract() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 292007;
		String subscriberId = "7807198318";
		
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber(subscriberId);
		
		Contract contract = subscriber.getContract();
		contract.addService("SBDOWNF0 ");
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		serviceRequestManager.reportChangeContract(banId, subscriberId, dealerCode, salesRepCode, userId, ((TMContract)contract).getDelegate(), ((TMContract)subscriber.getContract()).getDelegate(), contract.getAddedServices(), contract.getDeletedServices(), contract.getChangedServices(), contract.getChangedFeatures(), header);
	}	

	public void testReportChangePhoneNumber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 197806;
		String subscriberId = "4037109656";
		String newSubscriberId = "9059999632";
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		serviceRequestManager.reportChangePhoneNumber(banId, subscriberId, newSubscriberId, dealerCode, salesRepCode, userId, subscriberId, newSubscriberId, header);
	}	
	
	public void testReportChangeSubscriberStatus() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 292007;
		String subscriberId = "7807198318";

		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber(subscriberId);
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		//serviceRequestManager.reportChangeSubscriberStatus(banId, subscriber, dealerCode, salesRepCode, userId, subscriber.getStatus(), Subscriber.STATUS_CANCELED, "CR", new Date(), header);
	}	
	
	public void testReportMoveSubscriber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 194587;
		int newBanId = 197806;
		String subscriberId = "4037109998";

		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber(subscriberId);
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		serviceRequestManager.reportMoveSubscriber(banId, newBanId, subscriberId, dealerCode, salesRepCode, 
				userId, subscriberId, subscriber.getStatus(), new Date(), "CR", header);
	}	
		
	public void testReportChangeEquipment() throws TelusAPIException, SecurityException, NoSuchMethodException {
		int banId = 292007;
		String subscriberId = "7807198318";
		String newSerialNumber = "10102312000";
		Equipment newEquipment = super.provider.getEquipmentManager0().getEquipment(newSerialNumber);

		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber(subscriberId);
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		serviceRequestManager.reportChangeEquipment(banId, subscriberId, 
				dealerCode, salesRepCode, userId, subscriber.getEquipment(), newEquipment, null, Equipment.SWAP_TYPE_REPLACEMENT, null, null, header);
	}	
	
	/* only language code and application id are mandatory */
	private ServiceRequestHeader newServiceRequestHeader(String languageCode, long applicationId, String referenceNumber, ServiceRequestParent parentRequest, ServiceRequestNote note) throws TelusServiceRequestException {
		ServiceRequestHeaderInfo headerInfo = new ServiceRequestHeaderInfo();
		
		if (languageCode == null || languageCode.equals(""))
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setLanguageCode(languageCode);
		
		if (applicationId <= 0)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setApplicationId(applicationId);
		
		headerInfo.setReferenceNumber(referenceNumber);
		headerInfo.setServiceRequestParent(parentRequest);
		headerInfo.setServiceRequestNote(note);
		
		return headerInfo;
	}	

}
