package com.telus.cmb.subscriber.lifecyclehelper;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclehelper.xml", 
		"classpath:application-context-datasources-lifecyclehelper-testing-d3.xml"})
public abstract class BaseLifecycleHelperIntTest {
	static {
		String ldapUrl = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", ldapUrl);
	}
}