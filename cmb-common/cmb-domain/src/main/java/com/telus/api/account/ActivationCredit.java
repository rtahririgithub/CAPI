/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.reference.*;
import com.telus.api.*;

import java.util.*;

public interface ActivationCredit extends Credit, Reference {

  public final static String CREDIT_TYPE_CONTRACT_TERM  = "TERM";
  public final static String CREDIT_TYPE_NEW_ACTIVATION = "ACT";
  public final static String CREDIT_TYPE_PROMOTION      = "PROMO";
  public final static String CREDIT_TYPE_PRICE_PLAN     = "PPLAN";
  
  @Deprecated
  public final static String CREDIT_TYPE_FIDO           = "FIDO";

  String getBarCode();

  int getContractTerm();

  Date getExpiryDate();

  boolean isContractTermCredit();

  boolean isNewActivationCredit();

  boolean isPromotionCredit();

  boolean isPricePlanCredit();
  
  @Deprecated
  boolean isFidoCredit();

  void setBarCode(String barCode);

  void setContractTerm(int contractTerm);

  void setExpiryDate(Date expiryDate);


  void apply() throws TelusAPIException;
  void apply(int ban) throws TelusAPIException;
  void apply(int ban, String subscriberId) throws TelusAPIException;

  void memo() throws TelusAPIException;
  void memo(int ban) throws TelusAPIException;
  void memo(int ban, String subscriberId) throws TelusAPIException;
}
