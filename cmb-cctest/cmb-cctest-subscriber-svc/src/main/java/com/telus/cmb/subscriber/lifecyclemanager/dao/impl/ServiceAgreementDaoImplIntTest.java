package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.subscriber.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

@RunWith(SpringJUnit4ClassRunner.class)
public class ServiceAgreementDaoImplIntTest extends BaseLifecycleManagerIntTest {

	@Autowired
	ServiceAgreementDaoImpl dao;
	

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		System.setProperty("cmb.services.amdocs.url", "t3://corsair:5001");
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	
//	@Test
//	public void testChangePricePlan() throws ApplicationException{
//		System.out.println("testChangePricePlan Start");
//		try{
//			SubscriberInfo subscriberInfo=null;
//			SubscriberContractInfo subscriberContractInfo=null;
//			String dealerCode="";
//			String salesRepCode="";
//			PricePlanValidationInfo pricePlanValidation=null;
//			
//			
//			dao.changePricePlan(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
//			fail("Exception expected");
//		}catch(Throwable t){
//
//		}
//		System.out.println("testChangePricePlan End");
//	}
//	
//	
//	@Test
//	public void testChangeServiceAgreement() throws ApplicationException{
//		System.out.println("testChangeServiceAgreement Start");
//		try{
//			SubscriberInfo pSubscriberInfo=null;
//			SubscriberContractInfo pSubscriberContractInfo=null;
//			NumberGroupInfo pNumberGroupInfo=null;
//			String pDealerCode="";
//			String pSalesRepCode="";
//			PricePlanValidationInfo pricePlanValidation=null;
//		
//			
//			dao.changeServiceAgreement(pSubscriberInfo, pSubscriberContractInfo, 
//					pNumberGroupInfo, pDealerCode, pSalesRepCode, pricePlanValidation, sessionId);
//			fail("Exception expected");
//		}catch(Throwable t){
//
//		}
//		System.out.println("testChangeServiceAgreement End");
//	}		

}
