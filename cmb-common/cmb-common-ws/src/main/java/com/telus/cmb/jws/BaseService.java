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

import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.ejb.access.SimpleRemoteSlsbInvokerInterceptor;
import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.telus.api.util.JNDINames;
import com.telus.api.util.RemoteBeanProxy;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.app.ApplicationRuntimeHelper;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityCorporateLdapDao;
import com.telus.cmb.common.identity.ClientIdentityManager;
import com.telus.cmb.common.identity.ClientIdentityManagerLdapImpl;
import com.telus.cmb.common.perfmon.PerformanceMonitor;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.util.XmlUtil;
import com.telus.cmb.jws.identity.ClientIdentityProvider;
import com.telus.cmb.jws.identity.IdentityContext;
import com.telus.cmb.jws.identity.TelusHeaderIdentityProvider;
import com.telus.cmb.jws.util.AppConfiguration;
import com.telus.cmb.reference.svc.BillingInquiryReferenceFacade;
import com.telus.cmb.reference.svc.CustomerInformationReferenceFacade;
import com.telus.cmb.reference.svc.CustomerOrderReferenceFacade;
import com.telus.cmb.reference.svc.EnterpriseReferenceFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.reference.svc.ServiceOrderReferenceFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.framework.config.ConfigContext;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;


/**
 * @author Pavel Simonovsky
 *
 */
public abstract class BaseService {
	
	protected Log logger = LogFactory.getLog(getClass());

	@Resource
	protected WebServiceContext serviceContext;

	protected Map<String, Object> statelessProxies = new HashMap<String, Object>();
	
	protected static ClientIdentityManager clientIdentityManager;
	protected ClientIdentityProvider clientIdentityProvider;
	
	protected ExceptionTranslator exceptionTranslator;
	
	protected Boolean contextFixRequired = null;
	protected static final String SCHEMA_VALIDATION_ERROR_CODE = "SCHMA001"; 

	public BaseService() {
		this(null, null);
	}

	public BaseService(ClientIdentityProvider clientIdentityProvider) {
		this(null, clientIdentityProvider);
	}

	public BaseService(ExceptionTranslator exceptionTranslator) {
		this(exceptionTranslator, null);
	}
	
	public BaseService(ExceptionTranslator exceptionTranslator, ClientIdentityProvider clientIdentityProvider) {
		this.exceptionTranslator = exceptionTranslator == null ? new DefaultExceptionTranslator() : exceptionTranslator;
		this.clientIdentityProvider = clientIdentityProvider == null ? new TelusHeaderIdentityProvider() : clientIdentityProvider;
	}
	
	protected boolean isContextFixRequired() {
		if (contextFixRequired == null) {
			try {
				
				String serverVersion = ApplicationServiceLocator.getInstance().getApplicationRuntimeHelper().getServerVersion();
				// use context fix only for 10.3.0 WLS version
				contextFixRequired = serverVersion != null && serverVersion.startsWith("10.3.0");
			} catch (Exception e) {
				contextFixRequired = true; //TO DO: this is temp workaround. remove this
			}
		}
		return contextFixRequired;
	}
	
	public WebServiceContext getWebServiceContext() {
		return serviceContext;
	}
	
	protected ClientIdentityProvider getClientIdentityProvider() {
		return clientIdentityProvider;
	}
	
	protected ClientIdentityManager getClientIdentityManager() {
		if (clientIdentityManager == null) {
			ClientIdentityCorporateLdapDao dao = null;
			try {
				dao = (ClientIdentityCorporateLdapDao) getBean( "clientIdentityCorpLdapDao" );
			} catch ( Throwable t ) {
				//likely this WS does not need to access corporate LDAP. It's safe to ignore the error.
				logger.warn( "Unable to load ClientIdentityCorporateLdapDao from spring application context, error: " + t.toString() );
			}
			clientIdentityManager = new ClientIdentityManagerLdapImpl( dao );
		}
		return clientIdentityManager;
	}
	
	public ClientIdentity getClientIdentity() throws Exception {
		ClientIdentity userIdentity = (ClientIdentity) serviceContext.getMessageContext().get(ClientIdentityProvider.CLIENT_IDENTITY);
		if (userIdentity==null ) {
			
			userIdentity = getClientIdentityProvider().getClientIdentity(serviceContext, getClientIdentityManager());
			serviceContext.getMessageContext().put(ClientIdentityProvider.CLIENT_IDENTITY, userIdentity);
		} 
		
		return userIdentity;
	}
	
