/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import com.telus.api.account.AccountManager;
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
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.resource.ResourceManager;
import com.telus.api.rules.RulesProcessor;
import com.telus.api.servicerequest.ServiceRequestManager;
import com.telus.api.util.VersionReader;
import com.telus.cmnb.armx.agent.ApplicationMonitoringAgent;
import com.telus.cmnb.armx.agent.ApplicationMonitoringAgentFactory;

public final class ClientAPI implements Serializable {

  private static boolean descriptionDisplayed;
  private static ApplicationMonitoringAgent monitoringAgent = null;
  private static boolean enableMonitoringAgent=true;

  static {
    printProperty("java.home");
    printProperty("java.vm.version");
    printProperty("java.version");
    printProperty("java.runtime.version");
    printProperty("java.specification.version");
    printProperty("java.monitoring.agent");
    String s=System.getProperty("java.monitoring.agent");
    enableMonitoringAgent=(s==null||s.equals("true"))?true:false;
  }

  private static void printProperty(String property) {
    System.err.println("System Property:    [" + property + "] = [" + System.getProperty(property, "") + "]");
  }

  private Provider provider;

  public  static ClientAPI getInstance(String user, String password, String applicationCode, String audienceTypeCode) throws TelusAPIException, AuthenticationException {
    return new ClientAPI(user, password, applicationCode, audienceTypeCode, new int[0]);
  }

  public  static ClientAPI getInstance(String user, String password, String applicationCode) throws TelusAPIException, AuthenticationException {
    return new ClientAPI(user, password, applicationCode, null, new int[0]);
  }

  public  static ClientAPI getInstance(String user, String password, String applicationCode, String audienceTypeCode, int[] brandIds) throws TelusAPIException, AuthenticationException {
      return new ClientAPI(user, password, applicationCode, audienceTypeCode, brandIds);
    }

    public  static ClientAPI getInstance(String user, String password, String applicationCode, int[] brandIds) throws TelusAPIException, AuthenticationException {
      return new ClientAPI(user, password, applicationCode, null, brandIds);
    }


  private ClientAPI(String user, String password, String applicationCode, String audienceType, int[] brandIds) throws TelusAPIException, AuthenticationException {
    final String PROVIDER_CLASS = "com.telus.provider.TMProvider";
    
    validateInput (user, password);

    try {
      // Display provider's physical location, if possible.
      if (!descriptionDisplayed) {
        String classFile = PROVIDER_CLASS.replace('.', '/') + ".class";
        URL url = getClass().getClassLoader().getResource(classFile);
        System.err.println(PROVIDER_CLASS + " loaded from [" + url + "]");
      }

      // Instantiate the provider.
      if (audienceType != null) {
        Class providerClass = Class.forName(PROVIDER_CLASS);
        Constructor constructor = providerClass.getConstructor(new Class[] {String.class, String.class, String.class, String.class, int[].class});
        provider = (ClientAPI.Provider) constructor.newInstance(new Object[] {user, password, applicationCode, audienceType, brandIds});
      }
      else {
        Class providerClass = Class.forName(PROVIDER_CLASS);
        Constructor constructor = providerClass.getConstructor(new Class[] {String.class, String.class, String.class,int[].class});
        provider = (ClientAPI.Provider) constructor.newInstance(new Object[] {user, password, applicationCode,brandIds});
      }

      // publish diagnostic information
      startMonitoringAgent();

      if (!descriptionDisplayed) {
        System.err.println(getDescription());
        descriptionDisplayed = true;
      }
    }
    catch(InvocationTargetException e) {
      Throwable e2 = e.getTargetException();

      if (e2 == null) {
        throw new TelusAPIException(e);
      }
      else if (e2 instanceof TelusAPIException) {
        throw (TelusAPIException) e2;
      }
      else {
        throw new TelusAPIException(e2);
      }
    }
    catch(Throwable e) {
      throw new TelusAPIException(e);
    }
  }
  
	private void validateInput(String user, String password) throws AuthenticationException {
		if (user == null || password == null || user.trim().length() == 0 || password.trim().length() == 0) {
			throw new AuthenticationException("Invalid user [" + user + "] or password");
		}
	}

  /**
   * Returns the underlying service provider implementation.
   *
   * <P>This method is not intended for use in production, only testing.
   *
   */
  public Provider getProvider() {
    return provider;
  }

  public String getVersion() {
    return provider.getVersion();
  }

  public String getProviderVersion() {
    return provider.getVersion();
  }
  
  /**
   * @deprecated DO NOT USE
   * @return
   */
  public String getServiceVersion() {
    return "N/A";
  }

  public int[] getSupportedBrandIds(){
      return provider.getSupportedBrandIds();
  }

  public String getDescription() {
    return "TELUS ClientAPI JEE Provider version " + getVersion();
  }

  public String getUserRole() throws TelusAPIException {
    return provider.getUserRole();
  }

  public void setDealerCode(String newDealerCode) {
    provider.setDealerCode(newDealerCode);
  }

  public String getDealerCode() {
    return provider.getDealerCode();
  }

  public String getSalesRepId() {
    return provider.getSalesRepId();
  }

  public void setSalesRepId(String salesRepId) {
    provider.setSalesRepId(salesRepId);
  }

  public AccountManager getAccountManager() throws TelusAPIException {
    return provider.getAccountManager();
  }

  public FleetManager getFleetManager() throws TelusAPIException {
    return provider.getFleetManager();
  }

  public ResourceManager getResourceManager() {
    return provider.getResourceManager();
  }

  public EquipmentManager getEquipmentManager() throws TelusAPIException {
    return provider.getEquipmentManager();
  }

