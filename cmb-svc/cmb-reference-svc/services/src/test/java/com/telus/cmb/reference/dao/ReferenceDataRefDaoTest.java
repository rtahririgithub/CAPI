package com.telus.cmb.reference.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.reference.RatedFeature;
import com.telus.eas.utility.info.PricePlanInfo;

@ContextConfiguration(locations="classpath:com/telus/cmb/reference/dao/test-context-dao.xml")
public class ReferenceDataRefDaoTest extends AbstractTestNGSpringContextTests {

	private static final String LDAP_URL = "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration";
	//private static final String LDAP_URL = "ldaps://ldapread-pt168.tmi.telus.com:636/cn=pt168_81,o=telusconfiguration";
	private static final String SECURITY_PRINCIPAL = "uid=telusAdmin,ou=administrators,o=telusconfiguration";
	private static final String SECURITY_CREDENTIAL = "ptEM148";
	//private static final String SECURITY_CREDENTIAL = "ptEM168";
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
	private ReferenceDataRefDao dao;
	

    @Test(invocationCount=1, threadPoolSize=1)
    public void retrieve_price_plan_by_code() throws Exception {
    	String pricePlanCode = "PADSH50B";
		PricePlanInfo result = dao.retrievePricePlan(pricePlanCode);
		
		System.out.println(result);
		for (RatedFeature feature : result.getFeatures()) {
			System.out.println(feature);
		}
		
    }
	
}
