package com.telus.api.subscriber;

import java.util.Calendar;

import com.telus.api.BaseTest;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.Service;

public class SubscriberTest extends BaseTest {

	static {
		//setupEASECA_CSI();
		setupK();
		//setupEASECA_QA();
		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
		//System.setProperty("cmb.services.AccountInformationHelper.url", "t3://174.23.14.97:7001");
	}	

	public SubscriberTest(String name) throws Throwable {
		super(name);
	}
	

	public void testFindSubscirber() throws Throwable {
		PCSSubscriber subscriber = (PCSSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("7781811475");
		System.out.println(" Serial Number " + subscriber.getSerialNumber());
		Equipment equipment = subscriber.getEquipment();
		System.out.println(" Equipment information " + equipment);
	}
	
	public void testChangePricePlancSubscriber() throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161646366");
		PricePlanSummary pricePlanSummary = api.getReferenceDataManager().getPricePlan("PNTS55BB");
		PricePlan plan = api.getReferenceDataManager().getPricePlan(pricePlanSummary.getCode(), subscriber.getEquipment().getEquipmentType(), subscriber.getMarketProvince(), subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), subscriber.getBrandId());
		Contract contract = subscriber.newContract(plan, 0);
//		Service service = api.getReferenceDataManager().getRegularService("SPTNFCDCA");
//		contract.addService(service);
		contract.save();
	}
	
	public void testPrintContractInformation() throws Throwable {
		Subscriber subscriber = api.getAccountManager()
				.findSubscriberByPhoneNumber("5871753908");
		ContractService[] services = subscriber.getContract()
				.getOptionalServices();

		for (ContractService service : services) {
			System.out.println("Optional Contract Service [" + service + "]");
		}

		ContractService[] includedServices = subscriber.getContract()
				.getIncludedServices();

		for (ContractService service : includedServices) {
			System.out.println("Include Contract Service [" + service + "]");
		}

		ContractFeature[] contractFeatrues = subscriber.getContract()
				.getFeatures();

		for (ContractFeature feature : contractFeatrues) {
			System.out.println("Include Contract feature [" + feature + "]");
		}

	}
	
	
	public void testSocSubscriber() throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781811475");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		
		for(ContractService service:services){
		  System.out.println("Contract Service [" + service + "]");
		}
		
		Service service = api.getReferenceDataManager().getRegularService("XSPES80M");

		System.out.println(service);
		ContractService contractService = subscriber.getContract().getService("XSPES80M");
		System.out.println("Contract Service [" + contractService + "]");

	}

	public void testFutureDateSocEffectiveDate() throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("5871753908");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		
		for(ContractService service:services){
		  System.out.println("Contract Service [" + service + "]");
		}
		
		Service service = api.getReferenceDataManager().getRegularService("SCUSRP25");

		System.out.println(service);
		ContractService contractService = subscriber.getContract().getService("SCUSRP25");
		System.out.println("Contract Service [" + contractService + "]");
		Calendar cal = Calendar.getInstance();
		cal.setTime(contractService.getEffectiveDate());
		cal.add(Calendar.HOUR, 48);
		contractService.setEffectiveDate(cal.getTime());
		 subscriber.getContract().save();
	}	
	
	public void testAddSocSubscriber() throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781811475");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		System.out.println("Subscriber information [ " +  subscriber.getPricePlan()+ ", " +  subscriber.getBrandId() + ", " + subscriber.getBanId()+  "]");
		for(ContractService service:services){
		  System.out.println("Contract Service [" + service + "]");
		}
		Service service = api.getReferenceDataManager().getRegularService("XSPES80M");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 240);
		subscriber.getContract().addService(service, cal.getTime(), null);
		subscriber.getContract().save();
	}
	
	
	public void testRemoveSocSubscriber() throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4160714949");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		System.out.println("Subscriber information [ " +  subscriber.getPricePlan()+ ", " +  subscriber.getBrandId() + ", " + subscriber.getBanId()+  "]");
		for(ContractService service:services){
		  System.out.println("Contract Service [" + service + "]");
		}
	
		subscriber.getContract().save();
	}

	public void testPrintSubscriber() throws Throwable{
		Service service = api.getReferenceDataManager().getRegularService("3STAB01");
		 System.out.println("Contract Service [" + service + "]");	

	}
	
	public void testRenewContract()throws Throwable{
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161800346");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		System.out.println("Subscriber information [ " +  subscriber.getPricePlan()+ ", " +  subscriber.getBrandId() + ", " + subscriber.getBanId()+  "]");
		for(ContractService service:services){
		  System.out.println("Contract Service [" + service + "]");
		}
		
		Contract contract = subscriber.renewContract(24);
		for(ContractService service:contract.getOptionalServices()){
			  System.out.println("after Contract Service [" + service + "]");
		}
		
		for(ContractService service:contract.getDeletedServices()){
			  System.out.println("Deleted Service [" + service + "]");
		}
		
			
	}

}
