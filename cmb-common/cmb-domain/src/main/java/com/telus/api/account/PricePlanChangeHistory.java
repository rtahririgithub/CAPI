/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.reference.*;
import java.util.*;


public interface PricePlanChangeHistory {


  /**
   * The effective date for this change.
   *
   */
  Date getDate();

  String getNewPricePlanCode();

  /**
   * @return null
   * @deprecated This information is not populated, always return null, shall not be used, will be removed in the 
   * future release
   * 
   */
  String getOldPricePlanCode();

  /**
   * Returns the new PricePlan associated with this change.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   */
  PricePlanSummary getNewPricePlan() throws TelusAPIException;

  /**
   * Returns the old PricePlan associated with this change.
   *
   * <P>The returned object may be <CODE>null</CODE> if getOldPricePlanCode() returns <CODE>null</CODE>
   *
   * <P>This method may involve a remote method call.
   *
   * @deprecated This method shall not be used, will be removed in the future release
   */
  PricePlanSummary getOldPricePlan() throws TelusAPIException;

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
