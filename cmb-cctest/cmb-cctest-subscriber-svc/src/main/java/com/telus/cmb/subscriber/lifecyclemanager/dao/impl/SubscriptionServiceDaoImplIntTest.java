package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.reference.FundSource;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml", 
		"classpath:application-context-dao-lifecyclemanager.xml",
		"classpath:application-context-common.xml",
		"classpath:application-context-wsclient-prepaid.xml",
		"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml"})
public class SubscriptionServiceDaoImplIntTest {
	
	@Autowired
	SubscriptionServiceDaoImpl dao;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		
	
	/*
	@Test
	public void testUpdateAutoRenew(){
		String phoneNumber="4162809925";
		byte add= (byte)'I';
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("310");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(false);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		serAgInf1.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);
		//serAgInf1.setRecurringCharge(new Double(20.0));
		//serAgInf1.getService().getTermMonths()
	
		try {
			 dao.updateAutoRenewalFeature(phoneNumber, serAgInf1.getServiceCode().trim(), serAgInf1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testFeatureExpiryDate(){
		String phoneNumber="4162809925";
		byte add= (byte)'I';
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdf.parse("1/1/2014");
			serAgInf1.setExpiryDate(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("310");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(false);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		serAgInf1.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);
		//serAgInf1.setRecurringCharge(new Double(20.0));
		//serAgInf1.getService().getTermMonths()
	
		try {
			 dao.updateFeatureExpiryDate(phoneNumber, serAgInf1.getServiceCode().trim(), serAgInf1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	*/
	
	@Test
	public void testUpdateFeatureForPrepaidSubscriber(){
		String phoneNumber="4162809925";
		byte add= (byte)'I';
		ServiceAgreementInfo serviceAgreementInfo = new ServiceAgreementInfo();
		serviceAgreementInfo.setServiceTermUnits("MTH");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdf.parse("1/1/2014");
			serviceAgreementInfo.setExpiryDate(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		serviceAgreementInfo.setFeatureCard(true);
		serviceAgreementInfo.setWPS(true);
		serviceAgreementInfo.setTransaction(add);
		serviceAgreementInfo.setServiceCode("310");
		serviceAgreementInfo.setServiceTerm(3);
		serviceAgreementInfo.setAutoRenew(true);
		serviceAgreementInfo.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		serviceAgreementInfo.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);
		//serviceAgreementInfo.setRecurringCharge(new Double(20.0));
		//serviceAgreementInfo.getService().getTermMonths()
		
		ServiceAgreementInfo existingServiceAgreementInfo = new ServiceAgreementInfo();
		existingServiceAgreementInfo.setServiceTermUnits("MTH");
		
		sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdf.parse("1/1/2013");
			existingServiceAgreementInfo.setExpiryDate(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		existingServiceAgreementInfo.setFeatureCard(true);
		existingServiceAgreementInfo.setWPS(true);
		existingServiceAgreementInfo.setTransaction(add);
		existingServiceAgreementInfo.setServiceCode("310");
		existingServiceAgreementInfo.setServiceTerm(3);
		existingServiceAgreementInfo.setAutoRenew(false);
		existingServiceAgreementInfo.setAutoRenewFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);
		existingServiceAgreementInfo.setPurchaseFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);
		//existingServiceAgreementInfo.setRecurringCharge(new Double(20.0));
		//existingServiceAgreementInfo.getService().getTermMonths()
		
	
		try {
			 dao.updateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfo, existingServiceAgreementInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Test
	public void testDeactivateFeatureForPrepaidSubscriber(){
		String phoneNumber ="4162809925";
		
		byte delete = (byte) 'R';
		ServiceAgreementInfo serviceAgreementInfo = new ServiceAgreementInfo();
		serviceAgreementInfo.setServiceTermUnits("MTH");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date d = sdf.parse("1/1/2014");
			serviceAgreementInfo.setExpiryDate(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		serviceAgreementInfo.setFeatureCard(true);
		serviceAgreementInfo.setWPS(true);
		serviceAgreementInfo.setTransaction(delete);
		serviceAgreementInfo.setServiceCode("310");
		serviceAgreementInfo.setServiceTerm(3);
		serviceAgreementInfo.setAutoRenew(true);
		serviceAgreementInfo.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		serviceAgreementInfo.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);
		
		try {
			 dao.deactivateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfo);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}
