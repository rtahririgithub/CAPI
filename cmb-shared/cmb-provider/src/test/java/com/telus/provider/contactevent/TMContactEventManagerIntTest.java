package com.telus.provider.contactevent;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.contactevent.Notification;
import com.telus.api.contactevent.SMSNotification;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NotificationType;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccountManager;

public class TMContactEventManagerIntTest extends BaseTest {

	static {
		setupK();
		System.setProperty("cmb.services.ConfigurationManager.url","t3://localhost:7001");
		System.setProperty("cmb.services.ActivityLoggingService.url","t3://localhost:7001");
		System.setProperty("cmb.services.ContactEventManager.url","t3://localhost:7001");
		System.setProperty("cmb.services.DealerManager.url","t3://localhost:7001");
		System.setProperty("cmb.services.QueueEventManager.url","t3://localhost:7001");

		//setupD3();
	}

	
	public TMContactEventManagerIntTest(String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;
	private TMContactEventManager contactEventManager;
	private TestTMProvider testTMProvider;

	public void setUp() throws Exception{
		super.setUp();

		accountManager = super.provider.getAccountManager0();
		contactEventManager = super.provider.getContactEventManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
	}

	public void testSendSMS() throws TelusAPIException, SecurityException, NoSuchMethodException {
		//assertEquals(70106788, ai.getBanId());
		
        Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("4167979902");

		// Prepare SMS message
		SMSNotification smsNotification = contactEventManager.newSMSNotification();
		smsNotification.setBanId(subscriber.getBanId());
		smsNotification.setProductType(subscriber.getProductType());
		smsNotification.setSubscriberNumber(subscriber.getSubscriberId());
		smsNotification.setPhoneNumber(subscriber.getPhoneNumber());
		smsNotification.setEquipmentSerialNumber(subscriber.getSerialNumber());
		smsNotification.setDeliveryDate(api.getReferenceDataManager().getSystemDate());
		smsNotification.setLanguage(Notification.LANGUAGE_ENGLISH);
		smsNotification.setPriority(Notification.PRIORITY_SMS_NORMAL);
		smsNotification.setTimeToLive(43200);    // 12 hours

		// Set template and parameters
		NotificationType notificationType = api.getReferenceDataManager().getNotificationType(NotificationType.NOTIFICATION_TYPE_FREE_INFO);
		smsNotification.setNotificationType(notificationType);
		smsNotification.setContentParameters(new String[] {"This is a test message"});

		// Send SMS message
		contactEventManager.process(smsNotification);
		
	}
	public void testlogSubscriberAuthentication1() throws TelusAPIException, SecurityException, NoSuchMethodException {		 
		contactEventManager.logSubscriberAuthentication(1, true, null, null, null);
	}
	
	public void testlogSubscriberAuthentication() throws TelusAPIException, SecurityException, NoSuchMethodException {		 
		contactEventManager.logSubscriberAuthentication("5071916", true, null, null, null);
	}
	
	public void testlogAccountAuthentication() throws TelusAPIException, SecurityException, NoSuchMethodException {		 
		contactEventManager.logAccountAuthentication(5071916,true,null,null,null);		
	}	
	public void testlogAccountAuthentication1() throws TelusAPIException, SecurityException, NoSuchMethodException {		 
		contactEventManager.logAccountAuthentication("5071916",true,null,null,null);		
	}

}
