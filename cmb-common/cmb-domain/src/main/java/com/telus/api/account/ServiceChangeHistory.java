/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.reference.*;
import java.util.*;

public interface ServiceChangeHistory {
  /**
   * The effective date for this change.
   *
   */
  Date getDate();
 
  String getServiceCode();

  /**
   * Returns the service associated with this change.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   */
  ServiceSummary getService() throws TelusAPIException;

  Date getNewExpirationDate();
  
  /**
   * Returns the Knowbility Operator ID.
   *
   */
  String getKnowbilityOperatorID();
  
  /**
   * Returns the Application ID.
   *
   */
  String getApplicationID();
  
  /**
   * Returns the Knowbility Dealer Code.
   *
   */

  String getDealerCode();
  
  /**
   * Returns the Knowbility Sales Rep Id.
   *
   */
  
  String getSalesRepId();

}