  public ReferenceDataManager getReferenceDataManager() throws
      TelusAPIException {
    return provider.getReferenceDataManager();
  }

  public com.telus.api.config1.ConfigurationManager getConfigurationManager() throws
      TelusAPIException {
    return provider.getConfigurationManager();
  }

  public com.telus.api.config1.ConfigurationManager getConfigurationManager1() throws TelusAPIException {
    return provider.getConfigurationManager1();
  }


  public DealerManager getDealerManager() throws TelusAPIException {
    return provider.getDealerManager();
  }

  public ChargeableServiceManager getChargeableServiceManager() throws
      TelusAPIException {
    return provider.getChargeableServiceManager();
  }

  public InteractionManager getInteractionManager() throws TelusAPIException {
    return provider.getInteractionManager();
  }

  public ContactEventManager getContactEventManager() throws TelusAPIException {
    return provider.getContactEventManager();
  }

  public QueueEventManager getQueueEventManager() throws TelusAPIException {
    return provider.getQueueEventManager();
  }

  public void reconnect() throws TelusAPIException {
    provider.reconnect();
  }

  public PortRequestManager getPortRequestManager() throws TelusAPIException {
    return provider.getPortRequestManager();
  }

  public ServiceRequestManager getServiceRequestManager() throws TelusAPIException {
	  return provider.getServiceRequestManager();
  }
/*  
  public SemsManager getSemsManager() throws TelusAPIException {
    return provider.getSemsManager();
  }
*/  
  public RulesProcessor getRulesProcessor(int processorId) throws TelusAPIException {
	return provider.getRulesProcessor(processorId);
  }

  /** 
   * @deprecated Temporary solution for CR742630. Except MU-CLP projects of OCT 2014 release, all other applications DO NOT use this one. It will be removed after all projects move to calling HCD service directly.  
   */
  public HCDManager getHCDManager() throws TelusAPIException {
	  return provider.getHCDManager();
  }
  
  public void destroy() {
    try {
      provider.destroy();
    }
    finally {
      provider = null;
    }
  }

  /**
   * Requires synchronization on the variable to prevent monitoringAgent to start twice. It can happen if there're two ClientAPI
   * instances are being created at the same time.
   */
	private synchronized void startMonitoringAgent() {
		if (monitoringAgent == null && enableMonitoringAgent) {
			monitoringAgent = ApplicationMonitoringAgentFactory.createApplicationMonitoringAgent();
			monitoringAgent.addModuleVersion("cmb-provider", provider.getVersion());
			monitoringAgent.start();
		}
	}

  public static interface Provider {

    String getVersion();

    int[] getSupportedBrandIds();

    String getUserRole() throws TelusAPIException;

    void setDealerCode(String newDealerCode);

    String getDealerCode();

    String getSalesRepId();

    void setSalesRepId(String salesRepId);

    AccountManager getAccountManager() throws TelusAPIException;

    ResourceManager getResourceManager();

    FleetManager getFleetManager() throws TelusAPIException;

    EquipmentManager getEquipmentManager() throws TelusAPIException;

    ReferenceDataManager getReferenceDataManager() throws TelusAPIException;

    com.telus.api.config1.ConfigurationManager getConfigurationManager() throws TelusAPIException;

    com.telus.api.config1.ConfigurationManager getConfigurationManager1() throws TelusAPIException;

    DealerManager getDealerManager() throws TelusAPIException;

    ChargeableServiceManager getChargeableServiceManager() throws
        TelusAPIException;

    InteractionManager getInteractionManager() throws TelusAPIException;

    ContactEventManager getContactEventManager() throws TelusAPIException;

    QueueEventManager getQueueEventManager() throws TelusAPIException;

    ApplicationMessage getApplicationMessage(long messageId) throws TelusAPIException;

    ApplicationMessage getApplicationMessage(String code, String textEn, String textFr);

    /**
     * Manager class for PRM functionality exposed through the TELUS API.
     * 
     * @return PortRequestManager
     * @throws TelusAPIException
     */
    PortRequestManager getPortRequestManager() throws TelusAPIException;

    /**
     * Manager class for SRPDS functionality exposed through the TELUS API.
     * 
     * @return ServiceRequestManager
     * @throws TelusAPIException
     */
    ServiceRequestManager getServiceRequestManager() throws TelusAPIException;
    
    /**
     * Manager class for Rules Processor functionality exposed through the TELUS API.
     * 
     * @return RulesProcessor
     * @throws TelusAPIException
     */
    
    RulesProcessor getRulesProcessor(int processorId) throws TelusAPIException;
    
    /** 
     * Manager class for HCD service exposed through the TELUS API.
     * @deprecated Temporary solution for CR742630. Except MU-CLP projects of OCT 2014 release, all other applications DO NOT use this one. It will be removed after all projects move to calling HCD service directly.
     * @return HCDManager  
     * @throws TelusAPIException
     */
    public HCDManager getHCDManager() throws TelusAPIException; 
    
    int getContextBrandId();

    void setContextBrandId(int brandId);

    void reconnect() throws TelusAPIException;

    void destroy();

    /**
     * Allows CSR to change his Knowbility password.
     * 
     * This method is introduced as part of May 2007 release based on
     * APILink 9.1
     * 
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @throws InvalidPasswordException
     * @throws TelusAPIException
     */
    void changeKnowbilityPassword(String oldPassword, String newPassword, String confirmPassword) throws InvalidPasswordException, TelusAPIException;
  }
  
  /**
   * The CMDB ID for ClientAPI 
   */
  public static final int CMDB_ID=5284;

}
