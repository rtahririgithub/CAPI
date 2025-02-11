//package com.telus.cmb.account.informationhelper.dao.impl;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import java.util.Calendar;
//import java.util.Date;
//
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//
//import com.telus.api.ApplicationException;
//import com.telus.api.SystemCodes;
//import com.telus.api.SystemException;
//import com.telus.api.reference.ChargeType;
//import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
////import com.telus.cmb.account.informationhelper.dao.LetterDao;
////import com.telus.eas.account.info.LMSLetterRequestInfo;
//import com.telus.eas.account.info.SearchResultsInfo;
//
//@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
//public class LetterDaoImplIntTest extends BaseInformationHelperIntTest {
//
//	@Autowired
////	LetterDao dao;
//	Integer BANwithLetters = 8;
//	String subWithLetters = "4038579327";
//	
////	@Test
////	public void testRetrieveCollectionHistoryInfo() {
////		Date fromDate = null;
////		Date toDate = null;
////		
////		try {
////			dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, 'A', null, 0);
////			fail("Null dates should throw exception");			
////		} catch (SystemException e) {
////			assertEquals(SystemCodes.CMB_AIH_DAO, e.getSystemCode());
////		} catch (ApplicationException e) {
////			fail("Null dates should not throw ApplicationException");
////		}
////		
////		toDate = new Date();
////		try {
////			dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, 'A', null, 0);
////			fail("Null dates should throw exception");
////		} catch (SystemException e) {
////			assertEquals(SystemCodes.CMB_AIH_DAO, e.getSystemCode());
////		} catch (ApplicationException e) {
////			fail("Null dates should not throw ApplicationException");
////		}
////
////		Calendar cal = Calendar.getInstance();
////		cal.set(1990, 0, 1);
////		fromDate = cal.getTime();
////		toDate = new Date();
////		try {
////			// maximum = 0, returns maximum 1000 results defined in stored proc 
////			SearchResultsInfo searchResultsInfo = dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, 'Z', null, 0);
////			assertEquals(17, searchResultsInfo.getCount());
////			
////			// maximum = 12, returns exactly 12
////			searchResultsInfo = dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, 'Z', null, 12);
////			assertEquals(12, searchResultsInfo.getCount());
////
////			// toDate = fromDate, returns no records 
////			toDate = fromDate;
////			searchResultsInfo = dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, 'Z', null, 0);
////			assertEquals(0, searchResultsInfo.getCount());
////
////			// search letters for a specific subscriber
////			searchResultsInfo = dao.retrieveLetterRequests(BANwithLetters, fromDate, toDate, ChargeType.CHARGE_LEVEL_SUBSCRIBER, subWithLetters, 0);
////			assertEquals(2, searchResultsInfo.getCount());
////			
////			LMSLetterRequestInfo lrInfo[] = (LMSLetterRequestInfo[]) searchResultsInfo.getItems();
////			if (lrInfo[0].getId() == 4576) {
////				assertEquals("WEL", lrInfo[0].getLetterCategory());
////				assertEquals("WWSF", lrInfo[0].getLetterCode());
////			} else {
////				assertEquals(4577, lrInfo[1].getId());				
////				assertEquals("REE", lrInfo[1].getLetterCategory());
////				assertEquals("WWSF", lrInfo[1].getLetterCode());
////			}
////		} catch (ApplicationException e) {
////			fail("Should not throw ApplicationException");
////		}		
////	}
//}
