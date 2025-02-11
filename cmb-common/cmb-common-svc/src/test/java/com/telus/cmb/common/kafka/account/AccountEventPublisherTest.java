/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.kafka.account;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityUtil;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;

/**
 *
 */

@Test
@ContextConfiguration(locations="classpath:com/telus/cmb/common/kafka/account/application-context-test.xml")
public class AccountEventPublisherTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private AccountEventPublisher publisher;
	
	@Test
	public void publishBillCycleUpdateEvent() throws Throwable{
		
		short billCycle = 4;
		int billingAccountId = 1234567;
		publisher.publishBillCycleUpdateEvent(billCycle , billingAccountId , createSessionId());
	}
	
	@Test
	public void publishBillingInformationUpdate() throws Throwable{
		int billingAccountId = 5555555;
		publisher.publishBillingInfoUpdateEvent(createBillingPropertyInfo(), billingAccountId, createSessionId());
	}
	
	@Test
	public void publishContactInformationUpdate() throws Throwable{
		int billingAccountId = 88888;
		publisher.publishContactInfoUpdateEvent(createContactPropertyInfo_ContactNameOnly(), billingAccountId, createSessionId());
	}
	

	@Test
	public void publishAccountCreatedEvent() throws Throwable {
		publisher.publishAccountCreatedEvent(createAccount(), createSessionId());
	}
	
	@Test
	public void publishPostpaidAccountCreatedEvent() throws Throwable{
		publisher.publishAccountCreatedEvent(createPostpaidAccount(), createSessionId());
	}
	
	@Test
	public void publishAccountStatusUpdateEvent() throws Throwable{

		Date activityDate = new Date();
		String activityReason = "Cancelled";
		String depositReturnMethod = "Y";
		String waiveReason = "waive reason";
		String userText = "userMemoText";
		boolean isPortActivity=true;
		boolean isBrandPortActivity=false;
		int billingAccountId = 9999;
		publisher.publishAccountCancelStatusUpdateEvent(activityDate, activityReason, depositReturnMethod, waiveReason, userText, isBrandPortActivity, isPortActivity, billingAccountId, createSessionId());
	}
	
	@Test
	public void publishAccountUpdatedEvent() throws Exception {
		publisher.publishAccountUpdatedEvent(createAccountUpdated_BAN_70750602(), createSessionId());
	}
	
	@Test
	public void publishAuthorizedContactsUpdate() throws Exception {
		ConsumerNameInfo[] authorizedContacts = new ConsumerNameInfo[2];
		authorizedContacts[0] = new ConsumerNameInfo();
		authorizedContacts[0].setFirstName("first auth name");
		authorizedContacts[0].setLastName("first auth lastname");
		authorizedContacts[1] = new ConsumerNameInfo();
		authorizedContacts[1].setFirstName("first name 2");
		authorizedContacts[1].setLastName("lastname 2");
		int billingAccountId = 23232323;
		publisher.publishAuthorizedContactsUpdateEvent(authorizedContacts, billingAccountId, createSessionId());
	}
	
	private AccountInfo createAccount() {
		AccountInfo account = new AccountInfo();
		
		account.setBanId(8);
		account.setAccountType('I');
		account.setAccountSubType('R');
		account.setBrandId(1);
		account.getContactName().setFirstName("First Name");
		account.getContactName().setLastName("Last Name");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate( new Date());
		account.setAddress0(createAddress());
		
		return account;
	}
	
	private AccountInfo createAccountUpdated_BAN_70750602() {
		AccountInfo account = new AccountInfo();
		java.util.Date date= new java.util.Date();
		account.setStatusDate(new Timestamp(date.getTime()));
		account.setStatus('O');
		account.setBanId(70750602);
		account.setBillCycleCloseDay(28);
		account.setCustomerId(70750602);
		account.setAccountType('I');
		account.setAccountSubType('Q');
		account.setBillCycle(88);
		account.setNextBillCycle(0);
		account.setStartServiceDate(new Timestamp(date.getTime()));
		account.setLanguage("EN");
		account.setBrandId(1);
		account.setHomePhone("9999999999");
		account.setBusinessPhone("9865326598");
		account.setBusinessPhoneExtension("123");
		account.setOtherPhone("9878451245");
		account.setOtherPhoneExtension("123");
		account.getContactName().setTitle("MISS");
		account.getContactName().setFirstName("Sara");
		account.getContactName().setLastName("Test");
		account.getContactName().setNameFormat("P");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate( new Date());
		account.setAddress0(createAddress_Update());
		
		return account;
	}
	
	private AccountInfo createPostpaidAccount(){
		AccountInfo accountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		accountInfo.setStatus('O');
		accountInfo.setAccountSubType('R');
		accountInfo.setAccountType('I');
		accountInfo.setBrandId(1);
		accountInfo.setBanSegment("TCSO");
		accountInfo.setAddress0(createAddress());
		accountInfo.setEmail("sara@test.com");
		accountInfo.setLanguage("EN");
		((PostpaidConsumerAccountInfo)accountInfo).getName0().setTitle("Mr");
		((PostpaidConsumerAccountInfo)accountInfo).getName0().setFirstName("Jim");
		((PostpaidConsumerAccountInfo)accountInfo).getName0().setLastName("Bobanner");
		accountInfo.setHomePhone("555-555-5555");
		accountInfo.setBusinessPhone("333-333-3333");
		
		return accountInfo;
	}
	
	private AddressInfo createAddress_Update() {
		AddressInfo address = new AddressInfo();
		address.setAddressType("C");
		address.setCountry("CAN");
		address.setCity("TORONTO");
		address.setPostalCode("M1H3J3");
		address.setProvince("ON");
		address.setStreetName("CONSILIUM PL");
		address.setUnit("1A");
		address.setUnitType("APT");
		address.setCivicNo("200");
		return address;
	}
	
	private AddressInfo createAddress() {
		AddressInfo address = new AddressInfo();
		address.setAddressType("M");
		address.setAttention("First Lastname");
		address.setStreetName("King Street");
		address.setCity("cityTest");
		address.setProvince("ON");
		address.setCountry("Canada");
		address.setPoBox("PObox1234");
		address.setPostalCode("L5L 5L5");
		address.setPrimaryLine("Primary line");
		address.setProvince("Ontario");
		address.setRuralDeliveryType("T");
		address.setRuralNumber("ruralNumber");
		address.setRuralQualifier("rural qualifier");
		
		return address;
	}
	
	private ContactPropertyInfo createContactPropertyInfo_ContactNameOnly() {
		ContactPropertyInfo contactInfo = new ContactPropertyInfo();
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("CAPI");
		contactInfo.setName(name);
		return contactInfo;
	}

	private BillingPropertyInfo createBillingPropertyInfo() {
		BillingPropertyInfo billingInfo = new BillingPropertyInfo();
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("CAPI");
		billingInfo.setName(name);
		//billingInfo.setAddress(createAddress());
		return billingInfo;
	}
	
	private String createSessionId() {
		String sessionId = null;
		ClientIdentity clientIdentity = new ClientIdentity();
		clientIdentity.setApplication("1234");
		clientIdentity.setCredential("credential");
		clientIdentity.setPrincipal("principal");
		try {
			ClientIdentityUtil ciUtil = new ClientIdentityUtil();
			sessionId =ciUtil.encrypt(clientIdentity);	
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return sessionId;
	}
}
