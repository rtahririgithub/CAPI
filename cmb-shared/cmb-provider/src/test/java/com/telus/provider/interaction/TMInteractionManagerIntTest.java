package com.telus.provider.interaction;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.interaction.Interaction;
import com.telus.api.reference.Brand;
import com.telus.api.reference.PricePlan;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.account.TMContract;
import com.telus.provider.account.TMPrepaidConsumerAccount;
import com.telus.provider.account.TMSubscriber;

public class TMInteractionManagerIntTest extends BaseTest {

	static {
		setupD3();
//		setupEASECA_QA();
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
	}

	public TMInteractionManagerIntTest(String name) throws Throwable {
		super(name);
	}
	private TMInteractionManager interactionManager;
	private TestTMProvider testTMProvider;
	private TMAccountManager accountManager;

	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		interactionManager = super.provider.getInteractionManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
	}

	public void testRetrieveBANInteractions() throws TelusAPIException, SecurityException, NoSuchMethodException {
		long startDateMS = System.currentTimeMillis() - ((long)500) *24*60*60*1000;   
        Date startDate = new Date();   
        startDate.setTime(startDateMS);   
        Interaction[] interactions = interactionManager.getInteractionsByBan(12474, startDate, new Date());
        assertTrue(interactions.length > 0);
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	public void testRetrieveSubscriberInteractions() throws TelusAPIException, SecurityException, NoSuchMethodException {
		long startDateMS = System.currentTimeMillis() - ((long)500) *24*60*60*1000;   
        Date startDate = new Date();   
        startDate.setTime(startDateMS);   
        Interaction[] interactions = interactionManager.getInteractionsBySubscriber("44135264", getDateInput(2000,1,1), new Date());
        assertTrue(interactions.length > 0);
	}

	public void testChangeSubscriber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4033946834");
		subscriber.setFirstName("Test");
		subscriber.setLastName("Test");
		interactionManager.subscriberSave(subscriber);
	}
	
	public void testContractSave_priceplanChange()throws TelusAPIException{
		TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
				"C40NPPM5",
				String.valueOf(subscriber.getEquipment().getEquipmentType()),
				subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				Brand.BRAND_ID_TELUS);
		
		TMContract contract =(TMContract)subscriber.newContract(pricePlan, 12);
		interactionManager.contractSave(contract, subscriber.getDealerCode(), subscriber.getSalesRepId());
	}
	public void testContractSave_serviceChange()throws TelusAPIException{
		TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
		TMContract contract =(TMContract)subscriber.getContract();
		contract.addService("SOFFRT1  ");
		interactionManager.contractSave(contract, subscriber.getDealerCode(), subscriber.getSalesRepId());
	}
	
	public void testChangeAddress()throws TelusAPIException{
		TMAccount account = (TMAccount) accountManager.findAccountByBAN(20001552);
		account.getAddress().setCity("SCR");
		account.getAddress().setProvince("ON");
		interactionManager.changeAddress(account);
	}
	
   public void testSubscriberNewCharge()throws TelusAPIException{
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   interactionManager.subscriberNewCharge(subscriber, "ACTMMK", null);
	}
   
   public void testChangePaymentMethod()throws TelusAPIException{
	   TMAccount account = (TMAccount) accountManager.findAccountByBAN(20001552);
	   interactionManager.changePaymentMethod(account, 'C', 'R');
	}
   
   public void testMakePayment()throws TelusAPIException{
	   TMAccount account = (TMAccount) accountManager.findAccountByBAN(20001552);
	   interactionManager.makePayment(account, 'C', 200);
	}
   
   public void testAccountStatusChange()throws TelusAPIException{
	   TMAccount account = (TMAccount) accountManager.findAccountByBAN(20001552);
	   interactionManager.accountStatusChange(account, true, 'O');
	}
   
   public void testSubscriberStatusChange()throws TelusAPIException{
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   interactionManager.subscriberStatusChange(subscriber, true, 'O');
	}
   
   public void testPrepaidAccountTopUp()throws TelusAPIException{
	   TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByBAN(20007121);
	   interactionManager.prepaidAccountTopUp(account, new Double(200.0), 'C', 'O');
	}
   
   public void testSubscriberChangeRole()throws TelusAPIException{
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   interactionManager.subscriberChangeRole(subscriber, "C", "AA");
	}
   
   public void testSubscriberChangePhoneNumber()throws TelusAPIException{
	   
	   AvailablePhoneNumberInfo availablePhoneNumber= new AvailablePhoneNumberInfo();
	   availablePhoneNumber.setPhoneNumber("4162060444");
	   availablePhoneNumber.setNumberLocationCode("ON");
	   availablePhoneNumber.setNumberGroup(new NumberGroupInfo());
	   
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   interactionManager.subscriberChangePhoneNumber(subscriber, availablePhoneNumber, false);
	}
   
   public void testSubscriberChangeEquipment()throws TelusAPIException{
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   
	   //interactionManager.subscriberChangeEquipment(subscriber, subscriber.getEquipment0(), subscriber.getContract0().getEquipmentChangeRequest());
	   
	}
   
   public void testSubscriberSave()throws TelusAPIException{
	   TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber("4162060438");
	   interactionManager.subscriberSave(subscriber);
	   
	}
	
}