	@PreDestroy
	public void dispose() {
		logger.debug("Disposing allocated stateless resources...");
		for (Map.Entry<String, Object> entry : statelessProxies.entrySet()) {
			
			String jndiName = entry.getKey();
			Object proxy = entry.getValue();
			
			Object handler = Proxy.getInvocationHandler(proxy);
			if (handler != null && handler instanceof RemoteBeanProxy) {
				logger.debug("Disposing SLSB [" + jndiName + "]");
				((RemoteBeanProxy) handler).dispose();
			}
		}
		statelessProxies.clear();
	}

	/**
	 * Returns an exception translator provided by SEI implementation using constructor.
	 *  
	 * @return ExceptionTranslator
	 */
	public ExceptionTranslator getExceptionTranslator(){
		return exceptionTranslator;
	}
	
	protected <T> T execute(ServiceInvocationCallback<T> callback) throws ServiceException, PolicyException {

		/* CAPI DR 2021: since all CAPI web services (including old ones using this class) are moving to WLS 12.2.1.4.0,
		 * this code is no longer required. In fact, setting the context to the parent class loader will obfuscate 
		 * resources and classes that are necessary to execute service calls.
		if (isContextFixRequired()) {
			ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
			ClassLoader parentClassLoader = currentClassLoader.getParent();
			Thread.currentThread().setContextClassLoader(parentClassLoader);
		}
		*/

		ServiceInvocationContext serviceInvocationContext = new ServiceInvocationContext(this);
		
		try {
			// Check if there is a Schema Validation Exception 
			// from the Service Schema Validator
			Exception exception = (Exception) serviceContext.getMessageContext().get(ServiceSchemaValidator.ERROR);
			if(exception!=null) {
				throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Schema validation error ["+exception.getMessage()+"]", exception);
			}
			
			return callback.doInInvocationCallback(serviceInvocationContext);

		} catch (Throwable throwable) {
			// the runtime exception will be intercepted by service invocation advice
			// and passed to exception translator for further processing.
			throw new ServiceNestedException(throwable);
		} finally {
			serviceInvocationContext.dispose();
		}
	}
	
