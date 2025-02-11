package com.telus.cmb.utility.queueevent.svc.impl;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.utility.queueevent.svc.QueueEventManager;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context-datasources.xml", "classpath:application-context-dao-utility-queueevent.xml",
		"classpath:com/telus/cmb/utility/queueevent/svc/impl/application-context-svc-local.xml" })
public class QueueEventManagerImplTest {

	@Autowired
	QueueEventManager qemEjb;
	static {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}

	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	@Test
	public void getEvent() throws Throwable {
		
		long connectionId = 90917900637146389L;
		QueueThresholdEventInfo queueInfo = qemEjb.getEvent(connectionId);
		System.out.println(queueInfo);
	}

	@Test
	public void getEvents() throws Throwable {
		long subscriptionId = 5071856L;
		Date fromDate = getDateInput(2001, 3, 3);
		Date toDate = getDateInput(2011 , 1, 1);
		QueueThresholdEventInfo[] queueInfos = qemEjb.getEvents(subscriptionId, fromDate, toDate);
		for (QueueThresholdEventInfo queueInfo : queueInfos) {
			System.out.println(queueInfo);
		}
	}

	@Test
	public void updateEvent() throws Throwable {
		long interactionId = 327L;
		long subscriptionId = 0L;
		String phoneNumber = "7808376881";
		int teamMemberId = 0;
		int userId = 18654;

		qemEjb.updateEvent(interactionId, subscriptionId, phoneNumber, teamMemberId, userId);
	}
}
