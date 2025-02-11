package com.telus.cmb.common;

import java.util.Properties;

import javax.naming.Context;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

import com.telus.cmb.framework.config.ConfigurationManagerFactory;

public abstract class EjbServiceTest {

	protected void setupPT148() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	protected void setupD3() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	}
	
	protected void setupS() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
	}
	
	protected void setupP() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-p.tmi.telus.com:389/cn=prod_81,o=telusconfiguration");
	}
	
	protected String getProviderUrl(Class<?> businessInterface)  {

		String url = System.getProperty("cmb.services." +  businessInterface.getSimpleName() + ".url");
		
		if (url == null) {
			url = ConfigurationManagerFactory.getInstance().getStringValue(businessInterface.getSimpleName());
		}else {
			System.out.println("Provider URL overriden by system property: cmb.services." +  businessInterface.getSimpleName() + ".url");
		}

		System.out.println("Provider URL for [" + businessInterface.getName() + "] service is [" + url + "]");
		
		return url;
	}
	
	protected void overrideEjbUrl(Class<?> businessInterface, String url) {
		if (url != null) {
			System.setProperty("cmb.services." +  businessInterface.getSimpleName() + ".url", url);
		}
	}
	
	protected <T> T createRemoteServiceProxy(Class<T> businessInterface, String jndiName) {
		
		Properties jndiEnvironment = new Properties();
		
		jndiEnvironment.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		jndiEnvironment.setProperty(Context.PROVIDER_URL, getProviderUrl(businessInterface));
		
		SimpleRemoteSlsbInvokerInterceptor invoker = new SimpleRemoteStatelessSessionProxyFactoryBean();
		
		invoker.setJndiName(jndiName);
		invoker.setJndiEnvironment(jndiEnvironment);
		invoker.setCacheHome(true);
		invoker.setRefreshHomeOnConnectFailure(true);
		invoker.setLookupHomeOnStartup(false);
		invoker.setResourceRef(true);

		return ProxyFactory.getProxy(businessInterface, invoker);
	}
}
