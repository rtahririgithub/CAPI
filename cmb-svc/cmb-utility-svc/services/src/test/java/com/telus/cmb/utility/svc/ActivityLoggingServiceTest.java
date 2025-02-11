/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.utility.svc;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @author R. Fong
 *
 */
@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "pt140"})
public class ActivityLoggingServiceTest extends AbstractTestNGSpringContextTests {

	static {		
		System.setProperty("weblogic.Name", "standalone");
	}

	@Autowired
	private ActivityLoggingService activityLoggingService;
	
	@Test
	public void log_change_subscriber_status() throws Exception {
		
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(70705075);
		subscriberInfo.setSubscriberId("7781520006");
		subscriberInfo.setPhoneNumber("7781520006");
		subscriberInfo.setSubscriptionId(8370248);
		
		ServiceRequestHeaderInfo headerInfo = new ServiceRequestHeaderInfo();
		headerInfo.setLanguageCode("EN");
		headerInfo.setApplicationId(27);
		headerInfo.setApplicationName("SMARTDESKTOP");
		headerInfo.setReferenceNumber(null);
		headerInfo.setServiceRequestParent(null);
		headerInfo.setServiceRequestNote(null);
		
		ChangeSubscriberStatusActivity activity = new ChangeSubscriberStatusActivity(headerInfo);
		activity.setActors("0000", "1234", "18654");

		activity.setBanId(subscriberInfo.getBanId());
		activity.setSubscriberId(subscriberInfo.getSubscriberId());
		activity.setNewSubscriberStatus('C');
		activity.setOldSubscriberStatus('A');
		activity.setReason("");
		activity.setPhoneNumber(subscriberInfo.getPhoneNumber());
		activity.setSubscriberActivationDate(new Date());

	    activityLoggingService.logChangeSubscriberStatusActivity(activity);
	}

}