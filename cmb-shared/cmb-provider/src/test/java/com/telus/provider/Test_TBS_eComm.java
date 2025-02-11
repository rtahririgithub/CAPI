package com.telus.provider;

/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

import com.telus.api.BrandNotSupportedException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.Contract;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.InvalidAddressException;
import com.telus.api.account.Memo;
import com.telus.api.account.PCSPostpaidBusinessRegularAccount;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.ReasonType;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;

public class Test_TBS_eComm{

	private static ClientAPI api = null;

	private static void setupCommon() {
		//common properties
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider",	"com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.hcd.ldapEntryNameCommon","HCD");
	}
	
	public static void setupD3() {
		System.setProperty("com.telus.provider.providerURL", "t3://wld3easeca:8382");
		System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		System.setProperty("com.telus.credit.providerURL", "t3://wld3eascommonservices:8323");
	}
	
	public static void setupPT168() {
		System.setProperty("com.telus.provider.providerURL", "t3://wlpt168easeca:8682");
		System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
		System.setProperty("com.telus.credit.providerURL", "t3://wlpt168eascommonservices:8623");
		
	}

	public static void setupPT148() {
		System.setProperty("com.telus.provider.providerURL", "t3://wlqaeaseca:8682");
		System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		System.setProperty("com.telus.credit.providerURL", "t3://wlqaeascommonservices:8623");
		
	}
	
	public static void setupStaging() {
		System.setProperty("com.telus.provider.providerURL", "t3://wlseaseca:11182");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
		System.setProperty("com.telus.credit.providerURL", "t3://wlseascommonservices:11123");
		
	}
	
