package com.telus.api.account;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.TestCase;

import com.telus.api.ClientAPI;
import com.telus.api.util.RemoteBeanProxy;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.SubscriberManagerBean;
import com.telus.provider.util.SubscriberManagerBeanCmbImpl;

public class TestProviderDefect extends TestCase {
	private ClientAPI api;

	static {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}
	
	private ClientAPI getClientAPI() throws Throwable {
		if (api == null) {
			api = ClientAPI.getInstance("18654", "apollo", "SMARTD");
		}
		return api;
	}
	
	private void clearClientAPIInstance () {
		api = null;
	}

	private TMProvider getProvider() throws Throwable {
		return (TMProvider) getClientAPI().getProvider();
	}


	private void logMessage(String method, String message) {
		System.out.println("[" + method + "] " + message + "\r\n");
	}

	private void logMessage(String method, Throwable t) {
		System.out.println("[" + method + "] " + getStackTraceAsString(t) + "\r\n");
	}

	private String getStackTraceAsString(Throwable t) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		t.printStackTrace(ps);
		return os.toString();
	}

	private SubscriberLifecycleManager getSubscriberLifecycleManager() throws Throwable {
		return getProvider().getSubscriberLifecycleManager();
	}

	private SubscriberManagerBean getSubscriberManagerBean() throws Throwable {
		return getProvider().getSubscriberManagerBean();
	}
	
	private void printClasspath(String classFile) {
		URL url = getClass().getClassLoader().getResource(classFile);
		logMessage ("printClasspath", classFile + " loaded from [" + url + "]");
	}

	public void testSubscriberLifecycleManagerClasspath() throws Throwable {
		String classFile = "com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager".replace('.', '/') + ".class";
		printClasspath(classFile);
	}
	
	public void testSubscriberLifecycleManagerRemoteClasspath() {
		String classFile = "com.telus.cmb.subscriber.lifecyclemanager.svc.impl.SubscriberLifecycleManagerRemote".replace('.', '/') + ".class";
		printClasspath(classFile);
	}
	
	public void testSubscriberLifecycleManagerHomeClasspath() {
		String classFile = "com.telus.cmb.subscriber.lifecyclemanager.svc.impl.SubscriberLifecycleManagerHome".replace('.', '/') + ".class";
		printClasspath(classFile);
	}
	

	public void testSubscriberManagerBeanClasspath() throws Throwable {
		String classFile = "com.telus.provider.util.SubscriberManagerBean".replace('.', '/') + ".class";
		printClasspath(classFile);
		SubscriberManagerBean subMgrBean = getSubscriberManagerBean();
		String result = ClassLoaderUtils.showClassLoaderHierarchy(subMgrBean, "EJB");
		logMessage("test", result);
		Field f = ((SubscriberManagerBeanCmbImpl) subMgrBean).getClass().getDeclaredField("subscriberLifecycleManager");
		f.setAccessible(true);
		
	}
	
	public void testAllClasspaths() throws Throwable {
		testSubscriberLifecycleManagerClasspath();
		testSubscriberLifecycleManagerHomeClasspath();
		testSubscriberLifecycleManagerRemoteClasspath();
		testSubscriberManagerBeanClasspath();
	}
	
	
	public void testSubscriberManagerBeanClassLoader() throws Throwable {
		String method = "testSubscriberManagerBeanClassLoader";
		
		SubscriberManagerBean subMgrBean = getSubscriberManagerBean();
		String result = ClassLoaderUtils.showClassLoaderHierarchy(subMgrBean, "EJB");
		Field f = ((SubscriberManagerBeanCmbImpl) subMgrBean).getClass().getDeclaredField("subscriberLifecycleManager");
		f.setAccessible(true);
		logMessage(method, result);
		
		SubscriberLifecycleManager slcm = (SubscriberLifecycleManager) f.get(subMgrBean);
	
		printSubscriberLifecycleManagerProxyClassLoader (slcm);
	}
	
	private void printSubscriberLifecycleManagerProxyClassLoader(SubscriberLifecycleManager slcm) throws Throwable {
		String method = "printSubscriberLifecycleManagerProxyClassLoader";
		
		logMessage(method, "SubscriberLifecycleManager: " + ClassLoaderUtils.showClassLoaderHierarchy(SubscriberLifecycleManager.class.getClassLoader()));
		logMessage(method, ClassLoaderUtils.showClassLoaderHierarchy(slcm, "Proxy"));
		
		printRemoteBeanProxyClassLoader(slcm);
	}
	
	private void printRemoteBeanProxyClassLoader(SubscriberLifecycleManager slcm) throws Throwable {
		String method = "printRemoteBeanProxyClassLoader";
		
		RemoteBeanProxy remoteBeanProxy = (RemoteBeanProxy) Proxy.getInvocationHandler(slcm);
		Field f = remoteBeanProxy.getClass().getDeclaredField("beanInstance");
		f.setAccessible(true);
		Object obj = f.get(remoteBeanProxy);
		
		logMessage(method, ClassLoaderUtils.showClassLoaderHierarchy(remoteBeanProxy, "remoteBeanProxy"));
		
		if (obj != null) {
			logMessage(method, ClassLoaderUtils.showClassLoaderHierarchy(obj, "beanInstance"));
		}else {
			logMessage(method, "beanInstance is null.");
		}
	}
	
	public void testAllClassLoaders() throws Throwable {
		testSubscriberManagerBeanClassLoader();
	}

	/**
	 * This is to test the method call on SubscriberLifecycleManager EJB
	 * directly
	 * 
	 * @throws Throwable
	 */
	public void testSubscriberLifecycleManagerOpenSession() throws Throwable {
		String method = "SLCMopenSession";
		try {
			String sessionId = getSubscriberLifecycleManager().openSession("18654", "apollo", "APOLLO");
			logMessage(method, "SessionId=" + sessionId);
		} catch (Throwable t) {
			logMessage(method, t);
		}
	}

	/**
	 * This is a test on the SubscriberManagerBean on a different method
	 * 
	 * @throws Throwable
	 */
	public void testSubscriberManagerBean() throws Throwable {
		String method = "testSubscriberManagerBean";
		try {
			DiscountInfo[] discountList = getSubscriberManagerBean().retrieveDiscounts(1234, "4161234567", "C");
		} catch (Throwable t) {
			// error is expected due to invalid subscribers (or use a test
			// account if you want). we just want to see if the method can be
			// invoked successfully.
			logMessage(method, t);
		}
	}
	
	private String getProviderUrl (RemoteBeanProxy remoteBeanProxy) throws Throwable  {
		Field f = remoteBeanProxy.getClass().getDeclaredField("providerURL");
		f.setAccessible(true);
		String providerUrl  = (String) f.get(remoteBeanProxy);
		logMessage ("getProviderUrl", providerUrl);
		return providerUrl;
	}
	
	private String getPrincipal (RemoteBeanProxy remoteBeanProxy) throws Throwable  {
		Field f = remoteBeanProxy.getClass().getDeclaredField("principal");
		f.setAccessible(true);
		
		String principal  = (String) f.get(remoteBeanProxy);
		logMessage ("getPrincipal", principal);
		return principal;
	}
	
	private String getCredential (RemoteBeanProxy remoteBeanProxy) throws Throwable  {
		Field f = remoteBeanProxy.getClass().getDeclaredField("credentials");
		f.setAccessible(true);
		String credential  = (String) f.get(remoteBeanProxy);
		logMessage ("getCredential", credential);
		return credential;
	}
	
	
	public void testSubscriberLifecyleManagerDirectEJB3() throws Throwable {
		String method = "testSubscriberLifecyleManagerDirectEJB3";
		
		Hashtable environment = new Hashtable();
		Context context = null;
		SubscriberLifecycleManager slcm = getSubscriberLifecycleManager();
		RemoteBeanProxy remoteBeanProxy = (RemoteBeanProxy) Proxy.getInvocationHandler(slcm);
		
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		environment.put(Context.PROVIDER_URL, getProviderUrl(remoteBeanProxy));
		environment.put(Context.SECURITY_PRINCIPAL, getPrincipal(remoteBeanProxy));
		environment.put(Context.SECURITY_CREDENTIALS, getCredential(remoteBeanProxy));
		
		try {
			context = new InitialContext(environment);
			SubscriberLifecycleManager ejb = (SubscriberLifecycleManager) context.lookup("SubscriberLifecycleManager#com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager");
			logMessage (method, ClassLoaderUtils.showClassLoaderHierarchy(ejb, "SubscriberLifecycleManager EJB3"));
			String sessionId = ejb.openSession("18654", "apollo", "APOLLO");
			logMessage (method, "sessionId="+sessionId);
			logMessage (method, ClassLoaderUtils.showClassLoaderHierarchy(ejb, "SubscriberLifecycleManager EJB3"));
		}catch (Throwable t) {
			logMessage (method, t);
		}finally {
			if (context != null) {
				context.close();
			}
		}
	}
	
	
	public void testEJBLookup() throws Throwable {
		testSubscriberLifecyleManagerDirectEJB3();
	}
	
	private void printEnvInfo() throws Throwable {
		logMessage ("printEnvInfo", "printing classpath and classloader info...");
		testAllClasspaths();
		testAllClassLoaders();
	}
	
	public void testRun() throws Throwable {
		testBundle(); 
		
		clearClientAPIInstance();
		
		testBundle(); //retest with new instance		
	}
	
	private void testBundle() throws Throwable {
		printEnvInfo();
		
		testSubscriberManagerBean();
		testSubscriberLifecycleManagerOpenSession();
		testEJBLookup();
		
		printEnvInfo();
	}
}
