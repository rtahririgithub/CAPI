/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;

import java.util.*;

/**
 * <CODE>FinancialHistory</CODE>
 *
 */
public interface FinancialHistory {


  boolean isDelinquent();

  /**
   *
   * @link aggregationByValue
   */
  DebtSummary getDebtSummary();

  /**
   *
   * @link aggregationByValue
   */
  MonthlyFinancialActivity[] getMonthlyFinancialActivity();

  Date getLastPaymentDate();
  double getLastPaymentAmount();
  int getDishonoredPaymentCount();
  int getSuspensionCount();
  int getCancellationCount();
  boolean isWrittenOff();
  boolean isLastPaymentBackedout();
  boolean isLastPaymentFullyTransferred();
  boolean isLastPaymentSufficient();
  String getLastPaymentActivityCode();

  /**
   * @deprecated Use getCollectionState instead.
   * since May 29, 2006
   * @see #getCollectionState()
   */
  CollectionStep getCollectionStep();

  /**
   * @deprecated Use getCollectionState instead.
   * since May 29, 2006
   * @see #getCollectionState()
   */
  String getCollectionAgency();
  /**
   * @deprecated Use getCollectionState instead.
   * since May 29, 2006
   * @see #getCollectionState()
   */
  CollectionStep getNextCollectionStep();

  /**

   * To determine , if payment was refunded.

   *
  */

  boolean isLastPaymentRefunded();

  boolean isHotlined();
  Date getHotlinedDate();
  Date getDelinquentDate();
  Date getWrittenOffDate();
  CollectionState getCollectionState()throws TelusAPIException;

  /**
   * Returns CollectionHistory[], a remote call to back-end
   * @param from Date
   * @param to Date
   * @throws TelusAPIException
   * @return CollectionHistory[]
   */
  CollectionHistory[] getCollectionHistory(Date from, Date to) throws TelusAPIException;

}



