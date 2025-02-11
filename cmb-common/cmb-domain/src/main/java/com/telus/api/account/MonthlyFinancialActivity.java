/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;


/**
 * <CODE>MonthlyFinancialActivity</CODE>
 *
 */
public interface MonthlyFinancialActivity {
  int getYear();

  int getMonth();

  String getActivity();

  int getDishonoredPaymentCount();

  boolean isSuspended();

  boolean isCancelled();

}



