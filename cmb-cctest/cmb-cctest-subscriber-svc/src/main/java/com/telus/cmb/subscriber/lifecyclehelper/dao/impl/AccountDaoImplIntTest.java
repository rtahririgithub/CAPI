package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.subscriber.info.SubscriberInfo;

public class AccountDaoImplIntTest extends BaseLifecycleHelperIntTest{

	@Autowired
	AccountDaoImpl dao;
	
	@Test
	public void testRetrieveBanForPartiallyReservedSub(){
		SubscriberInfo subscriberInfo= new SubscriberInfo();
		subscriberInfo=dao.retrieveBanForPartiallyReservedSub("4164160869");
		assertEquals("4164160869",subscriberInfo.getSubscriberId());
		assertEquals('A',subscriberInfo.getStatus());
		assertEquals(99999991,subscriberInfo.getBanId());
		subscriberInfo=dao.retrieveBanForPartiallyReservedSub("1221");
		assertEquals(0,subscriberInfo.getBanId());
		
	}

	@Test
	public void testRetrieveBanIdByPhoneNumber(){
		assertEquals(25,dao.retrieveBanIdByPhoneNumber("M000000021"));
		assertEquals(25,dao.retrieveBanIdByPhoneNumber("905*131072*3"));
		assertEquals(0,dao.retrieveBanIdByPhoneNumber("2143321"));
		
	}
}
