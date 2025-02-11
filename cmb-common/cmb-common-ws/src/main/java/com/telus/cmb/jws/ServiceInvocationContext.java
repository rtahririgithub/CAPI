/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.util.JNDINames;
import com.telus.api.util.RemoteBeanProxy;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.jws.util.AppConfiguration;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;

/**
 * @author Pavel Simonovsky
 * 
 */
public class ServiceInvocationContext {

	private static final Log logger = LogFactory.getLog(ServiceInvocationContext.class);

	private BaseService baseService;

	private List<Object> statefullProxies = new ArrayList<Object>();

	// This cache is per single Web service business operation.
	private Map<Object, String> sessionIdCache = new HashMap<Object, String>();

	// PROD00189794 fix: temporary work-around, to be removed in July release
	private Map<String, Object> cmbProxies = new HashMap<String, Object>();
	
	//the identity for this service invocation 
	private ClientIdentity clientIdentity;
	
	private Exception schemaValidationException = null;


	public ServiceInvocationContext(BaseService baseService) {
		this.baseService = baseService;
	}

	public void dispose() {
		logger.debug("Disposing allocated statefull resources...");
		for (Object proxy : statefullProxies) {
			Object handler = Proxy.getInvocationHandler(proxy);
			if (handler != null && handler instanceof RemoteBeanProxy) {
				logger.debug("Disposing SFSB [" + proxy + "]");
				((RemoteBeanProxy) handler).dispose();
			}
		}
		statefullProxies.clear();

		// PROD00189794 fix, temporary work-around, to be removed in July release
		java.util.Collection<Object> c = cmbProxies.values();
		for (Object proxy : c) {
			Object handler = Proxy.getInvocationHandler(proxy);
			if (handler != null && handler instanceof RemoteBeanProxy) {
				logger.debug("Disposing CMB manager/facade [" + proxy + "]");
				((RemoteBeanProxy) handler).dispose();
			}
		}
		cmbProxies.clear();
	}

/*	@SuppressWarnings("unchecked")
	public <T> T getStatefullProxy(Class<T> businessInterface, String jndiName) throws Exception {

		ClientIdentity identity = getClientIdentity();

		Class<String>[] constructorTypes = new Class[] { String.class, String.class, String.class };
		Object[] constructorArgs = new String[] { identity.getPrincipal(), identity.getCredential(), identity.getApplication() };

		Object proxy = RemoteBeanProxyFactory.createProxy(businessInterface, jndiName, baseService.getProxyProviderUrl(businessInterface), "", "", constructorTypes, constructorArgs);

		statefullProxies.add(proxy);

		logger.debug("Created new SFSB [" + jndiName + "]");

		return (T) proxy;
	} */

