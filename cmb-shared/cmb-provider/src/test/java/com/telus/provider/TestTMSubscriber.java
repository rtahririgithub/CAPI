package com.telus.provider;



import java.util.Date;

import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriptionRole;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ReferenceDataManager;


public class TestTMSubscriber {
 
	public static void setupPT148() {
		System.setProperty("com.telus.provider.providerURL", "t3://wlqaeaseca:8682");
		System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
		System.setProperty("com.telus.credit.providerURL", "t3://wlqaeascommonservices:8623");
		
	}
	private static void setupCommon() {
		//common properties
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.provider",	"com.telus.provider.config.PropertiesOverridingConfigurationProvider");
		System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
		System.setProperty("com.telusmobility.config.hcd.ldapEntryNameCommon","HCD");
	}



	
	
	
	private static void saveExistingSubscriberWithNewRole(AccountManager accountManager ){
		System.out.println("Save existing Subscriber with new Role*** Start");
		try{
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("4037109656");
			SubscriptionRole newRole = subscriber.newSubscriptionRole();
			newRole.setCode("12");
			newRole.setCsrId("555");
			newRole.setDealerCode("0000029016");
			newRole.setSalesRepCode("0000");
			subscriber.setSubscriptionRole(newRole);
			// ((TMSubscriber)subscriber).setModified();
			subscriber.save();
		} 
		catch (Exception e) {
			System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Save existing Subscriber with new Role*** End");
	}

	private static void cancelSecondSubscriberWhenFirstSuspended(AccountManager accountManager ){
		System.out.println("Cancel Second Subscriber When First is Suspended*** Start");
		try {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("4037109656");
		Date activityDate = java.sql.Date.valueOf("2011-12-28");
		subscriber.cancel(activityDate,"MCNR",'O',"","");
		} 
		catch (Exception e) {
			System.out.println("Exception [" + e.getClass().getName() + "] caught: " + e.getMessage());
			e.printStackTrace();
		}		
		System.out.println("Cancel Second Subscriber When First is Suspended*** End");
	}
	

}
