package com.telus.cmb.subscriber.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.utilities.activation.ActivationPostTask;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PricePlanInfo;

@Test
@ContextConfiguration(locations={"classpath:application-context-test.xml"})
@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "pt148"})
public class ActivationPostTaskTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private ActivationPostTask activationPostTask;
	
	@Autowired
	private SubscriberLifecycleHelper helper;

	
	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("UseSunHttpHandler", "true");	
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
	}
	
	@Test
	public void applySubscriberActivationPostTasks() throws Throwable{
		String phoneNumber = "6471155313";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		// since we are testing the new activation post tasks and make sure contract.getPricePlan is not null.
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("naresh");
		activationPostTask.initialize(subscriberInfo, contract, false, PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, null, false, true, auditInfo, "9C/mZDrSmOGydSbg/xlH2rOhxkXslzpPnY4cxZzRiuU=");
		activationPostTask.apply();
	}
	
	
	@Test
	public void applyReservedSubscriberActivationPostTasks() throws Throwable{
		String phoneNumber = "6471155313";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		SubscriberContractInfo contract = helper.retrieveServiceAgreementByPhoneNumber(phoneNumber);
		// since we are testing the new activation post tasks and make sure contract.getPricePlan is not null.
		contract.setPricePlanInfo(createPriceplanInfo(contract.getCode()));
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("naresh");
		activationPostTask.initialize(subscriberInfo, contract, false, null, null, true, true, auditInfo, "9C/mZDrSmOGydSbg/xlH2rOhxkXslzpPnY4cxZzRiuU=");
		activationPostTask.applyReservedSubscriberPostTasks();
	}
	
	private ClientIdentity getClientIdentity(){
		ClientIdentity identity = new ClientIdentity();
		identity.setApplication("SMARTDESKTOP");
		identity.setPrincipal("18654");
		return identity;
	}
	
	private PricePlanInfo createPriceplanInfo(String priceplanCode) {
		PricePlanInfo pricePlan = new PricePlanInfo();
		pricePlan.setCode(priceplanCode);
		pricePlan.setServiceType("P");
		pricePlan.setDescription(priceplanCode+"English Des");
		pricePlan.setDescriptionFrench(priceplanCode+"French Des");
		pricePlan.setRecurringCharge(97);
		return pricePlan;
	}
	
}