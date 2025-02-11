package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;


import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class PrepaidDaoImplIntTest {
//
//	@Autowired
//	PrepaidDaoImpl dao;
//	String phoneNumber = "2042180050";
//	
//	@BeforeClass
//	public static void beforeClass() {
//		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
//		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
//		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
//	}		
//
//	@Test
//	public void testActivateFeatureCard() throws Exception {
//		int featureId=12;
//		String userId="IntTest";
//		int duration=1212;
//		try {
//			 dao.activateFeatureCard(phoneNumber, featureId, duration,userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Test
//	public void testActivateFeature() throws Exception {
//		int featureId=12;
//		String userId="IntTest";
//		double charge=12.12;
//		int expiryDays=12121;
//		try {
//			 dao.activateFeature(phoneNumber, featureId, charge, expiryDays, userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void testActivateFeature1() throws Exception {
//		int featureId=12;
//		String userId="IntTest";
//		Date startDate= new Date();
//		Date endDate= new Date();
//		try {
//			dao.activateFeature(phoneNumber, featureId, userId, startDate, endDate);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testUpdateFeatureAutoRenewal() throws Exception {
//		int featureId=12;
//		String userId="IntTest";
//		try {
//			dao.updateFeatureAutoRenewal(phoneNumber, featureId, true, 12, userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}	
//	@Test
//	public void testDeactivateFeature() throws Exception {
//		int featureId=12;
//		double charge=12.12;
//		String userId="IntTest";
//		try {
//			dao.deactivateFeature(phoneNumber, featureId, charge, userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testUpdateSubscriberRateId() throws Exception {
//		long rateId=12323232;
//		String userId="IntTest";
//		try {
//			dao.updateSubscriberRateId(phoneNumber, userId, rateId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testupdateSubscriberExpiryDate() throws Exception {
//		String userId="IntTest";
//		Date expiryDate= new Date();
//		try {
//			dao.updateSubscriberExpiryDate(phoneNumber, userId, expiryDate);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testSaveActivationFeaturesPurchaseArrangement() throws Exception {
//		String userId="IntTest";
//		try {
//			ActivationFeaturesPurchaseArrangementInfo[] info = new ActivationFeaturesPurchaseArrangementInfo[1];
//			ActivationFeaturesPurchaseArrangementInfo infoObj = new ActivationFeaturesPurchaseArrangementInfo();
//			infoObj.setAutoRenewIndicator("12121");
//			info[0]=infoObj;
//			dao.saveActivationFeaturesPurchaseArrangement(new com.telus.prepaid.winpas.api.subscriber.Subscriber("", "", ""), info, userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testUpdateCallingCircleParameters() throws Exception {
//		String userId="IntTest";
//		String applicationId ="testapp";
//		byte action=10;
//		ServiceAgreementInfo serInf = new ServiceAgreementInfo();
//		serInf.setTransaction(action, true, true);
//		try {
//			dao.updateCallingCircleParameters(applicationId, userId, phoneNumber, serInf, action);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//	@Test
//	public void testUpdatePrepaidSubscriberLanguage() throws Exception {
//		String userId="IntTest";
//		String banId="32232";
//		String MDN="test";
//		String prevLanguage="E";
//		String serialNo="21";
//		String language="F";
//		try {
//			dao.updatePrepaidSubscriberLanguage(banId, MDN, serialNo, prevLanguage, language, userId);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
}
