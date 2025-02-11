package com.telus.cmb.subscriber.kafka;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.subscriber.kafka.ServiceAgreementEventPublisher;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;

@Test
@ContextConfiguration(locations={"classpath:application-context-test.xml","classpath:kafka-context-test.xml"})
@ActiveProfiles("standalone")
public class ServiceAgreementEventPublisherTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private ServiceAgreementEventPublisher serviceAgreementEventPublisher;
	
	@Autowired
	private SubscriberLifecycleHelper helper;
	
	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("UseSunHttpHandler", "true");
	}
	
	
	@Test
	public void publish_ChangeEquipmentEvent_for_smartSpeakerSoc() throws Exception {
		int ban = 1234;
		String dealerCode = "5678";
		String salesRepCode = "9012";
		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setBanId(ban);
		accountInfo.setAccountType('I');
		accountInfo.setAccountSubType('R');
		accountInfo.setBrandId(1);
		accountInfo.setStatus('A');
		
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setSubscriberId("1234567890");
		
		EquipmentInfo equipmentInfo = new EquipmentInfo();
		equipmentInfo.setSerialNumber("8912239900006601282");
		equipmentInfo.setEquipmentType("U");
		
		EquipmentInfo oldEquipmentInfo = new EquipmentInfo();
		equipmentInfo.setSerialNumber("8912239900006601283");
		equipmentInfo.setEquipmentType("U");
		
		serviceAgreementEventPublisher.publishServiceAgreementChangeEvent(createAccount(), subscriberInfo, null, null, equipmentInfo,dealerCode,salesRepCode, getAuditInfo(),false);
	}
	
	@Test
	public void publishServiceAgreementChangeEvent() throws Throwable{
		System.out.println("begin publishServiceAgreementChangeEvent...");

		String phoneNumber = "4034920310";
		
		String dealerCode = "A001000001";
		String salesRepCode = "0000";
		
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo newContract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		
		
		newContract.setPricePlanInfo(createPriceplanInfo(newContract.getCode()));
		newContract.setPricePlanChange(true);
		
		SubscriberContractInfo oldContract = newContract;// just assign new contract as old contract for testing
		
	//	String jsonString = SubscriberEventPayloadFactory.createServiceAgreementChangeEventV2(subscriberInfo, createAccount(), newContract, oldContract, null,null,false,null);
		//System.out.println(jsonString);
		
		serviceAgreementEventPublisher.publishServiceAgreementChangeEvent(createAccount(), subscriberInfo, newContract, oldContract, null,dealerCode,salesRepCode, getAuditInfo(),false);
		
		System.out.println("end publishServiceAgreementChangeEvent...");
	}
	
	
	private AuditInfo getAuditInfo() {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOutletId("642431");
		auditInfo.setSalesRepId("ftsale");
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("18654");
		return auditInfo;
	}
   
	private PricePlanInfo createPriceplanInfo(String priceplanCode) {
		PricePlanInfo pricePlan = new PricePlanInfo();
		pricePlan.setCode(priceplanCode);
		pricePlan.setServiceType("P");
		pricePlan.setDescription(priceplanCode+"English Des");
		pricePlan.setDescriptionFrench(priceplanCode+"English French");
		pricePlan.setRecurringCharge(97);
		return pricePlan;
	}
	
	private AccountInfo createAccount() {
		AccountInfo account = new AccountInfo();
		account.setBanId(70892498);
		account.setAccountType('I');
		account.setAccountSubType('R');
		account.setBrandId(1);
		account.getContactName().setFirstName("First Name");
		account.getContactName().setLastName("Last Name");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate( new Date());	
		account.setBanSegment("TCSI");	
		account.setBanSubSegment("TCSI-SUB");

		return account;
	}
}