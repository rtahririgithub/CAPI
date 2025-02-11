/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


/**
 * <CODE>PostpaidBusinessRegularAccount</CODE>
 *
 */
public interface PostpaidBusinessRegularAccount extends PostpaidAccount {


  /**
   * Returns the list of businesses matching this one from the credit bureau.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  BusinessCreditIdentity[] getBusinessCreditIdentities() throws TelusAPIException, CreditCheckNotRequiredException;

  /**
   * Performs a credit check on this business using its identity as provided
   * by the credit bureau.
   *
   * <P>This method may involve a remote method call.
   *
   * @see #getBusinessCreditIdentities
   * @see #getCreditInformation
   *
   */
  CreditCheckResult checkCredit(BusinessCreditIdentity identity) throws TelusAPIException, CreditCheckNotRequiredException;

  String getLegalBusinessName();

  void setLegalBusinessName(String legalBusinessName);

  String getTradeNameAttention();

  void setTradeNameAttention(String tradeNameAttention);

  /**
   * @deprecated use getContactName().getFirstName()
   * @see #getContactName
   */
  String getFirstName();

  /**
   * @deprecated use getContactName().getLastName()
   * @see #getContactName
   */
  String getLastName();

  /**
   *
   * @link aggregationByValue
   */
  BusinessCredit getCreditInformation();
}



