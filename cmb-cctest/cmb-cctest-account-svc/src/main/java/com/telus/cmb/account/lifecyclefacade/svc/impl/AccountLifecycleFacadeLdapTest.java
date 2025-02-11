package com.telus.cmb.account.lifecyclefacade.svc.impl;

import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.common.ejb.LdapTestPoint;

public class AccountLifecycleFacadeLdapTest {

	LdapTestPoint ldapTestPoint;
//	String url="t3://localhost:7001";
//	String url = "t3://ln98556.corp.ads:30022"; //pt148
	String url = "t3://ln98557.corp.ads:30022"; //pt148

	
	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		ldapTestPoint = (LdapTestPoint) context.lookup("AccountLifecycleFacade#com.telus.cmb.common.ejb.LdapTestPoint");	
		context.close();
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}


	@Test
	public void testOverrideLdapValue() {
		updateLdapValue("marketingToNonPAP", "false");
//		updateLdapValue("retrieveAccountsByPostalCodeRollback", "false");
	}
	
	private void updateLdapValue(String ldapKey, String newValue) {
		displayLdapValue(ldapKey);
		ldapTestPoint.overrideLdapValue(ldapKey, newValue, null, null);
		displayLdapValue(ldapKey);
	}
	
	private void resetLdapValue(String ldapKey) {
		displayLdapValue(ldapKey);
		ldapTestPoint.clearLdapOverride(ldapKey);
		displayLdapValue(ldapKey);
	}
	
	private void displayLdapValue(String ldapKey) {
		System.out.println("["+ldapKey+"]"+ldapTestPoint.getStringValue(ldapKey));
	}
	
	@Test
	public void testLdapValue() {
		displayLdapValue("marketingToNonPAP");
	}

}
