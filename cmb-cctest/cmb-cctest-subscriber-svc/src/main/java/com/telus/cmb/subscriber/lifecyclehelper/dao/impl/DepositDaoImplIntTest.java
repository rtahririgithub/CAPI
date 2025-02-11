package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.DepositHistoryInfo;

public class DepositDaoImplIntTest extends BaseLifecycleHelperIntTest{

	@Autowired
	DepositDaoImpl dao;
	
	@Test
	public void testRetrieveDepositHistory(){
		int ban=81;
		String subscriber="9057160005";
		List<DepositHistoryInfo> list= new ArrayList<DepositHistoryInfo>();
		list = dao.retrieveDepositHistory(ban, subscriber);
		assertEquals(1,list.size());
		for(DepositHistoryInfo depHisinf : list){
			assertEquals(200,depHisinf.getDepositPaidAmount(),0);
			assertEquals(200,depHisinf.getChargesAmount(),0);
		}
		list = dao.retrieveDepositHistory(121, "1212");
		assertEquals(0,list.size());
		list = dao.retrieveDepositHistory(121, null);
		assertEquals(0,list.size());
		
	}
	@Test
	public void testRetrievePaidSecurityDeposit(){
		int ban=81;
		String subscriber="9057160005";
		double result=0;
		result = dao.retrievePaidSecurityDeposit(ban, subscriber, "C");
		assertEquals(0,result,0);
		result = dao.retrievePaidSecurityDeposit(12, "", "C");
		assertEquals(0,result,0);
	}
}
