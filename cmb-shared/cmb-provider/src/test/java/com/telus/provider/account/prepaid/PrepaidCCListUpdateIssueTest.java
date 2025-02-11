package com.telus.provider.account.prepaid;


import junit.framework.TestCase;

import com.telus.api.AuthenticationException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.CallingFeatureCycle;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.Feature;
import com.telus.api.reference.Service;

public class PrepaidCCListUpdateIssueTest extends TestCase {


	ClientAPI api=null;
	static int banId = 0;
	static String phoneNumber = "";
	static String prepaidCCFeatureCode = "505";
	static String kbSOC = "SCC30PRPD";//KB SOC SCC30PRPD for feature 505
	static String[] ccList = {"4182257943","4182253209", "4182251796","4188561644", "4182218033" };
	static String providerLdap = "";
	static String env = "";
	
	static {
		env = "PT168";
		if ("PT168".equalsIgnoreCase(env)) {
			banId = 70689768;
			phoneNumber = "4031659716";
			providerLdap = "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration";
		} else if ("Prod".equalsIgnoreCase(env)) {
			banId = 22913668;
			phoneNumber = "7095730783";
			providerLdap = "ldap://ldapread-p.tmi.telus.com:389/cn=prod_81,o=telusconfiguration";
		} else if ("PT148".equalsIgnoreCase(env)) {
			providerLdap = "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration";
		}
	}
	
	
	private ClientAPI getAPI() throws AuthenticationException, TelusAPIException {
		System.setProperty ("com.telusmobility.config.java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", providerLdap);
		
		if (api==null) {
			api = ClientAPI.getInstance("18654", "apollo", "C_API");
		}
		return api;
	}
	
	private Subscriber getSubscriber(String subPhoneNumber) throws Throwable {
		Subscriber sub = getAPI().getAccountManager().findSubscriberByPhoneNumber( subPhoneNumber);
		if ( banId>0 ) {
			assertEquals(banId, sub.getBanId());
		}
		return sub;
	}
	
	private Contract getContract(String subPhoneNumber) throws Throwable {
		Subscriber s = getSubscriber(subPhoneNumber);
		return s.getContract();
	}
	
	private void updatePrepaidCCList(String subPhoneNumber, String[] newPhoneNumbers, int banId, String featureCode) throws Throwable	{
		
		Contract contract = getContract(subPhoneNumber);
		
		ContractFeature cf = contract.getService(featureCode).getFeature(featureCode);
		assertTrue( cf.getFeature().isPrepaidCallingCircle() );
		
		CallingFeatureCycle callingFeatureCycle = contract.calculatePrepaidFeatureCycleDates();
		System.out.println( "CallingFeatureCycle.getLastUpdateDate() before update CC list: " + callingFeatureCycle.getLastUpdateDate() );
		ContractTestHelper.showContractCallingCircleInfo(contract);

		
		changeCCListAndCommit(subPhoneNumber, newPhoneNumbers, contract, cf);
	}

	private void changeCCListAndCommit(String subPhoneNumber,	String[] newPhoneNumbers, Contract contract, ContractFeature cf) throws TelusAPIException {
		
		cf.setCallingCirclePhoneNumberList( newPhoneNumbers );
		contract.save();
		
		
		Contract contract1 =  getAPI().getAccountManager().findSubscriberByPhoneNumber(subPhoneNumber).getContract();
		System.out.println("After reload subscriber");
		ContractTestHelper.showContractCallingCircleInfo(contract1);
		
		
		CallingFeatureCycle callingFeatureCycle = contract1.calculatePrepaidFeatureCycleDates();
		System.out.println( "CallingFeatureCycle.getLastUpdateDate() after update CC list: " + callingFeatureCycle.getLastUpdateDate() );
	}
	
	public void testPrepaid_UpdateCCList_Prod_issue() throws Throwable {
		updatePrepaidCCList( phoneNumber, ccList, banId, prepaidCCFeatureCode);
	}
	
	public void testPrepaid_ReadCCList() throws Throwable {
		ContractTestHelper.showAllCallingCircleParameter(getContract(phoneNumber));
	}
	
	public void testPrepaid_AddKBFeature() throws Throwable {
		Contract contract = getContract(phoneNumber);
		Service service = getKBService(kbSOC);
		contract.addService(service);
		contract.save();
	}
	
	public void testPrepaid_RemoveKBFeature() throws Throwable {
		Contract contract = getContract(phoneNumber);
		contract.removeService(kbSOC);
		contract.save();
	}
	
	public void testPrepaid_AddPrepaidFeature() throws Throwable {
		Contract contract = getContract(phoneNumber);
		Service service = getPrepaidService(prepaidCCFeatureCode);
		contract.addService(service);
		contract.save();
	}
	
	public void testPrepaid_RemovePrepaidFeature() throws Throwable {
		Contract contract = getContract(phoneNumber);
		contract.removeService(prepaidCCFeatureCode);
		contract.save();
	}
	
	public void testPrepaid_GetKBService() throws Throwable {
		getKBService("SCC30PRPD");
	}
	
	public void testPrepaid_GetPrepaidService() throws Throwable {
		getPrepaidService("505");
	}
	
	private Service getKBService(String kbSOCCode) throws Throwable {
		Service service = getAPI().getReferenceDataManager().getRegularService(kbSOCCode);
		System.out.println("Service code: " + service.getCode());
		System.out.println("Service desc: " + service.getDescription());
		//System.out.println("Service feature count: " + service.getFeatureCount());
		return service;
	}
	
	private Service getPrepaidService(String prepaidSOCCode) throws Throwable {
		Service service = getAPI().getReferenceDataManager().getWPSService(prepaidSOCCode);
		System.out.println("Service code: " + service.getCode());
		System.out.println("Service desc: " + service.getDescription());
		//System.out.println("Service feature count: " + service.getFeatureCount());
		return service;
	}

	public void testPrepaid_ListServices() throws Throwable {
		Service[] services = getAPI().getReferenceDataManager().getWPSServices();
		for (Service service:services) {
			System.out.println("services code: " + service.getCode());
		}
	}

}
