package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.reference.FundSource;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml", "classpath:application-context-dao-lifecyclemanager.xml",
		"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml"})
public class OrderServiceDaoImplIntTest {
	
	@Autowired
	OrderServiceDaoImpl dao;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		

	@Test
	public void testActivateFeature() throws Exception {
		
		String phoneNumber="3232223";
		String userId="testUser";
		double charge=0;
		byte add= (byte)'I';
		byte delete = (byte) 'R';
		byte update = (byte) 'U';
		ServiceAgreementInfo[] serAgInfArray= new ServiceAgreementInfo[3];
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("2112");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(true);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);

		ServiceAgreementInfo serAgInf2 = new ServiceAgreementInfo();
		serAgInf2.setWPS(true);
		//serAgInf2.setTransaction(update);
		serAgInf2.setTransaction(add);
		serAgInf2.setServiceCode("2112");
		serAgInf2.setServiceTerm(3);
		serAgInf2.setAutoRenew(true);
		serAgInf2.setAutoRenewFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);

		ServiceAgreementInfo serAgInf3 = new ServiceAgreementInfo();
		serAgInf3.setWPS(true);
		//serAgInf3.setTransaction(delete);
		serAgInf3.setTransaction(add);
		serAgInf3.setServiceCode("2112");
		serAgInf3.setServiceTerm(3);
		serAgInf3.setAutoRenew(true);
		serAgInf3.setAutoRenewFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);
		
		serAgInfArray[0]=serAgInf1;
		serAgInfArray[1]=serAgInf2;
		serAgInfArray[2]=serAgInf3;
		
		List<ServiceAgreementInfo> serviceAgreementInfoToAdd = new ArrayList<ServiceAgreementInfo>();
		List<ServiceAgreementInfo> serviceAgreementInfoToUpdate = new ArrayList<ServiceAgreementInfo>();
		List<ServiceAgreementInfo> serviceAgreementInfoToDelete = new ArrayList<ServiceAgreementInfo>();
		
		for (int i=0; i < serAgInfArray.length; i++) {
			
			System.out.println("I: " + i);
			System.out.println("Transaction: " + serAgInfArray[i].getTransaction());
			
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.ADD)){
				dao.activateFeatureForPrepaidSubscriber(phoneNumber, serAgInfArray[i]);
			}
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.UPDATE)){
				//TODO
				System.out.println("TODO Update");
			}		
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.DELETE)){
				//TODO
				System.out.println("TODO Delete");
			}
		}
	}
	/*
	@Test
	public void testActivateFeature2() throws Exception {
		
		String phoneNumber="3232223";
		String userId="testUser";
		double charge=0;
		byte add= (byte)'I';
		ServiceAgreementInfo[] serAgInfArray= new ServiceAgreementInfo[1];
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("539");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(true);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		//serAgInf1.setRecurringCharge(new Double(20.0));
		//serAgInf1.getService().getTermMonths()
	
		serAgInfArray[0]=serAgInf1;
		
		List<ServiceAgreementInfo> serviceAgreementInfoToAdd = new ArrayList<ServiceAgreementInfo>();
		
		for (int i=0; i < serAgInfArray.length; i++) {
			
			System.out.println("I: " + i);
			System.out.println("Transaction: " + serAgInfArray[i].getTransaction());
			
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.ADD)){
				serviceAgreementInfoToAdd.add(serAgInfArray[i]);
				System.out.println("Added");
			}			
		}
		try {
			 dao.activateMultipleFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfoToAdd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testActivateFeature3() throws Exception {
		
		String phoneNumber="9057160424";
		byte add= (byte)'I';
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("310");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(true);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_BALANCE);
		serAgInf1.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);
		//serAgInf1.setRecurringCharge(new Double(20.0));
		//serAgInf1.getService().getTermMonths()
	
		try {
			 dao.activateFeatureForPrepaidSubscriber(phoneNumber, serAgInf1);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Test
	public void testActivateFeature4() throws Exception {
		
		String phoneNumber="9057160424";
		byte add= (byte)'I';
		ServiceAgreementInfo[] serAgInfArray= new ServiceAgreementInfo[2];
		ServiceAgreementInfo serAgInf1 = new ServiceAgreementInfo();
		serAgInf1.setServiceTermUnits("MTH");
		serAgInf1.setFeatureCard(true);
		serAgInf1.setWPS(true);
		serAgInf1.setTransaction(add);
		serAgInf1.setServiceCode("310");
		serAgInf1.setServiceTerm(3);
		serAgInf1.setAutoRenew(true);
		serAgInf1.setAutoRenewFundSource(FundSource.FUND_SOURCE_CREDIT_CARD);
		serAgInf1.setPurchaseFundSource(FundSource.FUND_SOURCE_BALANCE);

		ServiceAgreementInfo serAgInf2 = new ServiceAgreementInfo();
		serAgInf2.setWPS(true);
		//serAgInf2.setTransaction(update);
		serAgInf2.setTransaction(add);
		serAgInf2.setServiceCode("539");
		serAgInf2.setServiceTerm(3);
		serAgInf2.setAutoRenew(false);
		
		serAgInfArray[0]=serAgInf1;
		serAgInfArray[1]=serAgInf2;
		
		List<ServiceAgreementInfo> serviceAgreementInfoToAdd = new ArrayList<ServiceAgreementInfo>();
		List<ServiceAgreementInfo> serviceAgreementInfoToUpdate = new ArrayList<ServiceAgreementInfo>();
		List<ServiceAgreementInfo> serviceAgreementInfoToDelete = new ArrayList<ServiceAgreementInfo>();
		
		for (int i=0; i < serAgInfArray.length; i++) {
			
			System.out.println("I: " + i);
			System.out.println("Transaction: " + serAgInfArray[i].getTransaction());
			
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.ADD)){
				serviceAgreementInfoToAdd.add(serAgInfArray[i]);
				
			}
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.UPDATE)){
				//TODO
				System.out.println("TODO Update");
			}		
			if (serAgInfArray[i].isWPS() &&
					(serAgInfArray[i].getTransaction() == BaseAgreementInfo.DELETE)){
				//TODO
				System.out.println("TODO Delete");
			}
		}
		try {
			 dao.activateMultipleFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfoToAdd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	*/
	
}
