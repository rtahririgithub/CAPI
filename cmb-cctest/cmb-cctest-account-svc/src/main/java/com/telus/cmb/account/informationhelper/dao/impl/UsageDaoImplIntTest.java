package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.UsageDao;

public class UsageDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	UsageDao dao;
	
	@Test
	public void testRetrieveVoiceUsageSummary() {
		assertEquals(0, dao.retrieveVoiceUsageSummary(20007879, null).size());
	}
	@Test
	public void testRetrieveUnpaidDataUsageTotal(){
		double result = dao.retrieveUnpaidDataUsageTotal(20007181, new Date());
		assertEquals(0,result,0);
	}
	@Test
	public void testRetrieveUnpaidAirTimeTotal() throws ApplicationException {
		double result = dao.retrieveUnpaidAirTimeTotal(20007181);
		assertEquals(0,result,0);
	}
}
