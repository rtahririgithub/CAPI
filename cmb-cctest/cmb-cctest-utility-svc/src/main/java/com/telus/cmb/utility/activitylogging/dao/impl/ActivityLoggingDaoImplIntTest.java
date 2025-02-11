package com.telus.cmb.utility.activitylogging.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.account.Address;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.servicerequest.TelusServiceRequestException;
import com.telus.cmb.utility.activitylogging.dao.ActivityLoggingDao;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;

public class ActivityLoggingDaoImplIntTest {


	static {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}
	
	static void pt168() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlpt168ecaeasinteca:7621");
	}

	static void staging() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlseaseca:11182");
	}
	
	static void sit() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlj1easeca:8982");
	}

	static void pt148() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	static void dv103() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wld3easeca:8382");
	}
	
	static void local() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://localhost:7001");
	}
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		dv103();
	}

	@After
	public void tearDown() throws Exception {
	}

	/* only language code and application id are mandatory */
	private ServiceRequestHeader newServiceRequestHeader(String languageCode, long applicationId, String referenceNumber, ServiceRequestParent parentRequest, ServiceRequestNote note) throws TelusServiceRequestException {
		ServiceRequestHeaderInfo headerInfo = new ServiceRequestHeaderInfo();
		
		if (languageCode == null || languageCode.equals(""))
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setLanguageCode(languageCode);
		
		if (applicationId <= 0)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setApplicationId(applicationId);
		
		headerInfo.setReferenceNumber(referenceNumber);
		headerInfo.setServiceRequestParent(parentRequest);
		headerInfo.setServiceRequestNote(note);
		
		return headerInfo;
	}
	
	@Test
	public void logChangeAccountAddressActivity() throws Exception {
		ActivityLoggingDao activityLoggingDao = new ActivityLoggingDaoImpl();
		String dealerCode = "";
		String salesRepCode = "";
		String userId = "18654";
		int banId = 8;
		Address address = new AddressInfo();
		address.setProvince("ON");
		address.setCity("TORONTO");
		address.setPostalCode("M1M1M1");
		address.setStreetName("CONSILIUM PL.");
		address.setStreetNumber("300");
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		ChangeAccountAddressActivity activity = new ChangeAccountAddressActivity(header);
		activity.setActors(dealerCode, salesRepCode, userId);

		activity.setBanId(banId);
		activity.setAddress(address);
		
		activityLoggingDao.logChangeAccountAddressActivity(activity);
	}
}