	/**
	 * This is the same as {@link #execute(ServiceInvocationCallback)} except that this method is exposed to BaseServiceV2
	 * @param callback
	 * @return
	 * @throws ServiceException
	 * @throws PolicyException
	 */
	protected <T> T executeDeprecated(ServiceInvocationCallback<T> callback) throws ServiceException, PolicyException {
		return execute(callback);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getStatelessProxy(Class<T> businessInterface, String jndiName, String providerUrl) {

		T proxy = (T) statelessProxies.get(jndiName);
		
		if (proxy == null) {
			Properties jndiEnvironment = new Properties();
			
			jndiEnvironment.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			jndiEnvironment.setProperty(Context.PROVIDER_URL, providerUrl);
			
			SimpleRemoteSlsbInvokerInterceptor invoker = new SimpleRemoteStatelessSessionProxyFactoryBean();
			
			invoker.setJndiName(jndiName);
			invoker.setJndiEnvironment(jndiEnvironment);
			invoker.setCacheHome(true);
			invoker.setRefreshHomeOnConnectFailure(true);
			invoker.setLookupHomeOnStartup(false);
			invoker.setResourceRef(true);

			proxy = ProxyFactory.getProxy(businessInterface, invoker);
			
			statelessProxies.put(jndiName, proxy);
			
			logger.debug("Created new SLSB [" + jndiName + "]");
		}

		return proxy;
	}
	
	public String getServiceRuntimeInfo() throws Exception {
		
		ApplicationServiceLocator serviceLocator = ApplicationServiceLocator.getInstance();
		ApplicationRuntimeHelper runtimeHelper = serviceLocator.getApplicationRuntimeHelper();
		
		PerformanceMonitor monitor = serviceLocator.getPerformanceMonitor(); 
		Document statsDocument = XmlUtil.parse(monitor.getReport());
		
		Document runtimeInfoDocument = XmlUtil.newDocument();
		
		String serviceName = serviceLocator.getApplicationName();
		String serviceVersion = ConfigContext.getApplicationVersion();

		// service info
		
		Element rootElement = runtimeInfoDocument.createElement("service-runtime-info");
		rootElement.setAttribute("name", serviceName);
		rootElement.setAttribute("version", serviceVersion);
		
		// environment
		
		Element environmentElement = runtimeInfoDocument.createElement("environment");
		environmentElement.setAttribute("domain", runtimeHelper.getDomainName());
		environmentElement.setAttribute("node", runtimeHelper.getNodeName());
		
		rootElement.appendChild(environmentElement);

		// resources
		
		Element resourcesElement = runtimeInfoDocument.createElement("resources");
		Map<String, Object> resources = enumerateRuntimeResources( new HashMap<String, Object>());
		for(Map.Entry<String, Object> resourceEntry : resources.entrySet()) {
			
			Element resourceElement = runtimeInfoDocument.createElement("resource");
			
			Object resource = resourceEntry.getValue();
			resourceElement.setAttribute("name", resourceEntry.getKey());
			resourceElement.setAttribute("value", resource == null ? "null" : resource.toString());
			resourcesElement.appendChild(resourceElement);
		}
		rootElement.appendChild(resourcesElement);
		
		// performance statistics
				
		rootElement.appendChild(runtimeInfoDocument.importNode(statsDocument.getDocumentElement(), true));
		
		runtimeInfoDocument.appendChild(rootElement);
		
		appendIdentityInfo( runtimeInfoDocument);
		
		return XmlUtil.toString(runtimeInfoDocument);
		
	}
	
	protected void appendIdentityInfo(Document runtimeInfoDocument) throws DOMException, Exception {
		try {

			Element rootElement = runtimeInfoDocument.getDocumentElement();
			
			IdentityContext	identityCtx =  getClientIdentityProvider().extractIdentityContext( getWebServiceContext() );
			
			Element securityElement = runtimeInfoDocument.createElement("identities");
			securityElement.setAttribute("userPrincipal", identityCtx.getPrincipal() );
			
			if ( identityCtx.getTelusTID()!=null ) {
				Element endUserIdElement = runtimeInfoDocument.createElement("endUserIdentity");
				endUserIdElement.setAttribute("telusTID", identityCtx.getTelusTID() );
				securityElement.appendChild(endUserIdElement);
			}
			
			Element applElement = runtimeInfoDocument.createElement("applIdentity");
			if ( identityCtx.getAppId()!=null ) {
				applElement.setAttribute("saml", identityCtx.getAppId() );
			}
			if ( identityCtx.getTelusHeaderAppId()!=null ) {
				applElement.setAttribute("telusHeader", identityCtx.getTelusHeaderAppId() );
			}
			securityElement.appendChild(applElement);
			
			rootElement.appendChild(securityElement);
			
		} catch (Exception e) {
			logger.warn("Unable to extract telus header from the request.");
		}
	}

	protected void assertValid(String paramName, Object paramValue, Object [] possibleValues) throws PolicyException {
		if (paramValue == null || (paramValue instanceof String && ((String)paramValue).trim().length() == 0)) {
			PolicyFaultInfo policyFaultInfo = new PolicyFaultInfo();
			policyFaultInfo.setErrorCode("ERROR_0002");
			policyFaultInfo.setErrorMessage(getPolicyErrorMessage(paramName, possibleValues));
			throw new PolicyException("Input parameter, " + paramName + ", validation error", policyFaultInfo);
			
		} else if (paramValue instanceof String) {
			String strValue = ((String) paramValue).trim();
			for (Object possibleValue : possibleValues) {
				if (strValue.equals(possibleValue)) {
					return;
				}
			}
			String errorMessage = getPolicyErrorMessage(paramName, possibleValues);
			PolicyFaultInfo policyFaultInfo = new PolicyFaultInfo();
			policyFaultInfo.setErrorCode("ERROR_0003");
			policyFaultInfo.setErrorMessage(errorMessage);
			throw new PolicyException(errorMessage, policyFaultInfo);
		}
	}
	
	protected String getPolicyErrorMessage(String paramName, Object[] possibleValues) {
		StringBuffer buffer = new StringBuffer("Input parameter [" + paramName + "] must be one of the following values: [");
		String pre = "";
		for (Object possibleValue : possibleValues) {
			buffer.append(pre).append(possibleValue.toString());
			pre = ", ";
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * Enumerates any of runtime resources to include in diagnostics
	 * 
	 * @param resources
	 * @return
	 */
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		return resources;
	}
	
	public String ping() throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<String>() {
			
			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Exception {
				return getServiceRuntimeInfo();
			}
		});
	}
	
	
	public AccountInformationHelper getAccountInformationHelper(ServiceInvocationContext context) throws Exception {
		return context.getStatelessProxy(AccountInformationHelper.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER, AppConfiguration.getAccountInformationHelperUrl());
	}
	
