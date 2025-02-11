package com.telus.cmb.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

import com.telus.cmb.framework.config.ConfigurationManagerFactory;

public class EJBUtil {
	

	private static final Logger logger = Logger.getLogger(EJBUtil.class);
	private static Map<String, String> urlMap = Collections.synchronizedMap(new HashMap<String, String>());
	private static String LDAP_ENTRY_SEPARATOR=".";
	
	public static final String TELUS_CMBSERVICE_REFERENCE_DATA_FACADE = "ReferenceDataFacade#com.telus.cmb.reference.svc.ReferenceDataFacade";
	public static final String TELUS_CMBSERVICE_REFERENCE_DATA_HELPER = "ReferenceDataHelper#com.telus.cmb.reference.svc.ReferenceDataHelper";
	public static final String TELUS_CMBSERVICE_APPLICATION_MESSAGE_FACADE = "ApplicationMessageFacade#com.telus.cmb.reference.svc.ApplicationMessageFacade";
	public static final String TELUS_CMBSERVICE_SERVICE_ORDER_REFERENCE_FACADE = "ServiceOrderReferenceFacade#com.telus.cmb.reference.svc.ServiceOrderReferenceFacade";

	public static final String TELUS_CMBSERVICE_MONITORING_FACADE = "MonitoringFacade#com.telus.cmb.monitoring.svc.impl.MonitoringFacadeHome";

	public static final String TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE = "SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade";
	public static final String TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER = "SubscriberLifecycleHelper#com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper";
	public static final String TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER = "SubscriberLifecycleManager#com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager";    

	public static final String TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER  = "AccountInformationHelper#com.telus.cmb.account.informationhelper.svc.AccountInformationHelper";
	public static final String TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE = "AccountLifecycleFacade#com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade";
	public static final String TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER = "AccountLifecycleManager#com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager";

	public static final String TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER = "ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper";
	public static final String TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_MANAGER = "ProductEquipmentManager#com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager";
	public static final String TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_LIFECYCLE_FACADE = "ProductEquipmentLifecycleFacade#com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade";

	public static final String TELUS_CMBSERVICE_CONTACT_EVENT_MANAGER = "ContactEventManager#com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager";
	public static final String TELUS_CMBSERVICE_QUEUE_EVENT_MANAGER = "QueueEventManager#com.telus.cmb.utility.queueevent.svc.QueueEventManager";
	public static final String TELUS_CMBSERVICE_CONFIGURATION_MANAGER = "ConfigurationManager#com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager";
	public static final String TELUS_CMBSERVICE_ACTIVITY_LOGGING = "ActivityLoggingService#com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService";
	public static final String TELUS_CMBSERVICE_DEALER_MANAGER = "DealerManager#com.telus.cmb.utility.dealermanager.svc.DealerManager";
	public static final String TELUS_CMBSERVICE_BILLING_INQUIRY_REFERENCE_FACADE = "BillingInquiryReferenceFacade#com.telus.cmb.reference.svc.BillingInquiryReferenceFacade";
	public static final String TELUS_CMBSERVICE_CUSTOMER_INFORMATION_REFERENCE_FACADE = "CustomerInformationReferenceFacade#com.telus.cmb.reference.svc.CustomerInformationReferenceFacade";
	public static final String TELUS_CMBSERVICE_CUSTOMER_ORDER_REFERENCE_FACADE = "CustomerOrderReferenceFacade#com.telus.cmb.reference.svc.CustomerOrderReferenceFacade";
	public static final String TELUS_CMBSERVICE_ENTERPRISE_REFERENCE_FACADE = "EnterpriseReferenceFacade#com.telus.cmb.reference.svc.EnterpriseReferenceFacade";

	public static final String SECURITY_PRINCIPAL = "ejb_user";
	public static final String SECURITY_CREDENTIALS = "ejb_user";

	private static final String LOCALHOST = "localhost";
	private static HashMap<String, String[]> EJB_GROUP = new HashMap<String, String[] > ();
	private static HashMap<String, String> EJB_INTERFACE_LDAPKEY_MAP = new HashMap<String, String> ();
	
	static {
		initEJBGroup();
		initEjbLdapKeyMap();
	}
	
