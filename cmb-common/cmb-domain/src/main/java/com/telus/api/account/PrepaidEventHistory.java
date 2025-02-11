/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;
import com.telus.api.reference.*;


/**
 * <b>TODO</b>: any modification requested should result in deprecating this class and having Prepaid web services handle the requested changes.  For futher details, 
 * refer to Client API Design.doc design document for Prepaid Real Time Rating project
 */
public interface PrepaidEventHistory {
	public static final String UNIT_TYPE_COUNT = "0";
	public static final String UNIT_TYPE_COST = "1";
	public static final String UNIT_TYPE_TIME = "2";
	public static final String UNIT_TYPE_VOLUME = "3";
	public static final String UNIT_TYPE_ITEM = "4";
	
  Date getEventDate();

  PrepaidEventType getEventType();

  String getDebitCreditFlag();

  double getAmount();

  String getCardId();

  String getCreditCardNumber();

  String getReferenceCode();

  double getStartBalance();

  double getEndBalance();

  String getUserID();

  String getSourceID();

  String getTransactionID();

  String getRelatedTransactionID();

  String getPrepaidAdjustmentReasonCode();
  
  double getOutstandingCharge();
  
  Date getTransactionDate();
  
  double getConfiscatedBalance();
  
  String getGMTOffset (); 
  
  String getPreEventStatus();
  String getPostEventStatus();
  double getPreEventAmount();
  double getPostEventAmount();
  String getUnitType();
  
  // -- Commented the Prepaid 5.1 rel changes  String getDiscountPercentage();
}



