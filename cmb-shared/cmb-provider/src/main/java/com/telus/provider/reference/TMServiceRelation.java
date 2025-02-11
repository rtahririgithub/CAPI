/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import java.sql.Date;

import com.telus.api.reference.*;
import com.telus.eas.utility.info.*;


public class TMServiceRelation implements ServiceRelation {
  /**
   * @link aggregation
   */
  private final ServiceRelationInfo delegate;
  private final ServiceSummary service;

  public TMServiceRelation(ServiceRelationInfo delegate, ServiceSummary service) {
    this.delegate = delegate;
    this.service = service;
  }

  public String getType() {
    return delegate.getType();
  }

  public ServiceSummary getService() {
    //return delegate.getService();
    return service;
  }

  public boolean isOptional() {
    return delegate.isOptional();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }
  
  public Date getExpirationDate()
  {
  	return delegate.getExpirationDate();	
  }


}



