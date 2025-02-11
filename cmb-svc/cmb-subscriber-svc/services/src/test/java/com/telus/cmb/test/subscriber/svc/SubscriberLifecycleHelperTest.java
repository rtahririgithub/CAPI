/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.svc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations = "classpath:application-context-test.xml")
//@ActiveProfiles({ "remote", "pt140" })
//@ActiveProfiles({"remote", "prb"})
@ActiveProfiles("standalone")
public class SubscriberLifecycleHelperTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("weblogic.Name", "standalone");
	}

	@Autowired
	private SubscriberLifecycleHelper helper;

	@Test
	public void retrieveSubscriber() throws Exception {
		System.out.println("hello");
		SubscriberInfo subscriber = helper.retrieveSubscriber("4160717530");
		System.out.println(subscriber);
	}

	@Test
	public void retrieveLastMemo() throws Exception {
		System.out.println("begin retrieveLastMemo");
		MemoInfo memo = helper.retrieveLastMemo(34550773, "5148841285", "TOWN");
		System.out.println(memo);
		System.out.println("end retrieveLastMemo");
	}
	@Test
	public void dateValidation() throws Exception {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 10);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		System.out.println(cal.getTimeZone());
		System.out.println(cal.getTime());
		System.out.println(cal.getTimeInMillis());
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(cal.getTime());

		XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		System.out.println(gregorianCalendar.getTime());
		System.out.println(gregorianCalendar.getTimeInMillis());
	}

	@Test
	public void getPhoneDirectory() throws Exception {
		PhoneDirectoryEntry[] entries = helper.getPhoneDirectory(8369289);
		System.out.println(entries);
	}

	@Test
	public void retrieveFeaturesForPrepaidSubscriber() throws Exception {
		ServiceAgreementInfo[] agreements = helper.retrieveFeaturesForPrepaidSubscriber("4160715847");
		System.out.println(agreements);
	}

	@Test
	public void retrieveVoiceUsageSummary() throws Exception {
		VoiceUsageSummaryInfo result = helper.retrieveVoiceUsageSummary(70627629, "7781660608", "STD");
		System.out.println(result.getPhoneNumber());
	}
	
	@Test
	public void retrieveSubscriberTest() throws Exception {
		System.out.println("start retrieveSubscriber");
		AddressInfo result = helper.retrieveSubscriberAddress(70813261, "4161536659");	
		System.out.println("Address info"+result);
		System.out.println("end retrieveSubscriber");

	}
	
	@Test
	public void retrieveLightWeightSubscriberListInSameBan() throws Exception {
		List<LightWeightSubscriberInfo> result = helper.retrieveLightWeightSubscriberListInSameBan(70801860	, new String[]{"4160619537",""});
		System.out.println(result.size());
	}
	
	@Test
	public void retrieveCommunicationSuite() throws Exception {
		System.out.println("start retrieveCommunicationSuite");
		CommunicationSuiteInfo info = helper.retrieveCommunicationSuite(70922766, "4161966329", CommunicationSuiteInfo.CHECK_LEVEL_ALL);
		System.out.println(info);
		System.out.println(info.getActiveAndSuspendedCompanionCount());
		System.out.println(info.getActiveCompanionCount());
		System.out.println(info.getCancelledCompanionCount());
		System.out.println(info.getPrimaryPhoneNumber());
		System.out.println(info.getSuspendedCompanionCount());
		List<String> allCompanionList = info.getCompanionPhoneNumberList();
		for (String companion : allCompanionList) {
			System.out.println(companion);
		}
		System.out.println("end retrieveCommunicationSuite");
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void retrieveSubscriberListByPhoneNumbers() throws Exception {

		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
		phoneNumberSearchOptionInfo.setSearchVOIP(true);
		long startTime = System.currentTimeMillis();
		Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumbers(new String[]{"6474568519"}, true); //21353
		//Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumbers(new String[]{"5871263217"}, true); // 5265
		//Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumbers(new String[]{"4160718029"}, true); // 

		long endTime = System.currentTimeMillis();
		System.out.println("total time"+(endTime-startTime));

		for (SubscriberInfo info : subscriberList) {
			System.out.println(info.getBanId());
		}
	}
	
	
	@Test
    public void retrieveSubscriberListByPhoneNumbersLoad() throws Exception {
           int loops = 200;
           ReadFileFromClassPath reader = new ReadFileFromClassPath();
           InputStream is = reader.openFile("/data.txt");
           BufferedReader in = new BufferedReader(new InputStreamReader(is));
           String line = null;

           final List<String> testData = new ArrayList<String>();
           Map<String, String> dataMap = new HashMap<String, String>();
           
           while ((line = in.readLine()) != null) {
                  if (line.trim().isEmpty() == false) {
                        testData.add(line);
                        
                  }
           }
           in.close();
           is.close();
           
           for (int i = 0 ; i < loops; i++) {
                  final int threadIndex = i;
                  Thread t = new Thread() {

                        /* (non-Javadoc)
                        * @see java.lang.Thread#run()
                        */
                        public void run() {
                               Random r = new Random();
                               int randomIndex = r.nextInt(testData.size());
                               String phoneNumber = "6476844981";
                               try {
       							Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumbers(new String [] { phoneNumber}, true);
                                      System.out.println("Thread  completed for phoneNumber "+phoneNumber);
                               } catch (ApplicationException e) {
                                      // TODO Auto-generated catch block
                                      e.printStackTrace();
                               }

                        }
                        
                  };
                  
                  t.start();
           }
           
           Thread.sleep(100L * 1000L);
    }


	
	@Test
	public void retrieveSubscriberListByPhoneNumberNew() throws Exception {
		for (int i = 0 ; i < 100; i++) {
            Thread t = new Thread() {

                  /* (non-Javadoc)
                  * @see java.lang.Thread#run()
                  */
                  public void run() {
                 		 try {
                 			 Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumbersPkgRuleHint(new String [] { "4161330424"}, true);

							for (SubscriberInfo info : subscriberList) {
								System.out.println("BanId "+info.getBanId());
							}
						} catch (ApplicationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}           

                  }
                  
            };
            
            t.start();
     }
     
     Thread.sleep(100L * 1000L);
}
	
	@Test
	public void retrieveSubscriberAddress() throws Exception {
		System.out.println("retrieveSubscriberAddress start...");
		SubscriberInfo subscriberInfo = helper.retrieveSubscriber(70779714, "4160500059");
		if (subscriberInfo.getAddress() == null) {
			subscriberInfo.setAddress(helper.retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId()));
		}
		System.out.println(subscriberInfo.getAddress());
		System.out.println("retrieveSubscriberAddress end.");
	}
	
	
	// WRP Phase 3 - Inline Sql changes testing Begin.
	
	@Test
	public void retrieveSubscriberByPhoneNumber_Wireless_VOIP() throws Exception {
		long startTime = System.currentTimeMillis();
		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
		phoneNumberSearchOptionInfo.setSearchVOIP(true);
		phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
		String voip_number = "5876010188";
		String wireless_number = "4160602685";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(wireless_number, phoneNumberSearchOptionInfo); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		System.out.println("SubscriberId = " +subscriberInfo.getSubscriberId() +" , Ban = "+subscriberInfo.getBanId() +", Status =  "+subscriberInfo.getStatus());
	}
	
	
	
	@Test
	public void retrieveSubscriberListByPhoneNumber_Wireless_VOIP() throws Exception {
		long startTime = System.currentTimeMillis();
		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo = new PhoneNumberSearchOptionInfo();
		phoneNumberSearchOptionInfo.setSearchVOIP(true);
		phoneNumberSearchOptionInfo.setSearchWirelessNumber(false);
		String voip_number = "6474905168";
		//String wireless_number = "4036202482";
		Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumber(voip_number, phoneNumberSearchOptionInfo,100,true); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		for (SubscriberInfo subscriberInfo : subscriberList) {
			System.out.println("subscriberId = "+ subscriberInfo.getSubscriberId() + " , Ban = "+ subscriberInfo.getBanId() + ", Status =  "+ subscriberInfo.getStatus());
		}
	}

	@Test
	public void retrieveSubscriber_Wireless_Latest_Subscriber() throws Exception {
		long startTime = System.currentTimeMillis();
		String cancelled_wireless_number = "5871723205";
		String wireless_number = "3682010096";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriber(cancelled_wireless_number); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		System.out.println("SubscriberId = " +subscriberInfo.getSubscriberId() +" , Ban = "+subscriberInfo.getBanId() +", Status =  "+subscriberInfo.getStatus());
	}
	
	@Test
	public void retrieveSubscriberByPhoneNumber_Wireless_Latest_ActiveOrSuspend_Subscriber() throws Exception {
		long startTime = System.currentTimeMillis();
		String wireless_number = "3682010096";
		String cancelled_wireless_number = "5871723205";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(wireless_number); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		System.out.println("SubscriberId = " +subscriberInfo.getSubscriberId() +" , Ban = "+subscriberInfo.getBanId() +", Status =  "+subscriberInfo.getStatus());
	}
	
	@Test
	public void retrieveSubscriberListByPhoneNumber_By_ActiveSubscriberOrCancelled_SubscriberList() throws Exception {
		long startTime = System.currentTimeMillis();
		String wireless_number = "4036202848";
		String cancelled_wireless_number = "4036202482"; // TODO : DB returns 6 , but ejb only returns 4.
		Collection<SubscriberInfo> subscriberList = helper.retrieveSubscriberListByPhoneNumber(cancelled_wireless_number,0,true); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		for (SubscriberInfo subscriberInfo : subscriberList) {
			System.out.println("subscriberId = " +subscriberInfo.getSubscriberId() +" , Ban = "+subscriberInfo.getBanId() +", Status =  "+subscriberInfo.getStatus());

		}
	}

	@Test
	public void retrieveLatestSubscriberByPhoneNumber_BySubStatusDate() throws Exception {
		long startTime = System.currentTimeMillis();
		String wireless_number = "4036202482";
		String cancelled_wireless_number = "5871723205";
		SubscriberInfo subscriberInfo = helper.retrieveLatestSubscriberByPhoneNumber(cancelled_wireless_number); 
		System.out.println("total time"+(System.currentTimeMillis()-startTime));
		System.out.println("SubscriberId = " +subscriberInfo.getSubscriberId() +" , Ban = "+subscriberInfo.getBanId() +", Status =  "+subscriberInfo.getStatus());
	}
	
	@Test
	public void getPortProtection() throws Throwable {
		int ban = 21826549;
		String subscriberId = "4163996252";
		String phoneNumber = subscriberId;
		String status = "C";
		
		String ppInd = helper.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
		System.out.println(ppInd);
	}
	
}