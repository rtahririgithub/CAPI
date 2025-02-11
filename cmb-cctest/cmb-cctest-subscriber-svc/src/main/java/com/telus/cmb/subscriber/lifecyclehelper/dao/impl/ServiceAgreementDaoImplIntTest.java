package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;

public class ServiceAgreementDaoImplIntTest extends BaseLifecycleHelperIntTest {

	@Autowired
	ServiceAgreementDaoImpl dao;
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveCallingCirclePhoneNumberListHistory(){
		
		int banId=70103921;
		String subscriberNo="9057160845";
		String productType="C";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<CallingCirclePhoneListInfo> list=dao.retrieveCallingCirclePhoneNumberListHistory(banId, subscriberNo, productType, from, to);
		for(CallingCirclePhoneListInfo ccpli:list){
			assertEquals(3,ccpli.getPhoneNumberList().length);
			assertEquals(new Date((2007-1900),(1-1),23),ccpli.getEffectiveDate());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveContractChangeHistory(){
		
		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2006-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<ContractChangeHistoryInfo> list=dao.retrieveContractChangeHistory(ban, subscriberID, from, to);
		for(ContractChangeHistoryInfo cchi:list){
			assertEquals("A001000001",cchi.getDealerCode());
			assertEquals("PPD",cchi.getReasonCode());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveFeatureParameterHistory(){
		
		int banId=20007348;
		String subscriberNo="7807183952";
		String productType="C";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		String[] parameterNames={"DATE-OF-BIRTH","CALLHOMEFREE","CALLING-CIRCLE"};
		List<FeatureParameterHistoryInfo> list=dao.retrieveFeatureParameterHistory(banId, subscriberNo, productType, parameterNames, from, to);
		for(FeatureParameterHistoryInfo fphi:list){
			assertEquals("FBC   ",fphi.getFeatureCode());
			assertEquals("1212",fphi.getParameterValue());
		}
	}
	
	@Test
	public void testRetrieveMultiRingPhoneNumbers(){

		String subscriberId="4039990012";
		List<String> list=dao.retrieveMultiRingPhoneNumbers(subscriberId);
		for(String s:list){
			assertEquals("4031110001",s);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePricePlanChangeHistory(){
		
		int ban=20001552;
		String subscriberID="4162060035";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		List<PricePlanChangeHistoryInfo> list=dao.retrievePricePlanChangeHistory(ban, subscriberID, from, to);
		System.out.println(list.size());
		for(PricePlanChangeHistoryInfo ppchi:list){
			System.out.println(ppchi.getDealerCode());
			assertEquals("A001000001",ppchi.getDealerCode());
			assertEquals("18681",ppchi.getKnowbilityOperatorID());
			break;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveServiceChangeHistory(){
		
		int ban=70103921;
		String subscriberID="9057160845";
		Date from=new Date((2000-1900),(1-1),1);
		Date to= new Date((2010-1900),(1-1),1);
		boolean includeAllServices = false;
		List<ServiceChangeHistoryInfo> list=dao.retrieveServiceChangeHistory(ban, subscriberID, from, to, includeAllServices);
		for(ServiceChangeHistoryInfo schi:list){
			assertEquals("A001000001",schi.getDealerCode());
			assertEquals("CLHMAIRCB",schi.getServiceCode());
		}
	}
	
	
	@Test
	public void testRetrieveVoiceMailFeatureByPhoneNumber(){

		String phoneNumber = "4162060035";
		String productType = "C";
		List<String> list=dao.retrieveVoiceMailFeatureByPhoneNumber(phoneNumber, productType);
		System.out.println(list.size());
		for(String s:list){
			assertEquals("FNTK150  ",s);
			break;
		}
	}
	
	@Test
	public void testRetrieveServiceAgreementBySubscriberID(){

		String subscriberId = "9057160845";
		SubscriberContractInfo sci=dao.retrieveServiceAgreementBySubscriberID(subscriberId);
		assertEquals(9,sci.getServiceCount());
		assertEquals(21,sci.getFeatures(true).length);
		assertEquals(6,sci.getFeatures(false).length);
	}
	
	@Test
	public void testRetrieveVendorServiceChangeHistory(){
		
		int ban=20580920;
		String subscriberId="4168940511";
		String[] categoryCodes={"N51","CL","VM"};
		List<VendorServiceChangeHistoryInfo> list=dao.retrieveVendorServiceChangeHistory(ban, subscriberId, categoryCodes);
		System.out.println(list.size());
		for(VendorServiceChangeHistoryInfo vschi:list){
			assertEquals("A001000001",vschi.getVendorServiceCode());
			assertEquals(2,vschi.getPromoSOCs().length);
		}
	}
	
}
