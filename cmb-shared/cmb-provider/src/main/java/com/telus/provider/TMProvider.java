package com.telus.provider;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import com.telus.api.ApplicationException;
import com.telus.api.AuthenticationException;
import com.telus.api.ClientAPI;
import com.telus.api.InvalidPasswordException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.ServiceFailureException;
import com.telus.api.chargeableservices.ChargeableServiceManager;
import com.telus.api.contactevent.ContactEventManager;
import com.telus.api.dealer.DealerManager;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.fleet.FleetManager;
import com.telus.api.hcd.HCDManager;
import com.telus.api.interaction.InteractionManager;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.queueevent.QueueEventManager;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.resource.ResourceManager;
import com.telus.api.rules.RulesException;
import com.telus.api.rules.RulesProcessor;
import com.telus.api.servicerequest.ServiceRequestManager;
import com.telus.api.util.ComponentEndpointConfiguration;
import com.telus.api.util.ComponentEndpointConfigurationManager;
import com.telus.api.util.ComponentEndpointConfigurationManagerImpl;
import com.telus.api.util.JNDINames;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionHandler;
import com.telus.api.util.VersionReader;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.monitoring.svc.MonitoringFacade;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.reference.svc.BillingInquiryReferenceFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.framework.config.LdapManager;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.chargeableservices.TMChargeableServiceManager;
import com.telus.provider.config1.TMConfigurationManager;
import com.telus.provider.contactevent.TMContactEventManager;
import com.telus.provider.dealer.TMDealerManager;
import com.telus.provider.equipment.TMEquipmentManager;
import com.telus.provider.fleet.TMFleetManager;
import com.telus.provider.hcd.TMHCDManager;
import com.telus.provider.interaction.TMInteractionManager;
import com.telus.provider.message.TMApplicationMessageManager;
import com.telus.provider.monitoring.scheduling.PerformanceMonitoringClearMethodCallsJob;
import com.telus.provider.monitoring.scheduling.PerformanceMonitoringLogMethodCallsJob;
import com.telus.provider.portability.TMPortRequestManager;
import com.telus.provider.queueevent.TMQueueEventManager;
import com.telus.provider.reference.TMReferenceDataManager;
import com.telus.provider.resource.TMResourceManager;
import com.telus.provider.rules.TMRulesProcessor;
import com.telus.provider.rules.message.TMMessageRulesProcessor;
import com.telus.provider.scheduling.IScheduler;
import com.telus.provider.scheduling.NowTrigger;
import com.telus.provider.scheduling.Scheduler;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.Logger;
import com.telus.provider.util.ProviderDefaultExceptionTranslator;
import com.telus.provider.util.ProviderLdapConfigurationManager;
import com.telus.provider.util.SubscriberManagerBean;
import com.telus.provider.util.SubscriberManagerBeanCmbImpl;
import com.telus.provider.util.transition.TMBrandTransitionMatrix;
import com.telus.provider.util.transition.TMEquipmentTransitionMatrix;


public class TMProvider implements ClientAPI.Provider, Serializable {

	private static final long serialVersionUID = 1L;
	private static final String version = VersionReader.getVersion("/appCtx-provider.properties");
	private static boolean monitorPerformanceStarted = false;

	private static final Object classLock = TMProvider.class;

	private String userRole;
	private final String user;
	private final int[] brandIds;
	private String password;
	private final String applicationCode;
	private final ApplicationSummary applicationSummary;
	private final AudienceType audienceType;
	private String dealerCode;
	private String salesRepId;

	private final TMAccountManager accountManager;
	private final TMResourceManager resourceManager;
	private final TMFleetManager fleetManager;
	private final TMEquipmentManager equipmentManager;
	private final TMDealerManager dealerManager;
	private final TMChargeableServiceManager chargeableServiceManager;
	private static TMConfigurationManager configurationManager;
	private static TMInteractionManager interactionManager;
	private static TMReferenceDataManager referenceDataManager;
	private  TMContactEventManager contactEventManager;
	private static TMQueueEventManager queueEventManager;
	private static TMApplicationMessageManager applicationMessageManager;
	private static TMEquipmentTransitionMatrix equipmentTransitionMatrix;
	private static TMBrandTransitionMatrix brandTransitionMatrix;
	private static TMRulesProcessor rulesProcessor;
//	private static Configuration configuration;
//	private static long lastConfigurationRefreshTime = System.currentTimeMillis();
	private TMPortRequestManager portRequestManager;
	private TMHCDManager hcdManager;
	private TMServiceRequestManager serviceRequestManager;
	private TMMessageRulesProcessor messageRulesProcessor;
	
	// Monitoring EJB
	private static MonitoringFacade monitoringFacadeEJB;
	
	// Reference EJB
	private static ReferenceDataHelper referenceDataHelperEJB;
	private static ReferenceDataFacade referenceDataFacadeEJB;
	
	private static BillingInquiryReferenceFacade billingInquiryReferenceFacadeEJB;
	
	// ACCOUNT EJB
	private static AccountInformationHelper  accountInformationHelper;
	private AccountLifecycleManager accountLifecycleManager;
	private AccountLifecycleFacade accountLifecycleFacade;
	
