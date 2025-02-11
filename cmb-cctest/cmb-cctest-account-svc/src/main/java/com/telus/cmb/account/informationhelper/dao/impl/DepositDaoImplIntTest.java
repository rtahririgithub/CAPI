package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.DepositDao;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;

public class DepositDaoImplIntTest extends BaseInformationHelperIntTest{
	
	@Autowired
	DepositDao dao;

	@Test
	public void testRetrieveDepositHistory() {
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		Calendar toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2006);
		
		List<DepositHistoryInfo> list = dao.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());
		
		assertEquals(13, list.size());
		
		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2003);
		
		list = dao.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());
		
		assertEquals(4, list.size());
		
		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2010);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2011);
		
		list = dao.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());
		
		assertEquals(0, list.size());		
	}
	
	@Test
	public void testRetrieveDepositAssessdHistoryList() {
		List<DepositAssessedHistoryInfo> list = dao.retrieveDepositAssessedHistoryList(81);
		assertEquals(6, list.size());
				
		list = null;
		list = dao.retrieveDepositAssessedHistoryList(0);
		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	@Test
	public void testRetrieveOriginalDepositAssessedHistoryList() {
		assertEquals(6, dao.retrieveOriginalDepositAssessedHistoryList(20007217).size());
		assertEquals(0, dao.retrieveOriginalDepositAssessedHistoryList(-1).size());
	}

}
