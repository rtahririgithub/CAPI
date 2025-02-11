/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.subscriber.dao;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.AccountDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.DepositDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.FleetDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.InvoiceDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.MemoDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.ServiceAgreementDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.SubscriberDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.SubscriberEquipmentDaoImpl;
import com.telus.cmb.subscriber.lifecyclehelper.dao.impl.UsageDaoImpl;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;

@ContextConfiguration(locations={"classpath:application-context-side-by-side-test.xml"})
@ActiveProfiles("standalone")
public class SubscriberDaoParallelTest extends AbstractTestNGSpringContextTests {

	@Autowired
	ApplicationContext context;
	private static final String KNOWBILITY_JDBC_TEMPLATE_O9 = "knowbilityJdbcTemplateO9";
	private static final String KNOWBILITY_JDBC_TEMPLATE_O12 = "knowbilityJdbcTemplateO12";		
	
	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
	}
	
	@Test
	public void run_O9_tests() throws Exception {
		Writer writerO9 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("subscriber-test-results-O9.txt"), "utf-8"));		
		Writer writerO9_perf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("p_subscriber-test-results-O9.txt"), "utf-8"));			
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O9, writerO9,writerO9_perf);
		writerO9.close();
		writerO9_perf.close();
	}
	
	@Test
	public void run_O12_tests() throws Exception {
		Writer writerO12 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("subscriber-test-results-O12.txt"), "utf-8"));
		Writer writerO12_perf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("perf_subscriber-test-results-O12.txt"), "utf-8"));
		run_all_tests(KNOWBILITY_JDBC_TEMPLATE_O12, writerO12,writerO12_perf);		
		writerO12.close();
		writerO12_perf.close();
	}	
	
	@Test
	public void run_all_tests(String template, Writer writer,Writer writer_perf) throws Exception {
		testRetrieveSubscriberListByBanAndFleet(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement	
		testRetrieveSubscriberListByBAN(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement		
		testRetrieveSubscriberListByBANAndSubscriberId(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement
		testRetrieveSubscriberListBySerialNumber(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement			
		testRetrieveFeatureParameterHistory(template, writer,writer_perf); 

		testRetrieveAccountByBan(template, writer,writer_perf);
		testRetrieveBanIdByPhoneNumber(template, writer,writer_perf);
		testRetrieveDepositHistory(template, writer,writer_perf);
		testRetrievePaidSecurityDeposit(template, writer,writer_perf);
		testRetrieveTalkGroupsBySubscriber(template, writer,writer_perf);
		testRetrieveInvoiceTaxInfo(template, writer,writer_perf);
		testRetrieveLastMemo(template, writer,writer_perf);
		testRetrieveCallingCirclePhoneNumberListHistory(template, writer,writer_perf);
		testRetrieveFeatureParameterHistory(template, writer,writer_perf); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection
		testRetrieveMultiRingPhoneNumbers(template, writer,writer_perf); //no data
		testRetrieveVendorServiceChangeHistory(template, writer,writer_perf);
		testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(template, writer,writer_perf);
		testRetrieveSubscriberListByBAN(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement		
		testRetrieveSubscriberListByBANAndSubscriberId(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement
		testRetrieveSubscriberListBySubscriberID(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement		
		testRetrievePartiallyReservedSubscriberListByBan(template, writer,writer_perf);		
		testRetrieveResourceChangeHistory(template, writer,writer_perf);
		testRetrieveLightWeightSubscriberListByBAN(template, writer,writer_perf);
		testRetrieveLastAssociatedSubscriptionId(template, writer,writer_perf);
		testRetrieveHotlineIndicator(template, writer,writer_perf);
		testRetrieveSubscriberHistory(template, writer,writer_perf);	
		testRetrieveSubscriberListByBanAndFleet(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement	
	    testRetrieveSubscriberListByBanAndTalkGroup(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement	
	    testRetrieveSubscriberListBySerialNumber(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement			
	    testRetrieveSubscriberListByPhoneNumbers(template, writer,writer_perf); //org.apache.commons.dbcp.PoolableConnection cannot be cast to oracle.jdbc.OracleConnection	
	    testRetrieveHSPASubscriberListByIMSI(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement	
    	testRetrieveSubscriberByBANAndPhoneNumber(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement		
		testRetrieveSubscriberListBySeatResourceNumber(template, writer,writer_perf); //org.apache.commons.dbcp.DelegatingCallableStatement cannot be cast to oracle.jdbc.OracleCallableStatement		
		testRetrieveSeatResourceInfoByBanAndPhoneNumber(template, writer,writer_perf);	
		testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(template, writer,writer_perf);		
		testRetrieveEquipmentChangeHistory(template, writer,writer_perf);			
	}
	
	
	@Test
	public void testRetrieveAccountByBan(String template, Writer writer, Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveAccountByBan\n");
		long startTime  = System.currentTimeMillis();
		AccountDaoImpl accountDao = new AccountDaoImpl();
		accountDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		SubscriberInfo sub = accountDao.retrieveBanForPartiallyReservedSub("4166778653");
		writer.append(sub.toString());
		printExecutionTime(startTime, "RetrieveAccountByBan", writer_perf);
		writer.append("\nExit testRetrieveAccountByBan\n");			
	}	
	
	private void printExecutionTime(long startTime,String methodName , Writer writer_perf) throws IOException{
		long totalExecutiontime = System.currentTimeMillis() - startTime;
		writer_perf.append(methodName +" total executionTime is , "+totalExecutiontime +"ms \n");
	}
	
	@Test
	public void testRetrieveBanIdByPhoneNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveBanIdByPhoneNumber\n");	
		long startTime = System.currentTimeMillis();
		AccountDaoImpl accountDao = new AccountDaoImpl();
		accountDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		int ban = accountDao.retrieveBanIdByPhoneNumber("4161633153");
		printExecutionTime(startTime, "RetrieveBanIdByPhoneNumber", writer_perf);
		writer.append(String.valueOf(ban));
		writer.append("\nExit testRetrieveBanIdByPhoneNumber\n");			
	}	
	
	@Test
	public void testRetrieveDepositHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveDepositHistory\n");	
		long startTime = System.currentTimeMillis();
		DepositDaoImpl depositDao = new DepositDaoImpl();
		depositDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<DepositHistoryInfo> depositHistoryList = depositDao.retrieveDepositHistory(70800731, "4031725291");
		for (DepositHistoryInfo depositHistoryInfo : depositHistoryList) {
			writer.append(depositHistoryInfo.toString());
		}
		printExecutionTime(startTime, "testRetrieveDepositHistory", writer_perf);
		writer.append("\nExit testRetrieveDepositHistory\n");			
	}	
	
	@Test
	public void testRetrievePaidSecurityDeposit(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePaidSecurityDeposit\n");	
		long startTime = System.currentTimeMillis();
		DepositDaoImpl depositDao = new DepositDaoImpl();
		depositDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		double deposit = depositDao.retrievePaidSecurityDeposit(70550525, "4161613205", "C");
		writer.append(String.valueOf(deposit));
		printExecutionTime(startTime, "testRetrievePaidSecurityDeposit", writer_perf);
		writer.append("\nExit testRetrievePaidSecurityDeposit\n");			
	}
	
	@Test
	public void testRetrieveTalkGroupsBySubscriber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveTalkGroupsBySubscriber\n");		
		long startTime = System.currentTimeMillis();
		FleetDaoImpl fleetDao = new FleetDaoImpl();
		fleetDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<TalkGroupInfo> talkGroupList = fleetDao.retrieveTalkGroupsBySubscriber("M000098755");
		for (TalkGroupInfo talkGroup : talkGroupList) {
			writer.append(talkGroup.toString());
		}	
		printExecutionTime(startTime, "testRetrieveTalkGroupsBySubscriber", writer_perf);
		writer.append("\nExit testRetrieveTalkGroupsBySubscriber\n");			
	}
	
	@Test
	public void testRetrieveInvoiceTaxInfo(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveInvoiceTaxInfo\n");	
		long startTime = System.currentTimeMillis();
		InvoiceDaoImpl invoiceDaoImpl = new InvoiceDaoImpl();
		invoiceDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		InvoiceTaxInfo invoiceTaxInfo = invoiceDaoImpl.retrieveInvoiceTaxInfo(70576908, "4161653613", 5);
		writer.append(invoiceTaxInfo.toString());
		printExecutionTime(startTime, "testRetrieveInvoiceTaxInfo", writer_perf);
		writer.append("\nExit testRetrieveInvoiceTaxInfo\n");			
	}
	
	@Test
	public void testRetrieveLastMemo(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLastMemo\n");	
		long startTime = System.currentTimeMillis();
		MemoDaoImpl memoDaoImpl = new MemoDaoImpl();
		memoDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		MemoInfo memoInfo = memoDaoImpl.retrieveLastMemo(70805958, "4160619482", "PORT");
		writer.append(memoInfo.toString());
		printExecutionTime(startTime, "testRetrieveLastMemo", writer_perf);
		writer.append("\nExit testRetrieveLastMemo\n");			
	}
	
	@Test
	public void testRetrieveCallingCirclePhoneNumberListHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveCallingCirclePhoneNumberListHistory\n");			
		long startTime = System.currentTimeMillis();
		ServiceAgreementDaoImpl serviceAgreementDao = new ServiceAgreementDaoImpl();
		serviceAgreementDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Date fromDate = new Date(0);
		Date toDate = new Date();
		List<CallingCirclePhoneListInfo> callingCircleList = serviceAgreementDao.retrieveCallingCirclePhoneNumberListHistory(70794337, "6478763538", "C", fromDate, toDate);
		for (CallingCirclePhoneListInfo callingCircle : callingCircleList) {
			for (String phoneNumber : callingCircle.getPhoneNumberList()) {			
				writer.append(phoneNumber);
			}
		}
		printExecutionTime(startTime, "testRetrieveCallingCirclePhoneNumberListHistory", writer_perf);

		writer.append("\nExit testRetrieveCallingCirclePhoneNumberListHistory\n");			
	}	
	
	@Test
	public void testRetrieveFeatureParameterHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveFeatureParameterHistory\n");			
		long startTime = System.currentTimeMillis();
		ServiceAgreementDaoImpl serviceAgreementDao = new ServiceAgreementDaoImpl();
		serviceAgreementDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Date fromDate = new Date(0);
		Date toDate = new Date();		
		List<FeatureParameterHistoryInfo> featureParameterHistoryList = serviceAgreementDao.retrieveFeatureParameterHistory(70794337, "6478763538", "C", null, fromDate, toDate);
		System.out.println(featureParameterHistoryList.size());
		for (FeatureParameterHistoryInfo featureParameterHistory : featureParameterHistoryList) {
			writer.append(featureParameterHistory.toString());
		}
		printExecutionTime(startTime, "testRetrieveFeatureParameterHistory", writer_perf);

		writer.append("\nExit testRetrieveFeatureParameterHistory\n");			
	}	
	
	@Test
	public void testRetrieveMultiRingPhoneNumbers(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveMultiRingPhoneNumbers\n");			
		long startTime = System.currentTimeMillis();
		ServiceAgreementDaoImpl serviceAgreementDao = new ServiceAgreementDaoImpl();
		serviceAgreementDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<String> multiRingPhoneNumberList = serviceAgreementDao.retrieveMultiRingPhoneNumbers("7802220025");
		for (String phoneNumber : multiRingPhoneNumberList) {
			writer.append(phoneNumber);
		}	
		printExecutionTime(startTime, "testRetrieveMultiRingPhoneNumbers", writer_perf);

		writer.append("\nExit testRetrieveMultiRingPhoneNumbers\n");			
	}		
	
	@Test
	public void testRetrieveVendorServiceChangeHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveVendorServiceChangeHistory\n");			
		long startTime = System.currentTimeMillis();
		ServiceAgreementDaoImpl serviceAgreementDao = new ServiceAgreementDaoImpl();
		serviceAgreementDao.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		String[] categoryCodes = {"N51", "N53"}; 
		List<VendorServiceChangeHistoryInfo> vendorChangeHistoryInfoList = serviceAgreementDao.retrieveVendorServiceChangeHistory(70524319, "4038807988", categoryCodes);
		for (VendorServiceChangeHistoryInfo vendorChangeHistory : vendorChangeHistoryInfoList) {
			writer.append(vendorChangeHistory.toString());
		}
		printExecutionTime(startTime, "testRetrieveVendorServiceChangeHistory", writer_perf);

		writer.append("\nExit testRetrieveVendorServiceChangeHistory\n");			
	}	
	
	@Test
	public void testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		SubscriberIdentifierInfo subscriberIdentifierInfo = subscriberDaoImpl.retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(70712536, "4161615711");
		writer.append(subscriberIdentifierInfo.toString());
		printExecutionTime(startTime, "testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber", writer_perf);

		writer.append("\nExit testRetrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber\n");			
		
	}	
	
	@Test
	public void testRetrieveSubscriberListByBAN(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListByBAN\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByBAN(70712536, 5, false);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}		
		printExecutionTime(startTime, "testRetrieveSubscriberListByBAN", writer_perf);

		writer.append("\nExit testRetrieveSubscriberListByBAN\n");			
	}	
	
	@Test
	public void testRetrieveSubscriberListByBANAndSubscriberId(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListByBANAndSubscriberId\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByBANAndSubscriberId(70712536, "4161615711", false, 5);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}
		printExecutionTime(startTime, "testRetrieveSubscriberListBySubscriberID", writer_perf);

		writer.append("\nExit testRetrieveSubscriberListBySubscriberID\n");			
	}
	
	@Test
	public void testRetrieveSubscriberListBySubscriberID(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListBySubscriberID\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListBySubscriberId("4161615711", false, 5);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}		
		printExecutionTime(startTime, "testRetrieveSubscriberListBySubscriberID", writer_perf);

		writer.append("\nExit testRetrieveSubscriberListBySubscriberID\n");			
		
	}
	
	@Test
	public void testRetrievePartiallyReservedSubscriberListByBan(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrievePartiallyReservedSubscriberListByBan\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		
		List<String> subscriberInfoList = subscriberDaoImpl.retrievePartiallyReservedSubscriberListByBan(70761854, 5);
		Collections.sort(subscriberInfoList);
		for (String subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo + "\n");
		}	
		printExecutionTime(startTime, "testRetrievePartiallyReservedSubscriberListByBan", writer_perf);

		writer.append("\nExit testRetrievePartiallyReservedSubscriberListByBan\n");			
		
	}
	
	@Test
	public void testRetrieveResourceChangeHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveResourceChangeHistory\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Date fromDate = new Date(0);
		Date toDate = new Date();		
		List<ResourceChangeHistoryInfo> resourceChangeHistoryInfoList = subscriberDaoImpl.retrieveResourceChangeHistory(70678526, "4161063808", "Q", fromDate, toDate);
		for (ResourceChangeHistoryInfo resourceChangeHistoryInfo : resourceChangeHistoryInfoList) {
			writer.append(resourceChangeHistoryInfo.toString());
		}	
		printExecutionTime(startTime, "testRetrieveResourceChangeHistory", writer_perf);

		writer.append("\nExit testRetrieveResourceChangeHistory\n");			

	}
	
	@Test
	public void testRetrieveLightWeightSubscriberListByBAN(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLightWeightSubscriberListByBAN\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<LightWeightSubscriberInfo> lightWeightSubscriberInfoList = subscriberDaoImpl.retrieveLightWeightSubscriberListByBAN(70678526, false, 5, false);
		for (LightWeightSubscriberInfo lightWeightSubscriberInfo : lightWeightSubscriberInfoList) {
			writer.append(lightWeightSubscriberInfo.toString());
		}		
		printExecutionTime(startTime, "testRetrieveLightWeightSubscriberListByBAN", writer_perf);
		writer.append("\nExit testRetrieveLightWeightSubscriberListByBAN\n");			
	
	}
	
	@Test
	public void testRetrieveLastAssociatedSubscriptionId(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLastAssociatedSubscriptionId\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		String subscriptionId = subscriberDaoImpl.retrieveLastAssociatedSubscriptionId("302221000484497");
		writer.append(subscriptionId);
		printExecutionTime(startTime, "testRetrieveLastAssociatedSubscriptionId", writer_perf);
		writer.append("\nExit testRetrieveLastAssociatedSubscriptionId\n");			
	}
	
	@Test
	public void testRetrieveHotlineIndicator(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveHotlineIndicator\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		boolean hotlineIndicator = subscriberDaoImpl.retrieveHotlineIndicator("4160617091");
		writer.append(String.valueOf(hotlineIndicator));
		printExecutionTime(startTime, "testRetrieveHotlineIndicator", writer_perf);

		writer.append("\nExit testRetrieveHotlineIndicator\n");			
	}
	
	@Test
	public void testRetrieveSubscriberHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberHistory\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Date fromDate = new Date(0);
		Date toDate = new Date();		
		List<SubscriberHistoryInfo> subscriberHistoryInfoList = subscriberDaoImpl.retrieveSubscriberHistory(70024305, "6049283412", fromDate, toDate);
		for (SubscriberHistoryInfo subscriberHistoryInfo : subscriberHistoryInfoList) {
			writer.append(subscriberHistoryInfo.toString());
		}						
		printExecutionTime(startTime, "testRetrieveSubscriberHistory", writer_perf);
		writer.append("\nExit testRetrieveSubscriberHistory\n");			
	}
	
	@Test
	public void testRetrieveSubscriberListByBanAndFleet(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListByBanAndFleet\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByBanAndFleet(6000651, 905, 9868, 5);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}
		printExecutionTime(startTime, "testRetrieveSubscriberListByBanAndFleet", writer_perf);
		writer.append("\nExit testRetrieveSubscriberListByBanAndFleet\n");			
	}	
	
	@Test
	public void testRetrieveSubscriberListByBanAndTalkGroup(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListByBanAndTalkGroup\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByBanAndTalkGroup(6001399, 905, 167, 1, 5);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}	
		printExecutionTime(startTime, "testRetrieveSubscriberListByBanAndTalkGroup", writer_perf);
		writer.append("\nExit testRetrieveSubscriberListByBanAndTalkGroup\n");			
	}	
	
	@Test
	public void testRetrieveSubscriberListBySerialNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListBySerialNumber\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListBySerialNumber("11902188555", true);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}	
		printExecutionTime(startTime, "testRetrieveSubscriberListBySerialNumber", writer_perf);
		writer.append("\nExit testRetrieveSubscriberListBySerialNumber\n");			
	}	
	
	@Test
	public void testRetrieveSubscriberListByPhoneNumbers(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListByPhoneNumbers\n");			
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<String> phoneNumbers = new ArrayList<String>();
		phoneNumbers.add("4163160072");
		phoneNumbers.add("4035403370");
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByPhoneNumbers(phoneNumbers, 0,false);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}
		printExecutionTime(startTime, "testRetrieveSubscriberListByPhoneNumbers", writer_perf);
		writer.append("\nExit testRetrieveSubscriberListByPhoneNumbers\n");			
	}
	
	@Test
	public void testRetrieveHSPASubscriberListByIMSI(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveHSPASubscriberListByIMSI\n");	
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveHSPASubscriberListByIMSI("302221000431730", true);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}			
		printExecutionTime(startTime, "testRetrieveHSPASubscriberListByIMSI", writer_perf);
		writer.append("\nExit testRetrieveHSPASubscriberListByIMSI\n");			
	}
	
	@Test
	public void testRetrieveSubscriberByBANAndPhoneNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberByBANAndPhoneNumber\n");	
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		SubscriberInfo subscriberInfo = subscriberDaoImpl.retrieveSubscriberByBANAndPhoneNumber(28053251, "4161800049");
		writer.append(subscriberInfo.toString());
		printExecutionTime(startTime, "testRetrieveSubscriberByBANAndPhoneNumber", writer_perf);
		writer.append("\nExit testRetrieveSubscriberByBANAndPhoneNumber\n");			
	}	
	
	@Test
	public void testRetrieveSubscriberListBySeatResourceNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSubscriberListBySeatResourceNumber\n");	
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<SubscriberInfo> subscriberInfoList = (List<SubscriberInfo>) subscriberDaoImpl.retrieveSubscriberListByBanAndSeatResourceNumber(0,"4503000235", 5, true);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			writer.append(subscriberInfo.toString());
		}					
		printExecutionTime(startTime, "testRetrieveSubscriberListBySeatResourceNumber", writer_perf);
		writer.append("\nExit testRetrieveSubscriberListBySeatResourceNumber\n");			
	}	

	@Test
	public void testRetrieveSeatResourceInfoByBanAndPhoneNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveSeatResourceInfoByBanAndPhoneNumber\n");	
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		List<ResourceInfo> resourceInfoList = subscriberDaoImpl.retrieveSeatResourceInfoByBanAndPhoneNumber(70785083, "4160618697", true);
		for (ResourceInfo resourceInfo : resourceInfoList) {
			writer.append(resourceInfo.toString());
		}
		printExecutionTime(startTime, "testRetrieveSeatResourceInfoByBanAndPhoneNumber", writer_perf);
		writer.append("\nExit testRetrieveSeatResourceInfoByBanAndPhoneNumber\n");			
	}

	@Test
	public void testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber\n");		
		long startTime = System.currentTimeMillis();
		SubscriberDaoImpl subscriberDaoImpl = new SubscriberDaoImpl();
		subscriberDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		SubscriberIdentifierInfo subscriberIdentifierInfo = subscriberDaoImpl.retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(70785083, "4503000235");
		writer.append(subscriberIdentifierInfo.toString());
		printExecutionTime(startTime, "testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber", writer_perf);
		writer.append("\nExit testRetrieveLatestSubscriberIdentifierInfoBySeatResourceNumber\n");			
	}

	@Test
	public void testRetrieveEquipmentChangeHistory(String template, Writer writer,Writer writer_perf) throws Exception {
		writer.append("\nEnter testRetrieveEquipmentChangeHistory\n");			
		long startTime = System.currentTimeMillis();
		SubscriberEquipmentDaoImpl subscriberEquipmentDaoImpl = new SubscriberEquipmentDaoImpl();
		subscriberEquipmentDaoImpl.setKnowbilityJdbcTemplate(context.getBean(template, org.springframework.jdbc.core.JdbcTemplate.class));
		Date fromDate = new Date(0);
		Date toDate = new Date();		
		List<EquipmentChangeHistoryInfo> equipmentChangeHistoryInfoList = subscriberEquipmentDaoImpl.retrieveEquipmentChangeHistory(28053283, "4161800063", fromDate, toDate);
		for (EquipmentChangeHistoryInfo equipmentChangeHistoryInfo : equipmentChangeHistoryInfoList) {
			writer.append(equipmentChangeHistoryInfo.toString());
		}			
		printExecutionTime(startTime, "testRetrieveEquipmentChangeHistory", writer_perf);
		writer.append("\nExit testRetrieveEquipmentChangeHistory\n");			
	}
}