	public static void main(String[] argv) throws Throwable {

	  	setupCommon();
//		setupD3();
//	  	setupStaging();
 	    setupPT148();
//		setupStaging();
	    //setupPT168();
		
		
		//--------------------------------
		// Connect to business objects.
		//--------------------------------
		try {
			System.out.println("Getting instance of ClientAPI...");
			api = ClientAPI.getInstance("18654","apollo","ATG_APP");
			System.out.println(api.getDescription());
			
			
			Test_TBS_eComm testRun = new Test_TBS_eComm();
	
			//Test methods available
			testRun.BAN_Lookup();
		testRun.address_Validation();
			testRun.manditoryAddOn();
		testRun.BANCreation();
		testRun.RecordPayment();
			
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private static void saveContract(Subscriber subscriber, Contract contract) throws TelusAPIException {
		try {
			contract.save();
		} catch (TelusAPIException e) {
			throw e;
		}
	}
	
	
	//#1 BAN Lookup
	public void BAN_Lookup() throws Throwable {
		int banId = 6021848;  
		int[] banIds = {6016549,6016549,6016913}; 	
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		System.out.println("Account status: " + account.getStatus());
		System.out.println("Account type: " + account.getAccountType());
		System.out.println("Account sub-type: " + account.getAccountSubType());
		
		Account[] accounts = api.getAccountManager().findAccountsByBANs(banIds);
		System.out.println(accounts.length + " of " + banIds.length + " were successfully retrieved");
	}
	
	//#2 Address Validation
	public void address_Validation() throws Throwable {
      
		Account account = api.getAccountManager().newPCSPostpaidBusinessRegularAccount();
		
		Address address = account.getAddress();
		
		address.setAddressType("C");
        address.setPrimaryLine("200 CONSILIUM PL SUITE 1600");
        address.setCity("SCARBOROUGH");
        address.setProvince("ON");
        address.setPostalCode("M1H3J3");//Valid postal: M1H3J3; invalid postal code: M1G3H2
        address.setCountry("CAN");        
        address.setStreetNumber("200");
        address.setStreetName("CONSILIUM");
        address.setStreetType("PL");
        address.setUnitType("SUITE");
        address.setUnit("1600");
       
        try {
        	address.validate();
        } catch (InvalidAddressException iae) {
        	System.out.println("******** PRINTING ALTERNATIVE ADDRESS ********");
        	System.out.println(iae.getSuggestedAddress().toString());
        }
       
        account = null;
	}
	
	//#3 Mandatory add-on
	public void manditoryAddOn() throws Throwable {
		
		//PT148 data
		String ppCode = "PCAPDSS50"; 
		String serialNo= "7781760305";
		
//		Equipment equipment = api.getEquipmentManager().getEquipment(serialNo);
//		equipment.getNetworkType();
		
		//The following example shows same PricePlan code, have different mandatory service set depends on the equipment type
		
		showMandatoryServices(ppCode, Equipment.EQUIPMENT_TYPE_PDA, NetworkType.NETWORK_TYPE_CDMA);
		showMandatoryServices(ppCode, Equipment.EQUIPMENT_TYPE_RIM, NetworkType.NETWORK_TYPE_CDMA);

	}

	private void showMandatoryServices(String ppCode, String equipmentType,	String networkType) throws TelusAPIException {
		
		//Note: No parameters in the following call shall be hard coded, everything shall be based on runtime context.
		PricePlan plan = api.getReferenceDataManager().getPricePlan(ppCode, equipmentType, "ON", Account.ACCOUNT_TYPE_CONSUMER, Account.ACCOUNT_SUBTYPE_PCS_REGULAR, Brand.BRAND_ID_TELUS);
		
		//when retrieve mandatory service set, need to use device's real equipmentType and networkType 
		ServiceSet[] mandatoryServiceSet = plan.getMandatoryServiceSets(equipmentType, networkType);

		System.out.println("# of Manditory Services found for call to getMandatoryServiceSets(String, String): " + mandatoryServiceSet.length + " found");
		for (int i=0; i<mandatoryServiceSet.length; i++) {
			System.out.println("  Manditory ServiceSet " + i + ": [Code: " + mandatoryServiceSet[i].getCode() + " -> Description: " + mandatoryServiceSet[i].getDescription() + "]");
			
			Service[] mandatoryServices = mandatoryServiceSet[i].getServices();
			
			//These are the services that need to be presented to user to select!!!
			for ( int k=0; k<mandatoryServices.length; k++ ) {
				System.out.println("    service " + k + ": [Code: " + mandatoryServices[k].getCode() + " -> Description: " +mandatoryServices[k].getDescription() + "]");
			}
		}
	}

	
	//#5 BAN creation  (successfully ran against D3)
	public void BANCreation() throws Throwable {
	
		//Ex. accountManager.newPCSPostpaidBusinessRegularAccount() 
		System.out.println("    Creating a new PCSPostpaidBusinessRegularAccount...");		
		PCSPostpaidBusinessRegularAccount pcsPostpaidBusinessRegularAccount = api.getAccountManager().newPCSPostpaidBusinessRegularAccount();
		setupBusinessAccount(pcsPostpaidBusinessRegularAccount, true);
		System.out.println("PCSPostpaidBusinessRegularAccount created with BAN = " + pcsPostpaidBusinessRegularAccount.getBanId());		
	}
	
	//#8 Record payment
	public void RecordPayment() throws Throwable {
		String MEMO_DEPOSIT_PAYMENT = "ADEP";
		int subscriberCount = 1; //subscriber count to be provided by calling (client) application
		double amount = 10.00; //subscriber count to be provided by calling (client) application

		int banId = 6005936;  
		Account account = api.getAccountManager().findAccountByBAN(banId);
		
		Memo memo = account.newMemo(); 

		memo.setMemoType(MEMO_DEPOSIT_PAYMENT);
		memo.setText(subscriberCount + " subscriber deposits paid ($" + amount +
		") for an active total of "
		+ (account.getActiveSubscribersCount() + subscriberCount)); //subscriberCount passed into payDeposit method

		memo.create(); 

	}
	
	public void getBusinessIdentities() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		int banId = 6005936;		
		PostpaidBusinessRegularAccount account = (PostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(banId);
		BusinessCreditIdentity[] creditIdentities = account.getBusinessCreditIdentities();
		System.out.println( "Business Identities: "  + creditIdentities );
	}
	
	private void setupBusinessAccount(PostpaidBusinessRegularAccount account, boolean save) throws Throwable {

		account.setLegalBusinessName("PURCELL");		
		account.getContactName().setTitle("MR.");
		account.getContactName().setFirstName("API");
		account.getContactName().setMiddleInitial("");
		account.getContactName().setLastName("TEST");
		setupAccount(account, save);
		
		if (save ) {
			//#6 Credit Check
			System.out.println("get business credit identities." );
			BusinessCreditIdentity[] creditIdentities = account.getBusinessCreditIdentities();
			if (creditIdentities != null) {
				CreditCheckResult result = account.checkCredit(creditIdentities[0]);
				
				System.out.println("Result from Credit Check: [ " + 
						"\nCredit Class -> " + result.getCreditClass() + ", " +
						"\nCredit result status -> " + result.getCreditCheckResultStatus() + ", " + 
						"\nDeposit -> " + result.getDeposit() + " ]");
				System.out.println("    Account CreditCheckResult=["+account.getCreditCheckResult()+"]");  								
			}
			
			CreditCheckResult eligibility = account.checkNewSubscriberEligibility(1, 0);
			System.out.println("Result from Eligibility Check: [ " + 
					"\nCredit Class -> " + eligibility.getCreditClass() + ", " +
					"\nCredit result status -> " + eligibility.getCreditCheckResultStatus() + ", " + 
					"\nDeposit -> " + eligibility.getDeposit() + " ]");

			//Iterate through AcvtivationOption
			ActivationOption[] activationOptions = eligibility.getActivationOptions();
			
			for( int i=0; i<activationOptions.length; i++  ) {
				ActivationOption option = activationOptions[i];
				System.out.print("\n activation option[" + i + "]");
				System.out.print( " type: " + option.getOptionType().getName() );
				System.out.print( "\n   class:" + option.getCreditClass());
				System.out.print( "   creditLimit:" + option.getCreditLimit());
				System.out.print( "   deposit:" + option.getDeposit());
				System.out.print( "   max contract term:" + option.getMaxContractTerm());
				System.out.println();
			}

		}
	}
	
	
	private void setupAccount(Account account, boolean save) throws Throwable {
	    AccountType accountType = api.getReferenceDataManager().getAccountType(account);

	    account.setAccountCategory("R");
	    account.setBanSegment("TBSQ");
	    account.setBanSubSegment("BPR");
	    account.setPin("2222");
	    account.setLanguage("EN");
	    account.setDealerCode(accountType.getDefaultDealer());
	    account.setSalesRepCode(accountType.getDefaultSalesCode());
	    account.setBrandId(Brand.BRAND_ID_TELUS);

	    account.setEmail("dele.taylor@telusmobility.com");
	    account.setHomePhone("4165551234");
	    account.setBusinessPhone("4165550000");
	    account.setContactPhone("4165550000");
	    account.setContactPhoneExtension("");

	    account.getAddress().setAddressType(Address.ADDRESS_TYPE_CITY);
	    account.getAddress().setAttention("Tech Dept.");
	    account.getAddress().setStreetNumber("90");
	    account.getAddress().setStreetName("gerrard street");
	    account.getAddress().setStreetDirection("");
	    account.getAddress().setStreetNumberSuffix("");
	    account.getAddress().setStreetType("ST");
	    account.getAddress().setCity("Toronto");
	    account.getAddress().setPoBox("");
	    account.getAddress().setPrimaryLine("");
	    account.getAddress().setProvince("ON");
	    account.getAddress().setPostalCode("m5g1j6");
	    account.getAddress().setCountry("CAN");
	    account.getAddress().setRr("");
	    account.getAddress().setRuralCompartment("");
	    account.getAddress().setRuralDeliveryType("");
	    account.getAddress().setRuralGroup("");
	    account.getAddress().setRuralLocation("");
	    account.getAddress().setRuralNumber("");
	    account.getAddress().setRuralQualifier("");
	    account.getAddress().setRuralSite("");
	    account.getAddress().setRuralType("");	    
	    account.getAddress().setUnit("");
	    account.getAddress().setUnitType("");
	    
	    
	    if (save) {
	    	System.out.println("    Saving...");
	    	account.save();
	    	System.out.println("    Account BAN=["+account.getBanId()+"]");
	    	System.out.println("    Checking Credit...");		      	     
	    }	
	}

}
