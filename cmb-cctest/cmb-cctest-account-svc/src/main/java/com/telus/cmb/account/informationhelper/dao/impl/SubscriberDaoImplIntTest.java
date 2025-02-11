package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;

public class SubscriberDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	SubscriberDaoImpl dao;

	@Test
	public void testRetrieveActiveSubscriberPhoneNumbers() {
		if (System.getProperty("env") != null && System.getProperty("env").equals("PT168")) {
			Collection<String> phoneNumbers = dao.retrieveActiveSubscriberPhoneNumbers(70602349, 10);
			assertEquals(1, phoneNumbers.size());
			for (String phoneNumber : phoneNumbers) {
				System.out.println(phoneNumber);
			}
		} 
	}

	@Test
	public void testRetrieveSuspendedSubscriberPhoneNumbers() {
		if (System.getProperty("env") != null && System.getProperty("env").equals("PT168")) {
			assertEquals(0, dao.retrieveSuspendedSubscriberPhoneNumbers(70106788, 10).size());
		}
	}
	
	@Test
	public void testRetrievePhoneNumbersByBan() {
		assertEquals(2, dao.retrievePhoneNumbersForBAN(20007869).size());
	}
	
	@Test
	public void testRetrieveAttachedSubscribersCount() {
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo();
		fleetIdentityInfo.setFleetId(131072);
		fleetIdentityInfo.setUrbanId(905);
		assertEquals(1, dao.retrieveAttachedSubscribersCount(25, fleetIdentityInfo));
	}
	
	@Test
	public void testRetrieveProductSubscriberLists() {		
		Collection<ProductSubscriberListInfo> subscribers = dao.retrieveProductSubscriberLists(20007869);
		assertEquals(1, subscribers.size());
		for (ProductSubscriberListInfo sub : subscribers) {
			assertEquals(2, sub.getReservedSubscribersCount());
			assertEquals(0, sub.getActiveSubscribersCount());
			assertEquals(0, sub.getCancelledSubscribersCount());
			assertEquals(0, sub.getSuspendedSubscribersCount());
		}
		
		assertEquals(0, dao.retrieveProductSubscriberLists(0).size());
	}
	
	@Test
	public void testRetrieveSubscriberIdsByStatus() {
		
		assertEquals(50, dao.retrieveSubscriberIdsByStatus(20001552, 'C', 50).size());
		assertEquals(0, dao.retrieveSubscriberIdsByStatus(2000155, 'C', 10).size());
		
	}
	
	@Test
	public void testRetrieveSubscriberPhoneNumbersByStatus() {
		
		assertEquals(12, dao.retrieveSubscriberPhoneNumbersByStatus(20001552, 'A', 50).size());
		assertEquals(0, dao.retrieveSubscriberPhoneNumbersByStatus(2000155, 'C', 10).size());
		
	}
	@Test
	public void testisFeatureCategoryExistOnSubscribers(){

		assertEquals(true,dao.isFeatureCategoryExistOnSubscribers(8,"VM"));
		assertEquals(false,dao.isFeatureCategoryExistOnSubscribers(24,"VM"));
	}
	@Test
	public void testretrieveHotlinedSubscriberPhoneNumber(){
		assertEquals("6472065737",dao.retrieveHotlinedSubscriberPhoneNumber(10000047));
		assertEquals("7807185873",dao.retrieveHotlinedSubscriberPhoneNumber(20007401));
		assertEquals("",dao.retrieveHotlinedSubscriberPhoneNumber(2));
	}
	
	@Test
	public void testRetrievePCSNetworkCountByBan(){
		assertEquals(new Integer(21),dao.retrievePCSNetworkCountByBan(8).get("C"));
		assertEquals(new Integer(0),dao.retrievePCSNetworkCountByBan(8).get("H"));
		
	}
	

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts1(){
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= dao.retrievePoolingPricePlanSubscriberCounts(570899,PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL,false);
		assertEquals(2,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());
		
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= dao.retrievePoolingPricePlanSubscriberCounts(570899,4,false);
		assertEquals(1,poolPP1.size());
		assertEquals(4,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}
	
	@Test
	public void testretrievePoolingPricePlanSubscriberCounts2(){
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= dao.retrievePoolingPricePlanSubscriberCounts(70105350, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL,true);
		assertEquals(6,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());
		
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= dao.retrievePoolingPricePlanSubscriberCounts(70105350, 7,true);
		assertEquals(1,poolPP1.size());
		assertEquals(7,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}
	
	// service codes list for testing 
	/*"PBS100MC ","SDOC1    ","SE911    ","SEW9PF9  ","SLD25G79 ","SMTMWB   ","SSMSB    ","SUNL6NW7 ","SVTTM15U ","SWWSS    ","USAR95F9 ","USTL50F9 ","PCPT3    ",
	"SDOC1    ","SE911    ","SLD40G79 ","SSMSB    ","SWWSS    ","USAR95F9 ","USTL50F9 ","PCPT3    ","S7VM10   ","SDOC1    ","SE911    ","SLD40G79 ","SSMSB    ",
	"SWWSS    ","USAR95F9 ","USTL50F9 ","PXSHP40NC","SDOC1    ","SE911A   ","SFBC     ","SLD35FX  ","SMTMWB   ","SSMSB    ","SUEWFX   ","SUNLSHTEX","SUSA95FX ",
	"SUSCA50FX","SWWSS    ","PCPT3    ","SDOC1    ","SE911    ","SLD40G79 ","SSMSB    ","SWWSS    ","USAR95F9 ","USTL50F9 ","PRIM100N3","RIMSB    ","RIMTM    ",
	"SDOC1    ","SE911    ","SFBC     ","SLD25H79 ","SLD25R79 ","SWWSS    ","USAR95F9 ","USTL50F9 "*/ 
	@Test
	public void testretrieveServiceSubscriberCounts(){
		Collection<ServiceSubscriberCount> scc=dao.retrieveServiceSubscriberCounts(70105350, 
				new String[]{"USTL50F9 ","PRIM100N3","RIMSB    ","RIMTM    ","SDOC1    ","SE911    ","SFBC     "}, true);
		assertEquals(7,scc.size());
		assertEquals("PRIM100N3", ((ServiceSubscriberCount[])scc.toArray(new ServiceSubscriberCount[scc.size()]))[0].getServiceCode());
	}
	
	@Test
	public void testretrieveMinutePoolingEnabledPricePlanSubscriberCounts(){
		Collection<PricePlanSubscriberCount> ppsc=dao.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(570899, new String[]{"H","M","O","R"});
		assertEquals(3,ppsc.size());
		assertEquals("PSHAR10MP", ((PricePlanSubscriberCount[])ppsc.toArray(new PricePlanSubscriberCount[ppsc.size()]))[0].getPricePlanCode());
	}
	
	@Test
	public void testretrieveDollarPoolingPricePlanSubscriberCounts(){
		Collection<PricePlanSubscriberCountInfo> ppsci=dao.retrievePricePlanSubscriberCountInfo(9980102,"I");
		assertEquals(1,ppsci.size());
		assertEquals("M001956744",((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}
	
	@Test
	public void testretrieveShareablePricePlanSubscriberCount(){
		Collection<PricePlanSubscriberCountInfo> ppsci=dao.retrieveShareablePricePlanSubscriberCount(9346510);
		assertEquals(1,ppsci.size());
		assertEquals("4035543200", ((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}
	@Test
	public void testretrieveSubscriberIdsByServiceFamily(){
		String[] subIds=dao.retrieveSubscriberIdsByServiceFamily(8, "P", new Date() );
		assertEquals(2,subIds.length );
		System.out.println( Arrays.asList( subIds) );
	}
	@Test
	public void testretrieveSubscribersByDataAhringGroupCodes(){
		SubscribersByDataSharingGroupResultInfo[] result=dao.retrieveSubscribersByDataSharingGroupCodes( 8, new String[] {"TB"}, new Date() );
		System.out.println( Arrays.asList( result) );
		assertEquals(0,result.length );
	}
	
	// Business connect test cases - July 2014
	
		@Test
		public void testRetrieveProductSubscriberListsWithSeat() {		
			Collection<ProductSubscriberListInfo> subscribers = dao.retrieveProductSubscriberLists(20002207);
			assertEquals(1, subscribers.size());
			for (ProductSubscriberListInfo sub : subscribers) {
				SubscriberIdentifier[] identifier =sub.getCancelledSubscriberIdentifiers();
				for(int k=0;k<identifier.length;k++)
				{
					System.out.println(identifier[k].getSeatType());
					System.out.println(identifier[k].getSeatGroup());
				}
			}
			
			assertEquals(0, dao.retrieveProductSubscriberLists(0).size());
		}

}
