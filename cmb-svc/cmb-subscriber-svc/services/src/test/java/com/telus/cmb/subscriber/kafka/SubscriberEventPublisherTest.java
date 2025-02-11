package com.telus.cmb.subscriber.kafka;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.api.account.Subscriber;
import com.telus.cmb.subscriber.kafka.SubscriberEventPublisher;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;

@Test
@ContextConfiguration(locations={"classpath:application-context-test.xml","classpath:kafka-context-test.xml"})
@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "pt148"})
public class SubscriberEventPublisherTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private SubscriberEventPublisher subscriberEventPublisher;
	
	@Autowired
	private SubscriberLifecycleHelper helper;

	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("UseSunHttpHandler", "true");		
	}
	
	@Test
	public void publishSubscriberActivationEvent() throws Throwable{
		String phoneNumber = "6472148009";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		subscriberEventPublisher.publishSubscriberActivationEvent(createAccount(), subscriberInfo,contract, false,"REGULAR",getAuditInfo(),false);
	}
	
	@Test
	public void publishSubscriberCancelEvent() throws Throwable{
		String phoneNumber = "4034920310";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		subscriberEventPublisher.publishSubscriberCancelEvent(createAccount(), subscriberInfo, new Date(), "CR", "R", "test Code","customer requested the subscriber cancel..", false,getAuditInfo(),true,false);
	}
	
	@Test
	public void publishSubscriberPortOutCancelEvent() throws Throwable{
		String phoneNumber = "4034920310";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		subscriberInfo.setPortType(Subscriber.PORT_TYPE_PORT_OUT);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		subscriberEventPublisher.publishSubscriberCancelPortOutEvent(createAccount(), subscriberInfo, new Date(), "CR", true, false, getAuditInfo(),false, false);
	}
	
	@Test
	public void publishSubscriberMoveEvent() throws Throwable{
		String phoneNumber = "4034920310";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		subscriberEventPublisher.publishSubscriberMoveEvent(createAccount(), subscriberInfo, 897987, new Date(), "CR", true, "customer requested ownership change", "A00000000", "0000", getAuditInfo(),false);
	}
	
	private AuditInfo getAuditInfo(){
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("naresh");
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
		account.setBanId(70832729);
		account.setAccountType('I');
		account.setAccountSubType('R');
		account.setBrandId(1);
		account.getContactName().setFirstName("First Name");
		account.getContactName().setLastName("Last Name");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate( new Date());		
		return account;
	}
}