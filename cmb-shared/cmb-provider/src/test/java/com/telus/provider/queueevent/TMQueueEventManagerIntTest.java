package com.telus.provider.queueevent;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.QueueThresholdEvent;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.Brand;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccountManager;

public class TMQueueEventManagerIntTest extends BaseTest {

	static {
		setupEASECA_QA();
		//setupD3();
	}

	public TMQueueEventManagerIntTest(String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;
	private TMQueueEventManager queueEventManager;
	private TestTMProvider testTMProvider;

	public void setUp() throws Exception{
		super.setUp();

		accountManager = super.provider.getAccountManager0();
		queueEventManager = super.provider.getQueueEventManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
	}

	public void testCreateQueueEvent() throws TelusAPIException, SecurityException, NoSuchMethodException {
		queueEventManager.createNewEvent(1091790063714639L, "4032130201", 0, 0, "vq_koodo_techassist_all_en_natl", 500); 		
	}
	
	public void testGetQueueEvent() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("4184187281");
        long startDateMS = System.currentTimeMillis() - ((long)500) *24*60*60*1000;   
        Date startDate = new Date();   
        startDate.setTime(startDateMS);   

		QueueThresholdEvent[] events = subscriber.getQueueThresholdEvents(startDate, new Date());
		System.out.println("Events"+events.length);
		assertFalse(events.length > 0);	
		
        QueueThresholdEvent qet =  accountManager.getQueueThresholdEventbyCallCentreConnectionId(1091790063714639L);
        assertTrue(qet != null);	
	}

	public void testUpdateQueueEvent() throws TelusAPIException, SecurityException, NoSuchMethodException {
        queueEventManager.updateEvent(102, 1021917, "4032130201", 19314, 0);
	}
	
	
}
