package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;

public class ProvisioningDaoImplIntTest extends BaseLifecycleHelperIntTest{
	@Autowired
	ProvisioningDaoImpl dao;
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveProvisioningTransactions(){
		List<ProvisioningTransactionInfo> provTranList = new ArrayList<ProvisioningTransactionInfo>();
		provTranList = dao.retrieveProvisioningTransactions(120045010, "7780551297", new Date(2000-1900,00,01), new Date(2010-1900,00,01));
		assertEquals(2,provTranList.size());
		for(ProvisioningTransactionInfo provIn:provTranList){
			assertEquals("36444",provIn.getTransactionNo());
			assertEquals("CS",provIn.getStatus());
			break;
		}
	}
	@Test
	public void testRetrieveSubscriberProvisioningStatus(){
		assertEquals("CE",dao.retrieveSubscriberProvisioningStatus(20070599, "9057160244"));
		assertEquals("CE",dao.retrieveSubscriberProvisioningStatus(20070334, "4168940079"));
		assertEquals(null,dao.retrieveSubscriberProvisioningStatus(1212, "12112"));
	}
		
	
}
