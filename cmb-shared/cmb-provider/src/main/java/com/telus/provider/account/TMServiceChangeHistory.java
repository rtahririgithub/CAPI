/*

 * $Id$

 * %E% %W%

 * Copyright (c) Telus Mobility Inc. All Rights Reserved.

 */



package com.telus.provider.account;



import com.telus.api.*;

import com.telus.api.account.*;

import com.telus.api.reference.*;

import java.util.*;

import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.eas.subscriber.info.*;




public class TMServiceChangeHistory extends BaseProvider implements ServiceChangeHistory {

  /**

   * @link aggregation

   */

  private final ServiceChangeHistoryInfo delegate;

  private ServiceSummary service;



  public TMServiceChangeHistory(TMProvider provider, ServiceChangeHistoryInfo delegate) {

    super(provider);

    this.delegate = delegate;

  }



  //--------------------------------------------------------------------

  //  Decorative Methods

  //--------------------------------------------------------------------

  public Date getDate() {

    return delegate.getDate();

  }



  public String getServiceCode() {

    return delegate.getServiceCode();

  }



  public Date getNewExpirationDate() {

    return delegate.getNewExpirationDate();

  }
  
  public String getKnowbilityOperatorID() {

    return delegate.getKnowbilityOperatorID();

  }
  
    
  
  public String getApplicationID() {

    return delegate.getApplicationID();

  }
  
  public String getDealerCode() {

    return delegate.getDealerCode();

  }
  
  public String getSalesRepId() {

    return delegate.getSalesRepId();

  }
  


  //--------------------------------------------------------------------

  //  Service Methods

  //--------------------------------------------------------------------

  public ServiceSummary getService() throws TelusAPIException {

    if(service == null) {

      service = provider.getReferenceDataManager().getRegularService(getServiceCode());

    }

    return service;

  }

}







