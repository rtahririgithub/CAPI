package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;


//public class UsageDaoImplIntTest extends BaseLifecycleHelperIntTest{
//
//	@Autowired
//	UsageDaoImpl dao;
//	
//	@SuppressWarnings("deprecation")
//	@Test
//	public void testRetrieveDataUsageDetails(){
//		List<DataUsageDetailsInfo> list = new ArrayList<DataUsageDetailsInfo>();
//		int ban=20581442;
//		String phoneNumber="2049903359";
//		Date date= new Date(2008-1900,01,12);
//		String serviceType="subs";
//		boolean isPostpaid=false;
//		list=dao.retrieveDataUsageDetails(ban, phoneNumber, date, serviceType, isPostpaid);
//		assertEquals(2,list.size());
//		for(DataUsageDetailsInfo dinf:list){
//			assertEquals("Pocket Express Promo", dinf.getEventName());
//			assertEquals("handmark", dinf.getEventSource());
//			break;
//		}
//	}
//	@SuppressWarnings("deprecation")
//	@Test
//	public void testRetrieveDataUsageSummary(){
//		List<DataUsageSummaryInfo> list = new ArrayList<DataUsageSummaryInfo>();
//		int ban=20581442;
//		String phoneNumber="2049903359";
//		Date fromDate= new Date(2008-1900,01,12);
//		Date toDate= new Date(2008-1900,01,13);
//		boolean isPostpaid=false;
//		list=dao.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, isPostpaid);
//		assertEquals(2,list.size());
//		for(DataUsageSummaryInfo dinf:list){
//			assertEquals("subs", dinf.getServiceType());
//			assertEquals(2, dinf.getCount());
//			break;
//		}
//		list=dao.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, true);
//		assertEquals(0,list.size());
//		String[] serviceTypes={"textmsg","subs"};
//		list=dao.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, serviceTypes, isPostpaid);
//		assertEquals(2,list.size());
//		for(DataUsageSummaryInfo dinf:list){
//			assertEquals("subs", dinf.getServiceType());
//			assertEquals(2, dinf.getCount());
//			break;
//		}
//		list=dao.retrieveDataUsageSummary(ban, phoneNumber, fromDate, toDate, serviceTypes, true);
//		assertEquals(0,list.size());
//	}
//	
//	@Test
//	public void testRetrieveVoiceUsageSummary() throws ApplicationException{
//		String subscriberId="7807183952";
//		String featureCode="STD";
//		int banId=20007348;
//		VoiceUsageSummaryInfo vsi=dao.retrieveVoiceUsageSummary(banId, subscriberId, featureCode);
//		assertEquals(null,vsi);//no test data available
//	}


