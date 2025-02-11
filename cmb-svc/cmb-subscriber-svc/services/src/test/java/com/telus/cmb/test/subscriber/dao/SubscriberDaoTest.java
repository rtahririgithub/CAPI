/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberDao;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/subscriber/dao/subscriber-context-dao-test.xml")
public class SubscriberDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private SubscriberDao dao;
	
	@Test
	public void retrieveSubscriberListByBanAndSeatResourceNumber() throws Exception {
		
		try {
			Collection<SubscriberInfo> subscriberInfoList = dao.retrieveSubscriberListByBanAndSeatResourceNumber(0, "5144189066", 100, true);
			for (SubscriberInfo subscriberInfo : subscriberInfoList) {
				System.out.println("subscriberId = "+ subscriberInfo.getSubscriberId() + " , Ban = "+ subscriberInfo.getBanId() + ", Status =  "+ subscriberInfo.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void retrieveSubscriberListByPhoneNumber() throws Exception {
		
		try {
			Collection<SubscriberInfo> subscriberInfoList = dao.retrieveSubscriberListByPhoneNumber("5871723205", 10, false);
			for (SubscriberInfo subscriberInfo : subscriberInfoList) {
				System.out.println(subscriberInfo);			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void retrieveLightWeightSubscriberListInSameBan() throws Exception {
		try {
			List<LightWeightSubscriberInfo> lightSubList = dao.retrieveLightWeightSubscriberListInSameBan(12423,new String[]{"4163996252"}); 
			System.out.println(lightSubList.size());			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void retrieveSubscriptionRole() throws Exception {
		
		try {
			SubscriptionRoleInfo roleInfo = dao.retrieveSubscriptionRole("5871072110"); // 4160710218 5141811616 7057155047 7781765451
			System.out.println(roleInfo);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void retrieveSubscriberListByPhoneNumbers() throws Exception {
		try {
			boolean includeCancelled = true;
			List<String> executionTime = new ArrayList<String>();

			for (int i = 0; i < 2; i++) {
				long startTime = System.currentTimeMillis();
				//Collection<SubscriberInfo> subInfoList = dao.retrieveSubscriberListByPhoneNumbers(new String[]{"6474568519"}, includeCancelled);
				//Collection<SubscriberInfo> subInfoList = dao.retrieveSubscriberListByPhoneNumbers(new String[]{"4161702292"}, includeCancelled);
				List<String> phoneNumbers = new ArrayList<String>();
				phoneNumbers.add("6474568519");
				Collection<SubscriberInfo> subInfoList = dao.retrieveSubscriberListByPhoneNumbers(phoneNumbers, 1000, includeCancelled);
				long endTime = System.currentTimeMillis();
				executionTime.add(endTime - startTime+"");
				//System.out.println("total time for the "  +"call  is " + (endTime - startTime));
				for (SubscriberInfo subscriberInfo : subInfoList) {
					System.out.println(subscriberInfo.getBanId());
				}
			}
			for (String time : executionTime) {
				System.out.println(time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}