	public AccountLifecycleFacade getAccountLifecycleFacade(ServiceInvocationContext context) throws Exception {
		return context.getAccountLifecycleFacade();
	}
	
	public AccountLifecycleManager getAccountLifecycleManager(ServiceInvocationContext context) throws Exception {
		return context.getAccountLifecycleManager();
	}
	
	public SubscriberLifecycleHelper getSubscriberLifecycleHelper(ServiceInvocationContext context) throws Exception {
		return context.getSubscriberLifecycleHelper();
  	}
	
	public SubscriberLifecycleFacade getSubscriberLifecycleFacade(ServiceInvocationContext context) throws Exception {
		return context.getSubscriberLifecycleFacade();
	}
	
	public SubscriberLifecycleManager getSubscriberLifecycleManager(ServiceInvocationContext context) throws Exception {
		return context.getSubscriberLifecycleManager();
	}
	
	public ReferenceDataHelper getReferenceDataHelper(ServiceInvocationContext context) throws Exception {
		return context.getReferenceDataHelper();
	}
	
	public ReferenceDataFacade getReferenceDataFacade(ServiceInvocationContext context) throws Exception{
		return context.getReferenceDataFacade();
	}
	
	public ServiceOrderReferenceFacade getServiceOrderReferenceFacade() {
		return getStatelessProxy(ServiceOrderReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_SERVICE_ORDER_REFERENCE_FACADE, AppConfiguration.getServiceOrderReferenceFacadeUrl());
	}
	
	public ReferenceDataFacade getReferenceDataReferenceFacade() {
		return getStatelessProxy(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE, AppConfiguration.getReferenceDataFacadeUrl());
	}
	
	public CustomerInformationReferenceFacade getCustomerInformationReferenceFacade() {
		return getStatelessProxy(CustomerInformationReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_CUSTOMER_INFORMATION_REFERENCE_FACADE, AppConfiguration.getCustomerInformationReferenceFacadeUrl());
	}
	
	public CustomerOrderReferenceFacade getCustomerOrderReferenceFacade() {
		return getStatelessProxy(CustomerOrderReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_CUSTOMER_ORDER_REFERENCE_FACADE, AppConfiguration.getCustomerOrderReferenceFacadeUrl());
	}

	public EnterpriseReferenceFacade getEnterpriseReferenceFacade() {
		return getStatelessProxy(EnterpriseReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_ENTERPRISE_REFERENCE_FACADE, AppConfiguration.getEnterpriseReferenceFacadeUrl());
	}

	public BillingInquiryReferenceFacade getBillingInquiryReferenceFacade() {
		return getStatelessProxy(BillingInquiryReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_BILLING_INQUIRY_REFERENCE_FACADE, AppConfiguration.BillingInquiryReferenceFacadeUrl());
	}
	
	public String getHostIpAddress() {
		try {
		    return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.debug(e);
		}
		
		return "127.0.0.1";
	}
	
	public Log getLogger() {
		return logger;
	}
	
	//To be used by subclass that has telusTID in the service request payload.
	public void setTelusTidToMessageContext( String telusTID ) {
		if ( telusTID!=null && telusTID.trim().length()>0 ) {
			serviceContext.getMessageContext().put( ClientIdentityProvider.TELUS_TID, telusTID );
		}
	}
	
	protected Object getBean( String beanName ) {
		return getBean( serviceContext.getMessageContext(), beanName ) ;
	}
	
	public static Object getBean( MessageContext messageContext, String beanName ) {
		ServletContext servletContext = (ServletContext) messageContext.get(MessageContext.SERVLET_CONTEXT);
		org.springframework.web.context.WebApplicationContext webApplicationContext = org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		Object beanInstance = webApplicationContext.getAutowireCapableBeanFactory().getBean(beanName);
		return beanInstance;
		
	}

	
	//To be used by subclass to indicate when extract ClientIdentiy, ignore the end user identity
	//This method need to be invoked before any call to getClientIdentity() - meaning before any calls to
	//  ServiceInvocationContext.getXXXXSessionId() methods
	public void setUseApplicationIdentity() {
		serviceContext.getMessageContext().put( ClientIdentityProvider.USE_APP_IDENTITY, true );
		
	}
}
