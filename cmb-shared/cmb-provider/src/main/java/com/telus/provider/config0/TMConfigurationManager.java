/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.config0;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Subscriber;
import com.telus.api.config0.Configuration;
import com.telus.api.config0.ConfigurationManager;
import com.telus.eas.config.info.ConfigurationInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMConfigurationManager extends BaseProvider implements ConfigurationManager {

   private static final long serialVersionUID = 1L;
  
  public TMConfigurationManager(TMProvider provider) {
		super(provider);
  }

  
  public Configuration getApplications() throws TelusAPIException {
    return getApplications0();
  }

  public TMConfiguration getApplications0() throws TelusAPIException {
    return getConfiguration0(new String[]{"application"});
  }

  public Configuration getConfiguration(String[] path) throws TelusAPIException, UnknownObjectException {
    return getConfiguration0(path);
  }

  public TMConfiguration getConfiguration0(String[] path) throws TelusAPIException, UnknownObjectException {
    try {
    	return decoratePath(provider.getConfigurationManagerNew().getConfiguration(path));
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return null;
  }

  public Configuration[] getChildren(Configuration parent) throws TelusAPIException, UnknownObjectException {
    return getChildren0(parent);
  }

  public TMConfiguration[] getChildren0(Configuration parent) throws TelusAPIException, UnknownObjectException {
    try {
    	return decorateChildren((TMConfiguration)parent,provider.getConfigurationManagerNew().getChildConfigurations(parent.getId()));
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return null;
  }

  public int removeConfiguration(Configuration configuration) throws TelusAPIException {
    try {
    		return provider.getConfigurationManagerNew().removeConfiguration(configuration.getId());
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return 0;
      
  }

  public Configuration newConfiguration(Configuration parent, String name) throws TelusAPIException {
    return newConfiguration0(parent, name);
  }

  public TMConfiguration newConfiguration0(Configuration parent, String name) throws TelusAPIException {
    try {
    	ConfigurationInfo info = provider.getConfigurationManagerNew().newConfiguration(parent.getId(), name);
        info.setParent0(((TMConfiguration)parent).getDelegate());
        return decorate((TMConfiguration)parent, info);
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return null;
  }

  public int removeProperties(Configuration configuration, boolean recursively) throws TelusAPIException {
    try {
    		return provider.getConfigurationManagerNew().removeProperties(configuration.getId(), recursively);
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return 0;
  }

  public int removeProperty(Configuration configuration, String name) throws TelusAPIException {
    return removeProperties(configuration, new String[]{name});
  }

  public int removeProperties(Configuration configuration, String[] name) throws TelusAPIException {
    try {
    	return provider.getConfigurationManagerNew().removeProperties(configuration.getId(), name);
    }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
    }
    return 0;
  }

  public void addProperty(Configuration configuration, String name, String value) throws TelusAPIException {
    addProperties(configuration, new String[]{name}, new String[]{value});
  }

  public void addProperties(Configuration configuration, String[] name, String[] value) throws TelusAPIException {
    try {
    	 provider.getConfigurationManagerNew().addProperties(configuration.getId(), name, value);
    } catch (Throwable e) {
    	provider.getExceptionHandler().handleException(e);
    }
  }

public long logActivation(Subscriber subscriber, String userID, long portalUserID, long transactionID) throws TelusAPIException{
    com.telus.eas.config1.info.ActivationLogInfo activationLogInfo = new com.telus.eas.config1.info.ActivationLogInfo();

    if (subscriber.getAccount().isPrepaidConsumer()){
     activationLogInfo.setActivationData("PREPAID");
    }else{
        activationLogInfo.setActivationData("POSTPAID");
    }

    if (subscriber.getStatus() == Subscriber.STATUS_ACTIVE){
       activationLogInfo.setStatusCD("S");
    }else{
       activationLogInfo.setStatusCD("I");
    }
    activationLogInfo.setCustomerID(subscriber.getAccount().getBanId());
    activationLogInfo.setClientID(portalUserID);
    activationLogInfo.setUserID(userID);
    try{
      if(transactionID == 0){
    	  transactionID = provider.getConfigurationManagerNew().getActivationLogID();
      }
      provider.getConfigurationManagerNew().logActivation(activationLogInfo, transactionID);
    }catch(Throwable e){
    	provider.getExceptionHandler().handleException(e);
    }
    return transactionID;
  }

  /*
  public void saveProperty(Configuration configuration, String[] name, String[] value) throws TelusAPIException {
    PropertyDAO[] dao = new PropertyDAO[name.length];

    for (int i=0; i<dao.length; i++) {
      PropertyDAO p = new PropertyDAO();



      dao[i] = p;
    }


  }
  */


  private TMConfiguration decoratePath(ConfigurationInfo[] info) {
    TMConfiguration parent = null;
    for(int i=0; i<info.length; i++) {
      //info.setParent0(...);  --each node already has its predecessor set as its parent
      TMConfiguration o = new TMConfiguration(this, parent, info[i], provider);
      parent = o;
    }

    return parent;
  }

  private TMConfiguration[] decorateChildren(TMConfiguration parent, ConfigurationInfo[] info) {
    TMConfiguration[] children = new TMConfiguration[info.length];
    for(int i=0; i<info.length; i++) {
      info[i].setParent0(parent.getDelegate());
      children[i] = new TMConfiguration(this, parent, info[i], provider);
    }

    return children;
  }

  private TMConfiguration decorate(TMConfiguration parent, ConfigurationInfo info) {
    info.setParent0(parent.getDelegate());
    return new TMConfiguration(this, parent, info, provider);
  }


}



