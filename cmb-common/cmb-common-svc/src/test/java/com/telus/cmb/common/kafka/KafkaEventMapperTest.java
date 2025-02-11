/**
 * 
 */
package com.telus.cmb.common.kafka;

import java.util.Date;

import org.junit.Test;

import com.telus.cmb.common.kafka.KafkaEventMapper;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

/**
 * @author t904649
 *
 */
public class KafkaEventMapperTest {
	
	@Test
	public void testCreateAccountCreatedEvent() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		String eventMessage = kafkaEventMapper.createAccountCreatedEvent(KafkaEventType.BILLING_ACCOUNT_ALL, createAccount());
		System.out.println(eventMessage);
	}
	
	@Test
	public void testUpdateAccountEvent() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		String eventMessage = kafkaEventMapper.createAccountCreatedEvent(KafkaEventType.BILLING_ACCOUNT_UPDATED, createAccount());
		System.out.println(eventMessage);
	}
	
	@Test
	public void testUpdateBillingInformation() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		BillingPropertyInfo billingInfo = new BillingPropertyInfo();
		billingInfo.setName(createName());
		billingInfo.setAddress(createAddress());
		int billingAccountId = 99999;
		String eventMessage = kafkaEventMapper.createBillingInfoUpdateEvent(KafkaEventType.BILLING_INFO, billingInfo, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testUpdateContactInformation() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		ContactPropertyInfo contactPropertyInfo = new ContactPropertyInfo();
		contactPropertyInfo.setName(createName());
		contactPropertyInfo.setBusinessPhoneExtension("333");
		contactPropertyInfo.setBusinessPhoneNumber("123-123-1234");
		contactPropertyInfo.setContactFax("123-FAX_HERE");
		contactPropertyInfo.setHomePhoneNumber("123-123-HOME");
		contactPropertyInfo.setOtherPhoneType("MOBILE");
		contactPropertyInfo.setOtherPhoneNumber("123-123-OTHR");
		int billingAccountId = 5555;
		String eventMessage = kafkaEventMapper.createContactInfoUpdateEvent(KafkaEventType.CONTACT_INFO, contactPropertyInfo, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testAccountStatusUpdate_Cancelled() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();

		Date activityDate = new Date();
		String activityReason = "Cancelled";
		String depositReturnMethod = "Y";
		String waiveReason = "waive reason";
		String userText = "userMemoText";
		boolean isPortActivity=true;
		boolean isBrandPortActivity=false;
		int billingAccountId = 9999;
		
		String eventMessage = kafkaEventMapper.createAccountCancelStatusUpdateEvent(KafkaEventType.BILLING_ACCOUNT_STATUS, activityDate, activityReason, depositReturnMethod, waiveReason, userText, isBrandPortActivity, isPortActivity, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testAccountStatusUpdate_Suspended() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		
		Date activityDate =  new Date();
		String activityReason = "Suspended";
		String isPortActivity = "true";
		String userMemoText = "userMemoText";
		
		int billingAccountId = 9999;
		
		String eventMessage = kafkaEventMapper.createAccountSuspendStatusUpdateEvent(KafkaEventType.BILLING_ACCOUNT_STATUS, activityDate, activityReason, userMemoText, isPortActivity, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testBillCycleUpdate() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		int billingAccountId = 9999;
		short billCycle = 6;
		String eventMessage = kafkaEventMapper.createBillCycleUpdateEvent(KafkaEventType.BILL_CYCLE, billCycle, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testEmailUpdate() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		int billingAccountId = 8888;
		String emailAddress = "test@test.com";
		String eventMessage = kafkaEventMapper.createEmailUpdateEvent(KafkaEventType.BILLING_CONTACT_EMAIL_ADDRESS, emailAddress, billingAccountId);
		System.out.println(eventMessage);
	}
	
	@Test
	public void testAuthorizedContacts() throws Exception{
		KafkaEventMapper kafkaEventMapper = new KafkaEventMapper();
		ConsumerNameInfo[] authorizedContacts = new ConsumerNameInfo[2];
		authorizedContacts[0] = new ConsumerNameInfo();
		authorizedContacts[0].setFirstName("first auth name");
		authorizedContacts[0].setLastName("first auth lastname");
		
		authorizedContacts[1] = new ConsumerNameInfo();
		authorizedContacts[1].setFirstName("first name 2");
		authorizedContacts[1].setLastName("lastname 2");
		int billingAccountId = 23232323;
		String eventMessage = kafkaEventMapper.createAuthorizedContactsUpdateEvent(KafkaEventType.AUTHORIZED_CONTACTS, authorizedContacts, billingAccountId);
		System.out.println(eventMessage);		
	}
	
	private AccountInfo createAccount() {
		AccountInfo account = new AccountInfo();
		
		account.setStatus('O');
		account.setStatusDate(new Date());
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
		account.setBillCycle(5);
		return account;
	}
	
	private ConsumerNameInfo createName(){
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("first name");
		name.setLastName("lastname");
		name.setGeneration("generation");
		name.setMiddleInitial("middle");
		name.setTitle("Title");
		return name;
	}
	
	private AddressInfo createAddress() {
		AddressInfo address = new AddressInfo();
		address.setAddressType("D");
		address.setAttention("First Lastname");
		address.setCity("cityTest");
		address.setCountry("Canada");
		address.setPoBox("PObox1234");
		address.setPostalCode("L5L 5L5");
		address.setPrimaryLine("Primary line");
		address.setProvince("Ontario");
		address.setRuralType("S");
		address.setRuralDeliveryType("T");
		address.setRuralSite("new rural site");
		address.setRuralNumber("ruralNumber");
		address.setRuralCompartment("ruralcompartment");
		address.setRuralGroup("rural group");
		address.setRuralQualifier("rural qualifier");
		address.setPrimaryLine("primary line");
		address.setSecondaryLine("secondary line");
		
		return address;
	}

}
