package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.AddressInfo;

public class AddressDaoImplIntTest extends BaseLifecycleHelperIntTest{

	@Autowired
	AddressDaoImpl dao;
	
	@Test
	public void testRetrieveSubscriberAddress(){
		AddressInfo addressInfo= new AddressInfo();
		addressInfo= dao.retrieveSubscriberAddress(8,"4033404108");
		assertEquals("PO BOX 5775 STN MAIN",addressInfo.getPrimaryLine());
		assertEquals("5775",addressInfo.getRrIdentifier());
		addressInfo=dao.retrieveSubscriberAddress(121,"4033404108");
		assertEquals(null,addressInfo);
	}
	
}
