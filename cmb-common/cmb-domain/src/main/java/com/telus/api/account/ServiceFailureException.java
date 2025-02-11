/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;

//import com.telus.eas.framework.exception.*;

/**
 * <CODE>InvalidCreditCardException</CODE>
 *
 */
public class ServiceFailureException extends TelusAPIException {

  public static final int SERVICE_UNKNOWN                 = 0;
  public static final int SERVICE_KNOWBILITY              = 1;
  public static final int SERVICE_PREPAID_ACCOUNT_MANAGER = 2;
  public static final int SERVICE_CRISK                   = 3;
  public static final int SERVICE_BIH                     = 4;
  public static final int SERVICE_CODE1                   = 5;  // TODO:  --peter frei

  /*
  public static final void check(TelusException e) throws ServiceFailureException {
    if("APP10007".equals(e.id)) {
      throw new ServiceFailureException(e, "Knowbility", SERVICE_KNOWBILITY);
    /*
    } else if("APP10008".equals(e.id)) {
      throw new ServiceFailureException(e, "BIH");
    * /
    } else if("SYS00009".equals(e.id)) {
      throw new ServiceFailureException(e, "PrepaidAccountManager", SERVICE_PREPAID_ACCOUNT_MANAGER);
    } else if("SYS00013".equals(e.id)) {
      throw new ServiceFailureException(e, "credit check application (CRISK)", SERVICE_CRISK);
    } else if("SYS00014".equals(e.id) || "APP10008".equals(e.id)) {
      throw new ServiceFailureException(e, "credit card transaction application (BIH)", SERVICE_BIH);
    }
  }
  */

  /**
   * @link aggregation
   */
  private final String serviceName;
  private final int service;

  public ServiceFailureException(String message, Throwable exception, String serviceName, int service) {
    super(((message==null)?"":message + ": ") + "service=[" + serviceName + "]", exception);
    this.serviceName = serviceName;
    this.service = service;
  }

  public ServiceFailureException(Throwable exception, String serviceName, int service) {
    this("Service Unavailable", exception, serviceName, service);
  }

  public ServiceFailureException(String message, String serviceName, int service) {
    this(message, null, serviceName, service);
  }

  public String getServiceName() {
    return serviceName;
  }

  public int getService() {
    return service;
  }

}