	private static void initEJBGroup() {
		registerEJBGroup( new String[] {"AccountInformationHelper", "AccountLifecycleManager", "AccountLifecycleFacade"} );
		registerEJBGroup( new String[] {"SubscriberLifecycleHelper", "SubscriberLifecycleManager", "SubscriberLifecycleFacade"} );
		registerEJBGroup( new String[] {"ProductEquipmentHelper", "ProductEquipmentManager", "ProductEquipmentLifecycleFacade"} );
	}
	
	private static void initEjbLdapKeyMap() {
		EJB_INTERFACE_LDAPKEY_MAP.put("AccountInformationHelper", "AccountInformationHelperUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("AccountLifecycleFacade", "AccountLifecycleFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("AccountLifecycleManager", "AccountLifecycleManagerUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ActivityLoggingService", "ActivityLoggingServiceUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ApplicationMessageFacade", "ApplicationMessageFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("BillingInquiryReferenceFacade", "BillingInquiryReferenceFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ConfigurationManager", "ConfigurationManagerEjbUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ContactEventManager", "ContactEventManagerEjbUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("DealerManager", "DealerManagerEjbUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ProductEquipmentHelper", "ProductEquipmentHelperUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ProductEquipmentLifecycleFacade", "ProductEquipmentLifecycleFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ProductEquipmentManager", "ProductEquipmentManagerUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("QueueEventManager", "QueueEventManagerEjbUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ReferenceDataFacade", "ReferenceDataFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("ReferenceDataHelper", "ReferenceDataHelperUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("SubscriberLifecycleFacade", "SubscriberLifecycleFacadeUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("SubscriberLifecycleHelper", "SubscriberLifecycleHelperUrl");
		EJB_INTERFACE_LDAPKEY_MAP.put("SubscriberLifecycleManager", "SubscriberLifecycleManagerUrl");
	}

	
	private static void registerEJBGroup(String[] ejbNames) {
		for( int i=0; i<ejbNames.length; i++ ) {
			EJB_GROUP.put(ejbNames[i], ejbNames);
		}
	}


	public static <T> T getHelperProxy(Class<T> businessInterface, String jndiName) {
		return getStatelessProxy(businessInterface, jndiName);
	}
	
	private static String resolveCMBUrlThroughSystemProperty( String interfaceName, String defaultValue ) {
		
		String[] ldapPath = new String[] { "cmb", "services", interfaceName , "url" };
		return System.getProperty( StringUtil.arrayToString( ldapPath, LDAP_ENTRY_SEPARATOR), defaultValue );
	}

	private static String getProxyProviderUrl(Class<?> businessInterface, String jndiName)  {	
		synchronized (urlMap) {
			String className = businessInterface.getSimpleName();
			String url = urlMap.get(className);

			if (url == null) {
				//System property override mechanism always take highest precedence. 
				url = resolveCMBUrlThroughSystemProperty( className, null );
				if (url == null) {
					//no system property specify the url
					if ( EJB_GROUP.containsKey( className )) {
						if ( doLocalJndiLookup( jndiName ) ) {
							//local EJB container has this EJB
							url = LOCALHOST;
							//now try to update its coexist EJB ( in same EAR file ) URL as well.
							//find the coexist EJB interface names
							String[] groupdEJBs = EJB_GROUP.get( className );
							if (groupdEJBs!=null ) {
								// each one, update the URL to localhost if no system property override the the URL.
								for( int i=0; i<groupdEJBs.length; i++ ) {
									String urlStr = resolveCMBUrlThroughSystemProperty( groupdEJBs[i], url );
									urlMap.put( groupdEJBs[i], urlStr );
								}
							} else {
								urlMap.put(className, url);
							}
						}
					}
					
					//at last, resolve the url from configuration LDAP
					if ( url==null) {
						String key = EJB_INTERFACE_LDAPKEY_MAP.get(className);
						if (key != null) {
							url = ConfigurationManagerFactory.getInstance().getStringValue(key);
						}
					}
				}
				
				if (url != null) {
					urlMap.put(className, url);
				}

			}
			logger.debug("Provider URL for [" + businessInterface.getName() + "] EJB is [" + url + "]");

			return url;
		}
	}
	
	//return true if local JNDI tree contain this name
	private static boolean doLocalJndiLookup(String jndiName) {
		Hashtable<String, String> jndiEnvironment = new Hashtable<String, String>();
		
		jndiEnvironment.put(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
		jndiEnvironment.put(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
		
		Context initialContext = null;
		try {
			initialContext = new InitialContext(jndiEnvironment );
			initialContext.lookup(jndiName);
			return true;
		} catch( Exception e ) { 
			return false;
		}
		finally {
			if ( initialContext!=null) try {initialContext.close(); } catch( Exception e ) {}
		}
	}

	public static <T> T getStatelessProxy(Class<T> businessInterface, String jndiName) {
		String proxyProviderUrl = getProxyProviderUrl(businessInterface, jndiName );
		if ( LOCALHOST.equals(proxyProviderUrl )) {
			return getLocalStatelessProxy( businessInterface, jndiName );
		}
		return getStatelessProxy(businessInterface, jndiName, proxyProviderUrl);
	}
	
	public static <T> T getStatelessProxy(Class<T> businessInterface, String jndiName, String proxyProviderUrl) {
		
		Properties jndiEnvironment = new Properties();
			
		jndiEnvironment.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		jndiEnvironment.setProperty(Context.PROVIDER_URL, proxyProviderUrl);
		
		jndiEnvironment.setProperty(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
		jndiEnvironment.setProperty(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
		SimpleRemoteSlsbInvokerInterceptor invoker = new SimpleRemoteStatelessSessionProxyFactoryBean();
			
		invoker.setJndiName(jndiName);
		invoker.setJndiEnvironment(jndiEnvironment);
		invoker.setCacheHome(true);
		invoker.setRefreshHomeOnConnectFailure(true);
		invoker.setLookupHomeOnStartup(false);
		invoker.setResourceRef(true);

		//defect PROD00192836 fix, also need to expose the auhorization context for EJB invocation to avoid the Security issue
		//when access cmb EJB in AmdocsTransactionCallback ( within Amdocs context )
		invoker.setExposeAccessContext(true);

		T proxy = ProxyFactory.getProxy(businessInterface, invoker);
			
		logger.debug("Created new SLSB [" + jndiName + "] with proxyProviderUrl="+proxyProviderUrl);
		return proxy;
	}
	
	
	//Defect PROD00192836 fix, May 26,2011 M.Liao 
	//Symptom: Accessing CMB EJB within AmdocsTransactionCallback would cause the following error even on JNDI lookup.
	//  java.lang.SecurityException:[Security:090398]Invalid Subject: principals=[name = EXT<TksmauY}UCebwrDuLsTXZ}L45h7ENcKDAjpSfg0;appId=MERCRY;>]
	//Root cause: This is because AmdocsTransactionCallback is running under AMDOCS security context
	//
	//Interim solution:
	//  Do the local JNDI lookup with explicit user/password 
	//
	//Long term goal is DAO class shall not access other EJB, and having EJB to orchestrate the call to other EJB then pass information down to DAO.
	/**
	 * Get EJB proxy from local JNDI container.
	 * @param <T>
	 * @param businessInterface
	 * @param jndiName
	 * @return
	 */
	public static <T> T getLocalStatelessProxy(Class<T> businessInterface, String jndiName) {
		
		Properties jndiEnvironment = new Properties();
			
		jndiEnvironment.setProperty(Context.SECURITY_PRINCIPAL, SECURITY_PRINCIPAL);
		jndiEnvironment.setProperty(Context.SECURITY_CREDENTIALS, SECURITY_CREDENTIALS);
		
		logger.debug("jndiEnv for local SLSB " + jndiEnvironment );
			
		SimpleRemoteSlsbInvokerInterceptor invoker = new SimpleRemoteStatelessSessionProxyFactoryBean();
			
		invoker.setJndiName(jndiName);
		invoker.setJndiEnvironment(jndiEnvironment);
		invoker.setCacheHome(true);
		invoker.setRefreshHomeOnConnectFailure(true);
		invoker.setLookupHomeOnStartup(false);
		invoker.setResourceRef(true);
		invoker.setExposeAccessContext(true);

		T proxy = ProxyFactory.getProxy(businessInterface, invoker);
			
		logger.debug("Created new local SLSB [" + jndiName + "]");
		return proxy;
	}
}
