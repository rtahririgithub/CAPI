package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.AddressDao;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class AddressDaoImplIntTest extends BaseInformationHelperIntTest{
	
	@Autowired
	AddressDao dao;	
	@Autowired
	Integer sampleBAN;
	
	@Test
	public void testRetrieveAlternateAddressByBan() {
		AddressInfo addressInfo = dao.retrieveAlternateAddressByBan(sampleBAN);

		assertEquals("M5J1R7", addressInfo.getPostalCode());
		
		addressInfo = null;	
		addressInfo = dao.retrieveAlternateAddressByBan(-1);
		assertNotNull(addressInfo);
	}

	@Test
	public void testRetrieveAddressHistory() {
		AddressHistoryInfo[] addressHistoryInfo;
		Date pFromDate; 
		Date pToDate;
		Calendar cal = Calendar.getInstance();
		cal.set(1990, 1, 1);
		pFromDate = cal.getTime();
		
		pToDate = new Date();		
		addressHistoryInfo = dao.retrieveAddressHistory(sampleBAN,
				pFromDate, pToDate);		
		assertEquals(1, addressHistoryInfo.length);
		
		pFromDate = new Date();		
		addressHistoryInfo = dao.retrieveAddressHistory(sampleBAN,
				pFromDate, pToDate);		
		assertEquals(0, addressHistoryInfo.length);
		
		pFromDate = cal.getTime();
		pToDate = cal.getTime();
		addressHistoryInfo = dao.retrieveAddressHistory(sampleBAN,
				pFromDate, pToDate);		
		assertEquals(0, addressHistoryInfo.length);

		pToDate = new Date();
		addressHistoryInfo = dao.retrieveAddressHistory(123456,
				pFromDate, pToDate);		
		assertEquals(0, addressHistoryInfo.length);		
		
		int banWithMoreThanOneAddress = 8;
		addressHistoryInfo = dao.retrieveAddressHistory(banWithMoreThanOneAddress,
				pFromDate, pToDate);		
		assertEquals(12, addressHistoryInfo.length);		
	}	
}
