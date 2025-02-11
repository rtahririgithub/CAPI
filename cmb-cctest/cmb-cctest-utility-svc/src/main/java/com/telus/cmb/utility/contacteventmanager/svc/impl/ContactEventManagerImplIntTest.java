package com.telus.cmb.utility.contacteventmanager.svc.impl;

import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Test;


public class ContactEventManagerImplIntTest {
	
	ContactEventManagerRemote managerImpl = null;

	public static ContactEventManagerRemote getContactEventManager() throws Exception {
		ContactEventManagerRemote managerImpl = null;
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, "t3://sn25260:30152");

		javax.naming.Context context = new javax.naming.InitialContext(env);

		ContactEventManagerHome contactEventManagerHome 
		= (ContactEventManagerHome) context.lookup("ContactEvent#com.telus.cmb.utility.contacteventmanager.svc.impl.ContactEventManagerHome");
		managerImpl = (ContactEventManagerRemote)contactEventManagerHome.create();
		
		return managerImpl;
	}
	
	@Test
	public void logSubscriberAuthentication() throws Throwable {
		
		String min = "2502622324";
		boolean isAuthenticationSucceeded = false;
		String channelOrganizationID = "9999";
		String outletID = "1234";
		String salesRepID = "0000";
		String applicationID = "SMARTDESKTOP";
		String userID = "18654";
		
		managerImpl = getContactEventManager();
		managerImpl.logSubscriberAuthentication(min, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
		System.out.println("done");
	}
				
	
}	
	
