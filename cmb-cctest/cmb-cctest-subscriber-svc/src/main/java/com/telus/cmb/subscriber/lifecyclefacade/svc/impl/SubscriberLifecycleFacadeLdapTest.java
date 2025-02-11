package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.cmb.common.ejb.LdapTestPoint;

public class SubscriberLifecycleFacadeLdapTest {

	LdapTestPoint ldapTestPoint;
//	String url="t3://localhost:7001";
//	String url = "t3://ln98556.corp.ads:30022"; //pt148
//	String url = "t3://ln98557.corp.ads:30022"; //pt148
	String url = "t3://ln98937.corp.ads:42022"; //st101 / cim2
//	String url = "t3://ln98938.corp.ads:42022"; //st101 / cim2
//	String url = "t3://ln98939.corp.ads:42023"; //st101 / cim3
//	String url = "t3://ln98940.corp.ads:42023"; //st101 / cim3
	
//	String url = "t3://ln98937.corp.ads:42024"; //st101 / cimBatch
//	String url = "t3://ln98938.corp.ads:42024"; //st101 / cimBatch

	
	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		ldapTestPoint = (LdapTestPoint) context.lookup("SubscriberLifecycleFacade#com.telus.cmb.common.ejb.LdapTestPoint");	
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
		updateLdapValue ("RetrieveSubscriberIdByPhoneNumberRollback", "true");
		updateLdapValue ("GetSubscriberListByBANrollBack", "true");
		updateLdapValue ("GetSubByBanAndPhoneNumberRollback", "true");
		updateLdapValue ("RetrieveSubscriberListByPhoneNumbersRollback", "true");
		updateLdapValue ("RetrieveSubscriberListByBanAndPhoneNumberRollback", "true");
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
	
	@Test
	public void testResetLdapvalue() {
		resetLdapValue("RetrieveSubscriberIdByPhoneNumberRollback");
		resetLdapValue("GetSubscriberListByBANrollBack");
		resetLdapValue("GetSubByBanAndPhoneNumberRollback");
		resetLdapValue("RetrieveSubscriberListByPhoneNumbersRollback");
		resetLdapValue("RetrieveSubscriberListByBanAndPhoneNumberRollback");
	}
	
	@Test
	public void testDisplayLdapValue() {
		displayLdapValue("RetrieveSubscriberIdByPhoneNumberRollback");
		displayLdapValue("GetSubscriberListByBANrollBack");
		displayLdapValue("GetSubByBanAndPhoneNumberRollback");
		displayLdapValue("RetrieveSubscriberListByPhoneNumbersRollback");
		displayLdapValue("RetrieveSubscriberListByBanAndPhoneNumberRollback");
	}
	
	private void displayLdapValue(String ldapKey) {
		System.out.println("["+ldapKey+"]"+ldapTestPoint.getStringValue(ldapKey));
	}

}