	/**
	 * PROD00189794 fix
	 * 
	 * Background: All CMB EJB are stateless EJB. We currently use Spring frameworks' SimpleRemoteSlsbInvokerInterceptor as stateless proxy. However,
	 * that proxy re-creates remote instance for each invocation. Although CMB "manager" / "facade" EJB are stateless, but they do require client to
	 * interact with same remote instance, on which they acquired sessionID via openSession() and any method that requires sessinID.
	 * 
	 * Temporary work-around for 2011 April release: treat the CMB manager / facade EJB as "stateful" EJB as any other ECA manager EJB, switch to
	 * RemoteBeanProxy, maintain the remote reference for single ServiceInvocationContext, dispose the proxy at the end of this InvocationContext
	 * 
	 * Long term solution would be removing the need of sticking to same remote instance for openSession() and other methods for "manager" / "facacde"
	 * EJB. To be implemented in 2011 July release.
	 * 
	 * @param <T>
	 * @param businessInterface
	 * @param jndiName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <T> T getCMBManagerFacadeProxy(Class<T> businessInterface, String jndiName, String providerUrl) throws Exception {

		// get it from cache
		Object proxy = cmbProxies.get(jndiName);

		if (proxy == null) { // if no, create one.

			proxy = RemoteBeanProxyFactory.createProxy(businessInterface, jndiName, providerUrl, JNDINames.SECURITY_PRINCIPAL_NAME, JNDINames.SECURITY_CREDENTIALS);
			cmbProxies.put(jndiName, proxy);
			logger.debug("Created new ManagerFacade [" + jndiName + "]");
		}

		return (T) proxy;
	}

	public <T> T getStatelessProxy(Class<T> businessInterface, String jndiName, String providerUrl) throws Exception {
		return baseService.getStatelessProxy(businessInterface, jndiName, providerUrl);
	}

	public AccountLifecycleFacade getAccountLifecycleFacade() throws Exception {
		// return getStatelessProxy(AccountLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE);

		// PROD00189794 work-around, 2011 April release, see getCMBManagerFacadeProxy() for detail
		return getCMBManagerFacadeProxy(AccountLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE, AppConfiguration.getAccountLifecycleFacadeUrl());
	}

	public AccountLifecycleManager getAccountLifecycleManager() throws Exception {
		// return getStatelessProxy(AccountLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);

		// PROD00189794 work-around, 2011 April release, see getCMBManagerFacadeProxy() for detail
		return getCMBManagerFacadeProxy(AccountLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER, AppConfiguration.getAccountLifecycleManagerUrl());
	}

	public SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws Exception {
		return getCMBManagerFacadeProxy(SubscriberLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE, AppConfiguration.getSubscriberLifecycleFacadeUrl());
	}

	public SubscriberLifecycleManager getSubscriberLifecycleManager() throws Exception {
		// return getStatelessProxy(SubscriberLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER);
		return getCMBManagerFacadeProxy(SubscriberLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER, AppConfiguration.getSubscriberLifecycleManagerUrl());
	}

	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws Exception {
		// return context.getStatelessProxy(SubscriberLifecycleHelper.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
		return getCMBManagerFacadeProxy(SubscriberLifecycleHelper.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER, AppConfiguration.getSubscriberLifecycleHelperUrl());
	}
	public ReferenceDataHelper getReferenceDataHelper() throws Exception {
		return getCMBManagerFacadeProxy(ReferenceDataHelper.class, JNDINames.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER, AppConfiguration.getReferenceDataHelperUrl());
	}
	
	public ReferenceDataFacade getReferenceDataFacade() throws Exception {
		return getCMBManagerFacadeProxy(ReferenceDataFacade.class, JNDINames.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE, AppConfiguration.getReferenceDataFacadeUrl());
	}

	public String getAccountLifeCycleFacadeSessionId() throws Throwable {
		return getSessionId(getAccountLifecycleFacade());
	}

	public String getAccountLifeCycleManagerSessionId() throws Throwable {
		return getSessionId(getAccountLifecycleManager());
	}

	public String getSubscriberLifecycleFacadeSessionId() throws Throwable {
		return getSessionId(getSubscriberLifecycleFacade());
	}

	public String getSubscriberLifecycleManagerSessionId() throws Throwable {
		return getSessionId(getSubscriberLifecycleManager());
	}

	public String getSessionId(Object proxy) throws Throwable {
		String sessionId = sessionIdCache.get(proxy);
		if (sessionId == null) {
			try {
				ClientIdentity identity = getClientIdentity();
				Method method = proxy.getClass().getMethod("openSession", String.class, String.class, String.class);
				sessionId = (String) method.invoke(proxy, identity.getPrincipal(), identity.getCredential(), identity.getApplication());
				sessionIdCache.put(proxy, sessionId);
			} catch (InvocationTargetException invocationTargetException) {
				if (invocationTargetException.getCause() != null){
					throw invocationTargetException.getCause();
				}
				
				throw invocationTargetException;
			}
		}
		return sessionId;
	}

	//No need to resolve ClientIdentity multiple times for one ServiceInvocation, so cache it.
	private ClientIdentity getClientIdentity() throws Exception {
		if ( clientIdentity==null ) {
			clientIdentity = baseService.getClientIdentity();
		}
		return clientIdentity;
	}

	public String getClientApplication() {
		try {
			ClientIdentity clientIdentity = getClientIdentity();
			if (clientIdentity != null) {
				return getClientIdentity().getApplication();
			}
		}catch (Throwable t) {
			logger.debug(t);
		}
		
		try {
			return "Unknown application in " + ApplicationServiceLocator.getInstance().getApplicationName();
		}catch (Throwable t) {
			logger.debug(t);
		}
		
		return "Unknown application in WS";
	}

	public Exception getSchemaValidationException() {
		return schemaValidationException;
	}

	public void setSchemaValidationException(Exception schemaValidationException) {
		this.schemaValidationException = schemaValidationException;
	}
	
}
