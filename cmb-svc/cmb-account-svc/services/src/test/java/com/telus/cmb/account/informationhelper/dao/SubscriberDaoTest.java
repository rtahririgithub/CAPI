package com.telus.cmb.account.informationhelper.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.account.informationhelper.dao.SubscriberDao;

@ContextConfiguration(locations="classpath:com/telus/cmb/test/account/dao/test-context-dao.xml")
public class SubscriberDaoTest extends AbstractTestNGSpringContextTests {

	private static final String LDAP_URL = "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration";
	private static final String SECURITY_PRINCIPAL = "uid=telusAdmin,ou=administrators,o=telusconfiguration";
	private static final String SECURITY_CREDENTIAL = "ptEM148";
	
	static {
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", LDAP_URL);
		System.setProperty("com.telusmobility.config.java.naming.security.authentication", "simple");		
		System.setProperty("com.telusmobility.config.java.naming.security.principal", SECURITY_PRINCIPAL);
		System.setProperty("com.telusmobility.config.java.naming.security.credentials", SECURITY_CREDENTIAL);
		System.setProperty("useLdapUrl", "true");	
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");		
	}
	
	@Autowired
	private SubscriberDao subscriberDao;

    @Test(invocationCount=1, threadPoolSize=1)
    //@Test(invocationCount=100, threadPoolSize=5)
	public void load_test_for_retrieveShareablePricePlanSubscriberCount() throws Exception {
		int ban = 70521937;
		subscriberDao.retrieveShareablePricePlanSubscriberCount(ban);
	}

    @Test(invocationCount=1, threadPoolSize=1)
    //@Test(invocationCount=100, threadPoolSize=5)
	public void load_test_for_retrieveServiceSubscriberCounts() throws Exception {
		int ban = 70521937;
        String[] shareableSecondarySocs = {"SFAMILY15"};
		subscriberDao.retrieveServiceSubscriberCounts(ban, shareableSecondarySocs, false);
	}
}
