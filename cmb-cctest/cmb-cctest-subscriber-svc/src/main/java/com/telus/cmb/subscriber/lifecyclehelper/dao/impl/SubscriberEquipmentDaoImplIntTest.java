package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;

public class SubscriberEquipmentDaoImplIntTest extends BaseLifecycleHelperIntTest{

	
	@Autowired
	SubscriberEquipmentDaoImpl dao;
	
	@Test
	public void testGetCountForRepairID(){
		assertEquals(918,dao.getCountForRepairID("DUMMY0"));
		assertEquals(4,dao.getCountForRepairID("123123"));
		assertEquals(0,dao.getCountForRepairID("12"));
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveHandsetChangeHistory(){
		int ban=20007953;
		String subscriberID="4168940137";
		Date from = new Date(2003-1900,07,12);
		Date to = new Date(2003-1900,07,12);
		List<HandsetChangeHistoryInfo> list= new ArrayList<HandsetChangeHistoryInfo>();
		list = dao.retrieveHandsetChangeHistory(ban, subscriberID, from, to);
		assertEquals(3,list.size());
		for(HandsetChangeHistoryInfo handset:list){
			assertEquals("24701409885",handset.getNewSerialNumber());
			assertEquals("24701409887",handset.getOldSerialNumber());
			break;
		}
	}
	
	
	@Test
	public void testGetIMSIsByUSIM(){
		
		//String USIM_Id="302221000007043";
		String USIM_Id="17905762942";
		List<String> imsiList=dao.getIMSIsByUSIM(USIM_Id);System.out.println(imsiList.size());
		for(String imsi:imsiList){
			assertEquals("89013022210000070439", imsi);
		}
		
	}
	
	@Test
	public void testRetrieveEquipmentSubscribers(){
		
		String serialNumber = "17905762942";
		boolean active=false;
		List<EquipmentSubscriberInfo> esiList=dao.retrieveEquipmentSubscribers(serialNumber, active);
		for(EquipmentSubscriberInfo esi:esiList){
			assertEquals("4168940045", esi.getPhoneNumber());
			break;
		}
		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void retrieveEquipmentChangeHistory(){
		List<EquipmentChangeHistoryInfo> list = null;
		int ban=8;
		String subscriberID="4164165526";
		Date from= new Date(2010-1900,9,20);
		Date to= new Date(2010-1900,9,30);
		list =dao.retrieveEquipmentChangeHistory(ban, subscriberID, from, to);
		assertEquals(1,list.size());
		for(EquipmentChangeHistoryInfo his : list){
			assertEquals("21101114427",his.getSerialNumber());
			assertEquals(1,his.getEsnLevel());
		}
		
	}
	@Test
	public void getUSIMListByIMSIs(){
		String[] ary= new String[2];
		ary[0]="1000";
		ary[1]="100012";
		Hashtable<String,String> hashTable= dao.getUSIMListByIMSIs(ary);
		assertEquals(2,hashTable.size());
		assertEquals("500",hashTable.get("1000"));
		assertEquals("100",hashTable.get("100012"));
	}
	@Test
	public void getIMSIsByUSIM(){
		List<String> list= null;
		list=dao.getIMSIsByUSIM("100");
		assertEquals(2,list.size());
		assertEquals("1001",list.get(0));
		assertEquals("100012",list.get(1));
	}
	@Test
	public void getIMSIsBySerialNumber(){
			assertEquals("214030000074960",dao.getIMSIsBySerialNumber("300000000100225").get(0));
			assertEquals(0,dao.getIMSIsBySerialNumber("3000000001002251").size());
	}
}
