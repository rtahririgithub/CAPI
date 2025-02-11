package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceDao;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SeatDataInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-provisioning-dao-test.xml"})
public class WirelessProvisioningServiceDaoTest {
	@Autowired
	private WirelessProvisioningServiceDao wirelessProvisioningServiceDao;
	
	@BeforeClass
	public static void beforeClass() {	
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
}		
	@Test
	public void testCreateAccountCancelRequest() throws Throwable {
		
		//1. Account Cancel Test
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createAccountCancelRequest(100, 1, new Date(), "TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);
		
	}
	
	@Test
	public void testCreateAccountAddRequest() throws Throwable {
		//2. Account Add Test
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(453454);
		subscriberInfo.setBrandId(1);
		subscriberInfo.setSubscriptionId(5656556);
		subscriberInfo.setPhoneNumber("5656556565");
		SeatDataInfo seatDataInfo = new SeatDataInfo();
		seatDataInfo.setSeatType("START");
		seatDataInfo.addSeatResource("V", "416678678");
		subscriberInfo.setSeatData(seatDataInfo);
		
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setEmail("naresh-pt148test@telus.com");
		ConsumerNameInfo nameInfo = new ConsumerNameInfo();
		nameInfo.setFirstName("naresj");
		nameInfo.setLastName("soa");
		accountInfo.getContactName0().copyFrom(nameInfo);
		accountInfo.setContactPhone("4033409558");
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setCity("dsdsad");
		addressInfo.setPostalCode("M8Y7H6");
		addressInfo.setCountry("CAN");
		addressInfo.setStreetName("AHUJHJH");
		addressInfo.setProvince("ON");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createAccountAddRequest(accountInfo,subscriberInfo,new Date(),addressInfo);
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);
	}
	
	@Test
	public void testCreateAccountSuspendRequest() throws Throwable {
		//3. Account Suspend Test
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createAccountSuspendRequest(1, 1, new Date(), "TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateSeatAddRequest() throws Throwable {
		//4. Seat ADD Test
				SubscriberInfo subscriberInfo = new SubscriberInfo();
				subscriberInfo.setBanId(84);
				subscriberInfo.setBrandId(1);
				subscriberInfo.setSubscriptionId(1000029);
				subscriberInfo.setPhoneNumber("4033409558");
				SeatDataInfo seatDataInfo = new SeatDataInfo();
				seatDataInfo.setSeatType("START");
				seatDataInfo.addSeatResource("V", "4166880002");
				subscriberInfo.setSeatData(seatDataInfo);
						
				AccountInfo accountInfo = new AccountInfo();
				accountInfo.setEmail("john@telus.com");
				ConsumerNameInfo nameInfo = new ConsumerNameInfo();
				nameInfo.setFirstName("john");
				nameInfo.setLastName("cusack");
				accountInfo.getContactName0().copyFrom(nameInfo);
				accountInfo.setContactPhone("4033409558");
				AddressInfo addressInfo = new AddressInfo();
				ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createSeatAddRequest(accountInfo,subscriberInfo,new Date(),addressInfo);
				wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateSeatCancelRequest() throws Throwable {
		//5. Seat Cancel Test
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(84);
		subscriberInfo.setBrandId(1);
		subscriberInfo.setSubscriptionId(1000029);
		subscriberInfo.setPhoneNumber("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createSeatCancelRequest(subscriberInfo,new Date());
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateSeatSuspendRequest() throws Throwable {
		//6. Seat Suspend Test
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(84);
		subscriberInfo.setBrandId(1);
		subscriberInfo.setSubscriptionId(1000029);
		subscriberInfo.setPhoneNumber("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createSeatSuspendRequest(subscriberInfo,new Date(),"TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateSeatResumeRequest() throws Throwable {
		//7. Seat Resume Test
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(84);
		subscriberInfo.setBrandId(1);
		subscriberInfo.setSubscriptionId(1000029);
		subscriberInfo.setPhoneNumber("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createSeatResumeRequest(subscriberInfo,new Date(),"TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateMultiSeatCancelRequest() throws Throwable {
		//8. Multiple Seat Cancel Test
		List<String> subscriptionIdList =  new ArrayList<String>();
		subscriptionIdList.add("1000029");
		List<String> subscriberPhonerNumberList = new ArrayList<String>();
		subscriberPhonerNumberList.add("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createMultiSeatCancelRequest(1,1,subscriptionIdList,subscriberPhonerNumberList,new Date());
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateMultiSeatSuspendRequest() throws Throwable {
		//9. Multiple Seat Suspend Test
		List<String> subscriptionIdList =  new ArrayList<String>();
		subscriptionIdList.add("1000029");
		List<String> subscriberPhonerNumberList = new ArrayList<String>();
		subscriberPhonerNumberList.add("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createMultiSeatSuspendRequest(1,1,subscriptionIdList,subscriberPhonerNumberList,new Date(),"TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateMultiSeatResumeRequest() throws Throwable {
		//10. Multiple Seat Resume Test
		List<String> subscriptionIdList =  new ArrayList<String>();
		subscriptionIdList.add("1000029");
		List<String> subscriberPhonerNumberList = new ArrayList<String>();
		subscriberPhonerNumberList.add("4033409558");
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createMultiSeatResumeRequest(1,1,subscriptionIdList,subscriberPhonerNumberList,new Date(),"TEST");
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}
	
	@Test
	public void testCreateVOIPChangeRequest() throws Throwable {
		//11. VOIP Change Test
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setBanId(84);
		subscriberInfo.setBrandId(1);
		subscriberInfo.setSubscriptionId(1000029);
		
		List<ResourceActivityInfo> resourceList = new ArrayList<ResourceActivityInfo>();
		ResourceActivityInfo resourceActivityInfo = new ResourceActivityInfo();
		ResourceInfo resourceInfo = new ResourceInfo();
		resourceInfo.setResourceType("V");
		resourceInfo.setResourceNumber("4166880002");
		resourceActivityInfo.setResource(resourceInfo);
		resourceActivityInfo.setResourceActivity("A");
		resourceList.add(resourceActivityInfo);
		ProvisioningRequestInfo provisioningRequestInfo = WirelessProvisioningServiceRequestFactory.createAddRemoveVOIPChangeRequest(subscriberInfo, new Date(), resourceList,true);
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);		
	}

	@Test
	public void ping() throws Throwable {
		//wirelessProvisioningServiceDao.
		
	}
}