	// SUBSCRIBER EJB
	private static SubscriberLifecycleHelper subscriberLifecycleHelper;
	private SubscriberLifecycleManager subscriberLifecycleManager;	
	private SubscriberLifecycleFacade subscriberLifecycleFacade;
	
	// PRODUCT EQUIPMENT EJB
	private static ProductEquipmentHelper productEquipmentHelper;
	private ProductEquipmentManager productEquipmentManager;
	private ProductEquipmentLifecycleFacade productEquipmentLifecycleFacade;
	
	// Contact Event EJB
	private static com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager contactEventManagerNew;
	
	// Queue Event EJB
	private static com.telus.cmb.utility.queueevent.svc.QueueEventManager queueEventManagerNew;
	
	// Configuration Manager EJB
	private static com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager configurationManagerNew;

	// Activity Logging EJB - SRPDS
	private static com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService activityLoggingService;
	
	// New Dealer Manager EJB
	private com.telus.cmb.utility.dealermanager.svc.DealerManager dealerManagerEJB;

	private static ComponentEndpointConfigurationManager componentEndpointConfigurationManager;
	
	private TelusExceptionHandler exceptionHandler = new TelusExceptionHandler( new ProviderDefaultExceptionTranslator());

	private int contextBrandId = Brand.BRAND_ID_ALL;
	private static boolean initialized = false;
	private static LdapManager ldapManager;
	private SubscriberManagerBean subscriberManagerBean;
	
	//CDR confirmation notification changes begin
	//create an in-memory repository( a central place) for holding a account notification suppression indicator
	private final Map accountNofiicationSuppressionIndicatorMap = Collections.synchronizedMap(new HashMap(128));
	private final Map newSubscriberRegistry = new HashMap(128);

	static {
		try {
			runOnceInitialization();
		}
		catch (TelusAPIException e) {
			Logger.debug0("Provider static block failed. Will retry in constructor.");
		}
	}
	
