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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager;

/**
 * @author R. Fong
 *
 */
@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles("standalone")
@ActiveProfiles({"remote", "local"})
public class ContactEventManagerTest extends AbstractTestNGSpringContextTests {

	static {		
		System.setProperty("weblogic.Name", "standalone");
	}

	@Autowired
	private ContactEventManager contactEventManager;

	@Test
	public void convert_to_xml_gregorian_calendar() throws Exception {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date date = cal.getTime();
		System.out.println("date=[" + date + "].");
		
		// Set the calendar to midnight, mountain standard time
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.clear();
		calendar.setTimeZone(TimeZone.getTimeZone("Canada/Mountain"));
		calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		System.out.println("calendar.getTime()=[" + calendar.getTime() + "].");
		
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.clear();	
		gregorianCalendar.setTimeZone(calendar.getTimeZone());
		gregorianCalendar.setTime(calendar.getTime());
		System.out.println("gregorianCalendar.getTime()=[" + gregorianCalendar.getTime() + "].");
		
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		System.out.println("xmlGregorianCalendar=[" + xmlGregorianCalendar + "].");
		
		GregorianCalendar gregory = xmlGregorianCalendar.toGregorianCalendar();
		System.out.println("gregory.getTime()=[" + gregory.getTime() + "].");
	}
}