/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.config1;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Subscriber;
import com.telus.api.config1.Configuration;
import com.telus.api.config1.ConfigurationManager;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMConfigurationManager extends BaseProvider implements ConfigurationManager {

  private static final long serialVersionUID = 1L;
	
  public TMConfigurationManager(TMProvider provider) {
	super(provider);
  }

  public com.telus.api.config1.Configuration lookup(String[] path) throws TelusAPIException, UnknownObjectException {
	  
    try {
    	  	return provider.getConfigurationManagerNew().getConfiguration1(path);
    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t, new TelusExceptionTranslator() {
			
			public TelusAPIException translateException(Throwable throwable) {
				if (throwable instanceof UnknownObjectException) {
					UnknownObjectException unknownObjectException= (UnknownObjectException)throwable;
					return new UnknownObjectException(unknownObjectException.getMessage(), unknownObjectException, unknownObjectException.getName());
				}
				return null;
			}
		});
    }
	return null;
     
  }

  public com.telus.api.config1.Configuration lookup(String path) throws TelusAPIException, UnknownObjectException {
    try {
    	
      	  	return provider.getConfigurationManagerNew().getConfiguration1(path);
     }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t, new TelusExceptionTranslator() {
			
			public TelusAPIException translateException(Throwable throwable) {
				if (throwable instanceof UnknownObjectException) {
					UnknownObjectException unknownObjectException= (UnknownObjectException)throwable;
					return new UnknownObjectException(unknownObjectException.getMessage(), unknownObjectException, unknownObjectException.getName());
				}
				return null;
			}
		});
    }
	return null;
     
  }

  public Configuration lookup(Configuration parent, String[] path) throws TelusAPIException, UnknownObjectException {
    String[] parentPath = parent.getPath();

    String[] newPath = new String[parentPath.length + path.length];

    System.arraycopy(parentPath, 0, newPath, 0, parentPath.length);
    System.arraycopy(path, 0, newPath, parentPath.length, path.length);

    return lookup(newPath);
  }

  public Configuration lookup(Configuration parent, String path) throws TelusAPIException, UnknownObjectException {
    return lookup(parent, new String[]{path});
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
       
      }catch (Throwable t) {
  		provider.getExceptionHandler().handleException(t);
      }
      return transactionID;
    }

}



