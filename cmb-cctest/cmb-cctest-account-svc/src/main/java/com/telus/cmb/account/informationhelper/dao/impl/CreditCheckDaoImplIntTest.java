package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.eas.account.info.CreditCheckResultInfo;

public class CreditCheckDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	CreditCheckDaoImpl dao;
	
	@Test
	public void testRetrieveLastCreditCheckResultByBan() {
		assertEquals(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR, dao.retrieveLastCreditCheckResultByBan(189, "").getCreditClass());		
	}
	
	@Test
	public void testRetrieveLastCreditCheckResultByBanBadBan() {
		dao.retrieveLastCreditCheckResultByBan(0, "");
	}

}