	/**
	 * This method was originally part of the static block. However, failure in the backend while provider is starting up can cause
	 * NoClassDefFoundError as the class fails to load. This method would be invoked at constructor if it failed during static initialization.
	 * @throws TelusAPIException
	 */
	private static synchronized void runOnceInitialization() throws TelusAPIException {
		if (!initialized) {
			try {
				ldapManager = ProviderLdapConfigurationManager.getInstance(); //initialize LDAP Manager on startup
				// RemoteBeanProxyFactory.initialize can be used to fix invalid
				// subject ejb_user error.

				// Aug 04,2011 M.Liao: The following line would acquire
				// InitialContext from ECA nodes with empty user credential,
				// this
				// requires ECA node to be up although we don't use ECA EJB.
				// Comment out this line to remove this dependency.
				// RemoteBeanProxyFactory.initialize(ProviderUrlReader.getInstance().getProviderUrl());

				componentEndpointConfigurationManager = new ComponentEndpointConfigurationManagerImpl("cn=services,cn=CMB", "cmb.services");

				monitoringFacadeEJB = (MonitoringFacade) createSimpleRemoteBeanProxy(MonitoringFacade.class, JNDINames.TELUS_CMBSERVICE_MONITORING_FACADE);
				referenceDataHelperEJB = (ReferenceDataHelper) createSimpleRemoteBeanProxy(ReferenceDataHelper.class, JNDINames.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
				referenceDataFacadeEJB = (ReferenceDataFacade) createSimpleRemoteBeanProxy(ReferenceDataFacade.class, JNDINames.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
				accountInformationHelper = (AccountInformationHelper) createSimpleRemoteBeanProxy(AccountInformationHelper.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
				subscriberLifecycleHelper = (SubscriberLifecycleHelper) createSimpleRemoteBeanProxy(SubscriberLifecycleHelper.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
				productEquipmentHelper = (ProductEquipmentHelper) createSimpleRemoteBeanProxy(ProductEquipmentHelper.class, JNDINames.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER);

				contactEventManagerNew = (com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager) createSimpleRemoteBeanProxy(
						com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager.class, JNDINames.TELUS_CMBSERVICE_CONTACT_EVENT_MANAGER);
				queueEventManagerNew = (com.telus.cmb.utility.queueevent.svc.QueueEventManager) createSimpleRemoteBeanProxy(com.telus.cmb.utility.queueevent.svc.QueueEventManager.class,
						JNDINames.TELUS_CMBSERVICE_QUEUE_EVENT_MANAGER);
				configurationManagerNew = (com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager) createSimpleRemoteBeanProxy(
						com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager.class, JNDINames.TELUS_CMBSERVICE_CONFIGURATION_MANAGER);

				activityLoggingService = (com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService) createSimpleRemoteBeanProxy(
						com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService.class, JNDINames.TELUS_CMBSERVICE_ACTIVITY_LOGGING);

				Properties properties = System.getProperties();
				properties.list(System.out);
				initialized = true;
			} catch (Throwable e) {
				Logger.debug0("runOnceInitialization encounters error:");
				Logger.debug0(e);
				throw new TelusAPIException("Unable to instantiate ClientAPI. ", e);
			}
		}
	}
	
	public static void setComponentEndpointConfigurationManager(ComponentEndpointConfigurationManager componentEndpointConfigurationManagerInput) {
		componentEndpointConfigurationManager = componentEndpointConfigurationManagerInput;
	}

	private static Object createSimpleRemoteBeanProxy(Class type, String jndiName) {
		ComponentEndpointConfiguration endpointConfiguration = componentEndpointConfigurationManager.getComponentEndpointConfiguration(type);
		if (endpointConfiguration == null) {
			throw new RuntimeException("Endpoint configuration is not defined for " + type.getName());
		}
		return RemoteBeanProxyFactory.createProxy(type, jndiName, endpointConfiguration.getUrl(),
				JNDINames.SECURITY_PRINCIPAL_NAME, JNDINames.SECURITY_CREDENTIALS);
	}
	
	private Object createSessionRemoteBeanProxy(Class type, String jndiName) {
		ComponentEndpointConfiguration endpointConfiguration = componentEndpointConfigurationManager.getComponentEndpointConfiguration(type);
		if (endpointConfiguration == null) {
			throw new RuntimeException("Endpoint configuration is not defined for " + type.getName());
		}
		return RemoteBeanProxyFactory.createProxy(type, jndiName, endpointConfiguration.getUrl(),
				JNDINames.SECURITY_PRINCIPAL_NAME, JNDINames.SECURITY_CREDENTIALS, user, password, applicationCode);
	}
	
	public static boolean isComponentEnabled(Class type) {
		ComponentEndpointConfiguration endpointConfiguration = componentEndpointConfigurationManager.getComponentEndpointConfiguration(type);
		return endpointConfiguration.isUsedByProvider();
	}
	
	public static boolean isProjectRolledback(String rollbackProjectName) {
		ComponentEndpointConfiguration endpointConfiguration = componentEndpointConfigurationManager.getComponentEndpointConfiguration(rollbackProjectName);
			return endpointConfiguration.isUsedByProvider();
	}


	public TMProvider(String user, String password, String applicationCode, int[] brandIds) throws TelusAPIException {
		this(user,password,applicationCode,null, brandIds);
	}

	public TMProvider(String user, String password, String applicationCode, String audienceTypeCode,int[] brandIds) throws TelusAPIException {
		if (!monitorPerformanceStarted) {
			monitorPerformance();
		}
		if (!initialized) {
			runOnceInitialization();
		}
		try {
			this.user = user;
			this.password = password;
			this.applicationCode = applicationCode;
			this.brandIds = brandIds;
						
			accountLifecycleManager = (AccountLifecycleManager) createSessionRemoteBeanProxy(AccountLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);
			
			//Authenticating KB user shall be the first thing when creating TMProvider instance, but it requires AccountLifecycleManager, 
			//so place the call after above line.
			authenticateKBUser(user, password, applicationCode);
			
			accountLifecycleFacade = (AccountLifecycleFacade) createSessionRemoteBeanProxy(AccountLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE);
			
			subscriberLifecycleManager = (SubscriberLifecycleManager) createSessionRemoteBeanProxy(SubscriberLifecycleManager.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER);
			subscriberLifecycleFacade = (SubscriberLifecycleFacade) createSessionRemoteBeanProxy(SubscriberLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE);
			
			productEquipmentManager = (ProductEquipmentManager) createSessionRemoteBeanProxy(ProductEquipmentManager.class, JNDINames.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_MANAGER);
			productEquipmentLifecycleFacade = (ProductEquipmentLifecycleFacade) createSessionRemoteBeanProxy(ProductEquipmentLifecycleFacade.class, JNDINames.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_LIFECYCLE_FACADE);
			
			dealerManagerEJB = (com.telus.cmb.utility.dealermanager.svc.DealerManager) createSessionRemoteBeanProxy(com.telus.cmb.utility.dealermanager.svc.DealerManager.class, JNDINames.TELUS_CMBSERVICE_DEALER_MANAGER);			
			billingInquiryReferenceFacadeEJB = (BillingInquiryReferenceFacade)createSessionRemoteBeanProxy(BillingInquiryReferenceFacade.class, JNDINames.TELUS_CMBSERVICE_BILLING_INQUIRY_REFERENCE_DATA_FACADE);

			// create references to static managers.
			if (configurationManager == null || referenceDataManager == null
					|| interactionManager == null
					|| queueEventManager == null
					) {
				synchronized (classLock) {
					if (configurationManager == null) {
						configurationManager = new TMConfigurationManager(this);
//						configuration = configurationManager.lookup(CONFIGURATION_PATH);
					}
				
					if (referenceDataManager == null)
						referenceDataManager = new TMReferenceDataManager( this, referenceDataHelperEJB, referenceDataFacadeEJB);

					if (interactionManager == null)
						interactionManager = new TMInteractionManager(this);

					if (queueEventManager == null)
						queueEventManager = new TMQueueEventManager(this);

				}
			}

			// set default application summary and audience type if the codes are valid.
			if (audienceTypeCode == null) {
				// set default application summary and audience type. if
				// audienceTypeCode == null
				audienceType = AudienceType.DEFAULT;
				ApplicationSummary appSummary = null;
				try {
					appSummary = referenceDataManager.getApplicationSummary(applicationCode);
				} catch (Throwable t) {
					// eat the exception and log the warning
					Logger.warning("ApplicationSummary for applicationCode = [" + applicationCode + 
					"] returned exception.  Using default ApplicationSummary.");
					Logger.warning(t);
				}
				// if the appSummary is null, assign applicationSummary to the default
				if (appSummary == null)
					applicationSummary = ApplicationSummary.DEFAULT;
				else
					applicationSummary = appSummary;

			} else {
				applicationSummary = referenceDataManager.getApplicationSummary(applicationCode);
				audienceType = referenceDataManager.getAudienceType(audienceTypeCode);

				if (applicationSummary == null)
					throw new TelusAPIException(
							"applicationCode doesn't correspond to any registered application: "
							+ applicationCode);

				if (audienceType == null)
					throw new TelusAPIException(
							"audienceTypeCode doesn't correspond to any registered audience type: "
							+ audienceType);
			}
			

			// create references to instance managers.
			accountManager = new TMAccountManager(this);
			resourceManager = new TMResourceManager(this);
			fleetManager = new TMFleetManager(this);
			equipmentManager = new TMEquipmentManager(this);
			dealerManager = new TMDealerManager(this);
			chargeableServiceManager = new TMChargeableServiceManager(this);
			contactEventManager = new TMContactEventManager(this);
			
			subscriberManagerBean = new SubscriberManagerBeanCmbImpl(subscriberLifecycleManager);

		}catch(TelusException e) {
			if ("SYS00011".equals(e.id)) {
				throw new AuthenticationException("user=[" + user + "]", e);
			}
			else {
				throw new TelusAPIException(e);
			}
		}
		catch(TelusAPIException e) {
			throw e;
		}
		catch(Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	private void authenticateKBUser(String user, String password,
			String applicationCode) throws AuthenticationException,
			TelusAPIException, RemoteException, TelusException {
		
			try{
				Logger.debug("Acquiring AmdocsSession id...");
				accountLifecycleManager.openSession(user, password, applicationCode);
			}catch(ApplicationException ae){
				throw new AuthenticationException("user=[" + user + "] : ",ae);
			}catch( Throwable t){
				throw new TelusAPIException(t);
			}
	}

	//--------------------------------------------------------------------
	// Accessors: general.
	//--------------------------------------------------------------------
	public String getVersion() {
		return version;
	}

	public String getUserRole() throws TelusAPIException {
		try {
			if (userRole == null) {
					 userRole =  getAccountLifecycleManager().getUserProfileID(SessionUtil.getSessionId(getAccountLifecycleManager()));
			}
			return userRole;
		}catch (Throwable e) {
			getExceptionHandler().handleException(e);
		}
		return null;
	}

	public void setDealerCode(String newDealerCode) {
		dealerCode = newDealerCode;
	}

	public String getDealerCode() {
		return dealerCode;
	}

	public String getSalesRepId() {
		return salesRepId;
	}

	public void setSalesRepId(String salesRepId) {
		this.salesRepId = salesRepId;
	}

	public boolean isDealerCodeSet() {
		return dealerCode != null && dealerCode.length() > 0;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getApplication() {
		return applicationCode;
	}

	public void changeKnowbilityPassword(String oldPassword, String newPassword, String confirmPassword) throws InvalidPasswordException, TelusAPIException {

		if (oldPassword == null || newPassword == null || confirmPassword == null) {
			throw new java.lang.IllegalArgumentException("oldPassword, newPassword, or confirmPassword cannot be null");
		}
		else if (!newPassword.equals(confirmPassword)) {
			throw new InvalidPasswordException(InvalidPasswordException.PASSWORD_MISMATCH, "newPassword and confirmPassword must be the same, case-sensative.");
		}

		try {
			this.getAccountLifecycleManager().changeKnowbilityPassword(user, oldPassword, newPassword, SessionUtil.getSessionId(getAccountLifecycleManager()));

		} catch (Throwable t) {
			
			getExceptionHandler().handleException( t, new ProviderDefaultExceptionTranslator() {
				protected TelusAPIException getExceptionForErrorId( String errorId , Throwable throwable ) {
					
					if (throwable instanceof ApplicationException) {
						ApplicationException ae= ((ApplicationException) throwable);
						String errorCode =	ae.getErrorCode();
						int reason = 0;
						
					    if(errorCode.equals("1118020")) reason = InvalidPasswordException.OLD_PASSWORD_INCORRECT; 
					    if(errorCode.equals("1118021")) reason = InvalidPasswordException.INVALID_PASSWORD_LENGTH; 
					    if(errorCode.equals("1118022")) reason = InvalidPasswordException.INVALID_PASSWORD_FIRST_LAST_DIGIT; 
					    if(errorCode.equals("1118023")) reason = InvalidPasswordException.INVALID_PASSWORD_NO_DIGIT; 
					    if(errorCode.equals("1118024")) reason = InvalidPasswordException.INVALID_PASSWORD_ALREADY_USED; 
					     
						return new InvalidPasswordException(reason, "Amdocs threw back-end validation exception for changing password. Use InvalidPasswordException.getReason() to see the detail.", ae);
						
					}
					return null;					
					
				}
			}
			);
		}

		this.password = newPassword;

		return;

	}

	//--------------------------------------------------------------------
	// Accessors: Configuration.
	//--------------------------------------------------------------------
//	public static Configuration getConfiguration() {
//		refreshConfiguration(false);
//		return configuration;
//	}
//
//	public static void refreshConfiguration(boolean forceRefresh) {
//		try {
//			// ignore forceRefresh
//			// TODO: refresh if last access time >  threshold (5 minutes)
//			// TODO: record access time.
//			long now = System.currentTimeMillis();
//			if (now - lastConfigurationRefreshTime > CONFIGURATION_REFRESH) {
//				lastConfigurationRefreshTime = now;
//				configuration = configurationManager.lookup(CONFIGURATION_PATH);
//			}
//		}
//		catch (TelusAPIException e) {
//			Logger.debug(e);
//		}
//	}

//	public void refreshConfiguration() {
//		refreshConfiguration(true);
//	}

	//--------------------------------------------------------------------
	// Accessors: Client Managers.
	//--------------------------------------------------------------------
	public AccountManager getAccountManager() throws TelusAPIException {
		return accountManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public FleetManager getFleetManager() throws TelusAPIException {
		return fleetManager;
	}

	public EquipmentManager getEquipmentManager() throws TelusAPIException {
		return equipmentManager;
	}

	public com.telus.api.config1.ConfigurationManager getConfigurationManager() throws TelusAPIException {
		return configurationManager;
	}

	public com.telus.api.config1.ConfigurationManager getConfigurationManager1() throws TelusAPIException {
		return configurationManager;
	}

	public InteractionManager getInteractionManager() throws TelusAPIException {
		return interactionManager;
	}

	public ContactEventManager getContactEventManager() throws TelusAPIException {
		return contactEventManager;
	}

	public QueueEventManager getQueueEventManager() throws TelusAPIException {
		return queueEventManager;
	}

	public ReferenceDataManager getReferenceDataManager() throws TelusAPIException {
		return referenceDataManager;
	}

	public DealerManager getDealerManager() throws TelusAPIException {
		return dealerManager;
	}

	public ChargeableServiceManager getChargeableServiceManager() throws TelusAPIException {
		return chargeableServiceManager;
	}

	private synchronized TMApplicationMessageManager getApplicationManager() {
		if (applicationMessageManager==null) {
			try {
				applicationMessageManager = new TMApplicationMessageManager( referenceDataHelperEJB );
			} catch (Throwable e) {
				throw new SystemException( SystemCodes.PROVIDER, "Error instantiating TMApplicationMessageManager", "", e );
			}
		}
		return applicationMessageManager;
	}
	public ApplicationMessage getApplicationMessage(long messageId) {
		return getApplicationManager().getApplicationMessage(applicationSummary, audienceType, getContextBrandId(), messageId);
	}

	public ApplicationMessage getApplicationMessage(String sourceApplicationCode, String sourceMessageCode, int brandId) {
		return getApplicationManager().getApplicationMessage(applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode, brandId);
	}

	public ApplicationMessage getApplicationMessage(String sourceApplicationCode, String sourceMessageCode) {
		return getApplicationManager().getApplicationMessage(applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode, Brand.BRAND_ID_ALL);
	}

	public ApplicationMessage getApplicationMessage(String sourceApplicationCode, String sourceMessageCode, int brandId,ApplicationException ae) {
		return getApplicationManager().getApplicationMessage(applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode, brandId,ae);
	}

	public ApplicationMessage getApplicationMessage(String sourceApplicationCode, String sourceMessageCode,ApplicationException ae) {
		return getApplicationManager().getApplicationMessage(applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode, Brand.BRAND_ID_ALL,ae);
	}
	public ApplicationMessage getApplicationMessage(String code, String textEn, String textFr) {
		return getApplicationManager().getApplicationMessage(code, textEn, textFr);
	}

	public ApplicationMessage getApplicationMessageWithSourceCode(long messageId) {
		return getApplicationManager().getApplicationMessageWithSourceCode(applicationSummary, audienceType, getContextBrandId(), messageId);
	}

	public synchronized PortRequestManager getPortRequestManager() throws TelusAPIException {
		if (portRequestManager == null) {
			portRequestManager = new TMPortRequestManager(this);
		}
		return portRequestManager;
	}

    /** 
     * @deprecated Temporary solution for CR742630. Except MU-CLP projects of OCT 2014 release, all other applications DO NOT use this one. It will be removed after all projects move to calling HCD service directly.
     */
	public HCDManager getHCDManager() throws TelusAPIException {
		if (hcdManager == null) {
			hcdManager =  new TMHCDManager(this);
		}
		return hcdManager;
	}

	public synchronized ServiceRequestManager getServiceRequestManager() throws TelusAPIException {
		if (serviceRequestManager == null) {
			serviceRequestManager = new TMServiceRequestManager(this);
		}
		return serviceRequestManager;
	}

	public synchronized RulesProcessor getRulesProcessor(int processorId) throws RulesException, TelusAPIException {	  

		switch (processorId) {	  
		case RulesProcessor.PROCESSOR_ID_MESSAGE:
			if (messageRulesProcessor == null) {
				messageRulesProcessor = new TMMessageRulesProcessor(this);
			}
			return messageRulesProcessor;

		default:
			throw new RulesException("Invalid processor ID [" + processorId + "].", RulesException.REASON_INVALID_PROCESSOR_ID);
		}
	}

	public int getContextBrandId() {
		return contextBrandId;
	}

	public void setContextBrandId(int brandId) {
		this.contextBrandId = brandId;
	}

	//--------------------------------------------------------------------
	// Accessors: Internal Client Managers.
	//--------------------------------------------------------------------
	public TMAccountManager getAccountManager0() throws TelusAPIException {
		return accountManager;
	}

	public TMResourceManager getResourceManager0() {
		return resourceManager;
	}

	public TMFleetManager getFleetManager0() throws TelusAPIException {
		return fleetManager;
	}

	public TMEquipmentManager getEquipmentManager0() throws TelusAPIException {
		return equipmentManager;
	}

	public TMConfigurationManager getConfigurationManager0() {
		return configurationManager;
	}

	public com.telus.provider.config1.TMConfigurationManager getConfigurationManager10() throws
	TelusAPIException {
		return configurationManager;
	}

	public TMInteractionManager getInteractionManager0() throws TelusAPIException {
		return interactionManager;
	}

	public TMReferenceDataManager getReferenceDataManager0() throws
	TelusAPIException {
		return referenceDataManager;
	}

	public TMChargeableServiceManager getChargeableServiceManager0() throws
	TelusAPIException {
		return chargeableServiceManager;
	}

	public TMContactEventManager getContactEventManager0() throws TelusAPIException {
		return contactEventManager;
	}

	public TMQueueEventManager getQueueEventManager0() throws TelusAPIException {
		return queueEventManager;
	}

	public synchronized TMEquipmentTransitionMatrix getEquipmentTransitionMatrix0() throws TelusAPIException {
		if (equipmentTransitionMatrix==null) {
			try {
				equipmentTransitionMatrix = new TMEquipmentTransitionMatrix( referenceDataHelperEJB );
			} catch (Throwable e) {
				throw new TelusAPIException ( "Error instantiating  TMEquipmentTransitionMatrix", e  );
			}
		}
		return equipmentTransitionMatrix;
	}

	public synchronized TMBrandTransitionMatrix getBrandTransitionMatrix0() throws TelusAPIException {
		if ( brandTransitionMatrix==null ) {
			try {
				brandTransitionMatrix = new TMBrandTransitionMatrix( referenceDataHelperEJB );
			} catch (Throwable e ) {
				throw new TelusAPIException ( "Error instantiating TMBrandTransitionMatrix ", e );
			}
		}
		return brandTransitionMatrix;
	}

	public synchronized TMRulesProcessor getRulesProcessor0() throws TelusAPIException {
		try {
			if (rulesProcessor == null)
				rulesProcessor = new TMRulesProcessor(referenceDataHelperEJB);
		} catch (Throwable t) {
			Logger.warning("Rules processor initialization failure.");
			Logger.warning(t);
			throw new RulesException("Rules processor initialization failure.", RulesException.REASON_PROCESSOR_INITIALIZATION_FAILURE, t);
		}	  
		return rulesProcessor;
	}

	//--------------------------------------------------------------------
	// Accessors: EJBs.
	//--------------------------------------------------------------------

	public synchronized ReferenceDataHelper getReferenceDataHelperEJB() throws
	TelusAPIException, AuthenticationException {
		return referenceDataHelperEJB;
	}
	
	public ReferenceDataFacade getReferenceDataFacade() {
		return referenceDataFacadeEJB;
	}

	public SubscriberLifecycleFacade getSubscriberLifecycleFacade() {
		return subscriberLifecycleFacade;
	}

	
	public com.telus.cmb.utility.dealermanager.svc.DealerManager getDealerManagerEJB() {
		return dealerManagerEJB;
	}	

	public static MonitoringFacade getMonitoringFacadeEJB() {
		return monitoringFacadeEJB;
	}

	//--------------------------------------------------------------------
	// Subscriber Registry Methods.
	//--------------------------------------------------------------------
	private Object getKey(SubscriberInfo info) {
		return new Integer(System.identityHashCode(info));
	}

	public synchronized void registerNewSubscriber(SubscriberInfo info) {
		//    if(info.getStatus() == 0 && info.getSubscriberId() != null) {
		if (info.getStatus() == 0) {
			Logger.debug("TMProvider#" + System.identityHashCode(this) +
					".registerNewSubscriber(" + info.getSubscriberId() + ")");
			Object key = getKey(info);
			//Object value = new SubscriberSummaryInfo(info);

			newSubscriberRegistry.put(key, info);
		}
		Logger.debug("TMProvider#" + System.identityHashCode(this) +
		".registerNewSubscriber(): END");
	}

	public synchronized void unregisterNewSubscriber(SubscriberInfo info) {
		Logger.debug("TMProvider#" + System.identityHashCode(this) +
		".unregisterNewSubscriber(): BEGIN");
		Logger.debug(info.toString());
		//    debug("TMProvider#"+System.identityHashCode(this)+".unregisterNewSubscriber("+info.getSubscriberId()+")");
		Logger.debug("TMProvider#" + System.identityHashCode(this) +
				".unregisterNewSubscriber(" + info.getPhoneNumber() + ")");
		Object key = getKey(info);

		newSubscriberRegistry.remove(key);
		Logger.debug("TMProvider#" + System.identityHashCode(this) +
		".unregisterNewSubscriber(): END");
	}

	public synchronized void releaseNewSubscribers() {
		Logger.debug("TMProvider#" + System.identityHashCode(this) +
		".releaseNewSubscribers(): BEGIN");
		SubscriberInfo[] info = (SubscriberInfo[]) newSubscriberRegistry.values().
		toArray(
				new SubscriberInfo[newSubscriberRegistry.size()]);
		newSubscriberRegistry.clear();

		Logger.debug("TMProvider#" + System.identityHashCode(this) +
				".releaseNewSubscribers(): info.length=" + info.length);
		for (int i = 0; i < info.length; i++) {
			try {

				Logger.debug("    Releasing: " + info[i]);
				if (!info[i].isPortInd())
					getSubscriberManagerBean().releaseSubscriber(info[i]);
				else
					getSubscriberManagerBean().releasePortedInSubscriber(info[i]);
			}
			catch (Throwable e) {
				Logger.debug(e);
			}
		}
	}

	public synchronized void destroy() {
		this.ldapManager.destroy();
	}

	protected void finalize() throws Throwable {
		try {
			releaseNewSubscribers();
			destroy();
		}
		finally {
			super.finalize();
		}
	}

	public synchronized void reconnect() throws TelusAPIException {

	}

	

	//--------------------------------------------------------------------
	// General.
	//--------------------------------------------------------------------
	public static final boolean messageContains(Throwable e, String string) {
		if (e == null || string == null) {
			return false;
		}

		String message = e.getMessage();
		if (message != null && message.indexOf(string) >= 0) {
			return true;
		} else {
			return messageContains(e.getCause(), string);
		}
	}

	public static final String[] stringToArray(String string) {
		return stringToArray(string, null);
	}

	public static final String[] stringToArray(String string, String delimeters) {

		if (string == null) {
			return new String[0];
		}

		List list = new ArrayList(10);
		StringTokenizer t = (delimeters == null) ? new StringTokenizer(string) : new StringTokenizer(string, delimeters);
		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}

		return (String[])list.toArray(new String[list.size()]);
	}

	// converts a Date object into a String based on the supplied date format
	public static String dateToString(Date dateDateArg, String dateFormatArg) {

		// Format the date passed in
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(dateFormatArg);
		String dateString = formatter.format(dateDateArg);

		return dateString;
	}

	// converts a String array of key-value pairs to a HashMap
	public static HashMap convertKeyValuePairsToHashMap(String[] stringArray, String delimiters) {

		HashMap hashMap = new HashMap();
		ArrayList list = new ArrayList();

		// key-value pairs are separated by delimiters - parse these into the array list		 
		for (int i = 0; i < stringArray.length; i++) {	 
			String[] keyValuePair = stringToArray(stringArray[i], delimiters);
			list.add(keyValuePair);			 
		}

		// populate the HashMap
		for (int j = 0; j < list.size(); j++) {
			hashMap.put(((String[])list.get(j))[0], ((String[])list.get(j))[1]);
		}

		Logger.debug("successfully parsed key-value pairs");
		Logger.debug(hashMap.toString());

		return hashMap;
	}

	public static final void checkServiceFailure(TelusException e) throws
	ServiceFailureException {
		checkServiceFailure(e, e.id);
	}

	public static final void checkServiceFailure(Throwable e, String exceptionId) throws ServiceFailureException {

		if ("APP10007".equals(exceptionId)) {
			throw new ServiceFailureException(e, "Knowbility",
					ServiceFailureException.
					SERVICE_KNOWBILITY);
			/*
           } else if("APP10008".equals(exceptionId)) {
        throw new ServiceFailureException(e, "BIH");
			 */
		} else if ("SYS00009".equals(exceptionId)) {
			throw new ServiceFailureException(e, "PrepaidAccountManager",
					ServiceFailureException.
					SERVICE_PREPAID_ACCOUNT_MANAGER);
		} else if ("SYS00013".equals(exceptionId)) {
			throw new ServiceFailureException(e, "credit check applicationCode (CRISK)",
					ServiceFailureException.SERVICE_CRISK);
		} else if ("SYS00014".equals(exceptionId) || "APP10008".equals(exceptionId)) {
			throw new ServiceFailureException(e,
					"credit card transaction applicationCode (BIH)",
					ServiceFailureException.SERVICE_BIH);
		}
	}

	//==============================================================================
	// Performance Monitoring
	//==============================================================================
	private static final long LOG_INTERVAL = 1000L * 60L * 5L; // 5 minutes
	private static final long CLEAR_LOG_INTERVAL = 1000L * 60L * 60L; // 60 minutes

	private static final Map map = new HashMap(1024);

	public static final void addMethodCall(Method method, long time) {
		String key = method.toString();

		MethodCall call = null;

		synchronized (map) {
			call = (MethodCall) map.get(key);
			if (call == null) {
				call = new MethodCall();
				call.name = key;
				map.put(key, call);
			}
		}

		call.count++;
		call.accumulatedTime += time;
	}

	public static final void clearMethodCalls() {
		synchronized (map) {
			if (map.size() > 0) {
				Logger.debug0("<resetting method calls statistics>");
				map.clear();
			}
		}
	}

	public static final void logMethodCalls() {
		MethodCall[] calls = null;

		synchronized (map) {
			if (map.size() > 0) {
				calls = (MethodCall[]) map.values().toArray(new MethodCall[map.size()]);
			}
		}

		if (calls != null && calls.length > 0) {
			Arrays.sort(calls);

			StringBuffer s = new StringBuffer(1024 * 2);

			s.append(Logger.LOG_DATE_FORMAT.format(new Date()));
			s.append("|");
			s.append(Thread.currentThread().getName());
			s.append("\n");
			s.append("<begin method calls statistics>\n");
			s.append("average call time (seconds) - number of calls - accumulated call time (seconds) - method\n");
			for (int i = 0; i < calls.length; i++) {
				s.append("  " + (calls[i].accumulatedTime / calls[i].count / 1000.0) +
						"  -  " + calls[i].count + "  -  " +
						(calls[i].accumulatedTime / 1000.0) + "  -  " + calls[i].name).append('\n');
			}
			s.append("<end method calls statistics>\n");
			Logger.debug0(s);
		}
	}

	private static final synchronized void monitorPerformance() {
		if (!monitorPerformanceStarted) {
			try {
				IScheduler sched = Scheduler.getInstance();

				sched.addJob(new NowTrigger(LOG_INTERVAL), new PerformanceMonitoringLogMethodCallsJob());
				sched.addJob(new NowTrigger(CLEAR_LOG_INTERVAL), new PerformanceMonitoringClearMethodCallsJob());
				sched.start();
				monitorPerformanceStarted = true;
			}catch (Exception e) {
				Logger.debug("monitorPerformance error. Retry in constructor. " + e);
			}
		}
	}

	/*
	 */

	private static final class MethodCall
	implements Comparable {
		public String name;
		public long count;
		public long accumulatedTime;

		public int compareTo(Object o) {
			MethodCall o2 = (MethodCall) o;
			return (accumulatedTime < o2.accumulatedTime ? 1 :
				(accumulatedTime == o2.accumulatedTime ? 0 : -1));
		}

	}

	public int[] getSupportedBrandIds() {
		return brandIds;
	}

	private AuditHeader defaultAuditHeader;
	private void appendProvider( AuditHeader auditHeader ) {
		String localIP="127.0.0.1";
		try {
			localIP = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			Logger.debug(e);
		} 
		auditHeader.appendAppInfo( 
				"kbUser=" + getUser () + ", appCode=" +getApplication() + "@ClientAPI-Provider",
				ClientAPI.CMDB_ID,
				localIP
		);
	}
	public AuditHeader appendToAuditHeader( AuditHeader auditHeader ) {
		AuditHeader newHeader = new AuditHeaderInfo ( auditHeader );
		if ( auditHeader!=defaultAuditHeader ) {
			appendProvider( newHeader );
		}
		return newHeader;
	}
	public AuditHeader getAuditHeader() {
		if ( defaultAuditHeader==null) {
			defaultAuditHeader = new AuditHeaderInfo();  
			appendProvider(defaultAuditHeader );
		}
		return defaultAuditHeader;
	}

	public AccountLifecycleManager getAccountLifecycleManager() {
		return accountLifecycleManager;
	}
	
	public AccountInformationHelper getAccountInformationHelper() {
		return accountInformationHelper;
	}	
	
	public AccountLifecycleFacade getAccountLifecycleFacade() {
		return accountLifecycleFacade;
	}

	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() {
		return subscriberLifecycleHelper;
	}
	
	public SubscriberLifecycleManager getSubscriberLifecycleManager() {
		return subscriberLifecycleManager;
	}

	public ProductEquipmentHelper getProductEquipmentHelper() {
		return productEquipmentHelper;
	}

	public ProductEquipmentManager getProductEquipmentManager() {
		return productEquipmentManager;
	}

	public ProductEquipmentLifecycleFacade getProductEquipmentLifecycleFacade() {
		return productEquipmentLifecycleFacade;
	}

	public com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager getContactEventManagerNew() {
		return contactEventManagerNew;
	}

	public com.telus.cmb.utility.queueevent.svc.QueueEventManager getQueueEventManagerNew() {
		return queueEventManagerNew;
	}

	public com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager getConfigurationManagerNew() {
		return configurationManagerNew;
	}
	
	public com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService getActivityLoggingService() {
		return activityLoggingService;
	}	

	public BillingInquiryReferenceFacade getBillingInquiryReferenceFacade() {
		return billingInquiryReferenceFacadeEJB;
	}	


	public TelusExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public SubscriberManagerBean getSubscriberManagerBean() {
		return subscriberManagerBean;
	}
	



	public void setAccountNotificationSuppressionIndicator( int banId, boolean flag ) {
		Integer key = new Integer( banId );
		accountNofiicationSuppressionIndicatorMap.put(key, Boolean.valueOf(flag) );
	}
	public boolean getAccountNotificationSuppressionIndicator( int banId ) {
		Integer key = new Integer( banId );
		if ( accountNofiicationSuppressionIndicatorMap.containsKey(key) ==false )
			//default notificationSuppressionIndicator=false;  
			return false;
		
		return ((Boolean)accountNofiicationSuppressionIndicatorMap.get(key)).booleanValue();
		
	}
	//CDR confirmation notification changes end
		
}
