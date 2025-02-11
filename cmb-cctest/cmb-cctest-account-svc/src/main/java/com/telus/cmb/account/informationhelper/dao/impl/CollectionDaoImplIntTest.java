package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.CollectionDao;
import com.telus.eas.account.info.CollectionHistoryInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class CollectionDaoImplIntTest extends BaseInformationHelperIntTest {

	@Autowired
	CollectionDao dao;
	@Autowired
	Integer sampleBAN;
	Integer BANwithCollections = 8;

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}


	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
				cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
				cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}	

	
	@Test
	public void testRetrieveCollectionHistoryInfo() {
		Date fromDate = null;
		Date toDate = null;
		
		try {
			dao.retrieveCollectionHistoryInfo(sampleBAN, fromDate, toDate);
			fail("Null dates should throw exception");			
		} catch (SystemException e) {
			assertEquals(SystemCodes.CMB_AIH_DAO, e.getSystemCode());
		} catch (ApplicationException e) {
			fail("Null dates should not throw ApplicationException");
		}
		
		toDate = new Date();
		try {
			dao.retrieveCollectionHistoryInfo(sampleBAN, fromDate, toDate);
			fail("Null dates should throw exception");
		} catch (SystemException e) {
			assertEquals(SystemCodes.CMB_AIH_DAO, e.getSystemCode());
		} catch (ApplicationException e) {
			fail("Null dates should not throw ApplicationException");
		}

		Calendar cal = Calendar.getInstance();
		cal.set(2002, 6, 1);
		fromDate = cal.getTime();
		cal.set(2002, 6, 30);
		toDate = cal.getTime();
		try {
			CollectionHistoryInfo[] collectionHistory = dao.retrieveCollectionHistoryInfo(BANwithCollections, fromDate, toDate);
			assertEquals(2, collectionHistory.length);
			assertEquals("A", collectionHistory[0].getActivityMode());
			assertEquals("A", collectionHistory[1].getActivityMode());
			assertNull(collectionHistory[0].getCollectorCode());
			assertNull(collectionHistory[1].getCollectorCode());
			assertNull(collectionHistory[0].getCollectorName());
			assertNull(collectionHistory[1].getCollectorName());
			assertNull(collectionHistory[0].getAgencyCode());
			assertNull(collectionHistory[1].getAgencyCode());
			assertEquals("AI3", collectionHistory[0].getCollectionStepInfo().getPath());
			assertEquals("AI3", collectionHistory[1].getCollectionStepInfo().getPath());
			
			cal.set(2002, 6, 24);
			Date treatmentDate = cal.getTime();
			
			if (isSameDay(collectionHistory[0].getCollectionStepInfo().getTreatmentDate(), treatmentDate)) {
				assertEquals(1, collectionHistory[0].getCollectionStepInfo().getStep());
				assertEquals("1", collectionHistory[0].getCollectionStepInfo().getCollectionActivityCode());
			} else {
				cal.set(2002, 6, 23);
				treatmentDate = cal.getTime();
				assertTrue(isSameDay(treatmentDate, collectionHistory[1].getCollectionStepInfo().getTreatmentDate()));
				assertEquals(0, collectionHistory[1].getCollectionStepInfo().getStep());
				assertEquals("E", collectionHistory[1].getCollectionStepInfo().getCollectionActivityCode());
			}
			
		} catch (ApplicationException e) {
			fail("Should not throw ApplicationException");
		}		
	}
}
