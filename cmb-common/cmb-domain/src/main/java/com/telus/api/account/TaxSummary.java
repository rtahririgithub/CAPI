/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface TaxSummary {
  double getPSTAmount();

  double getGSTAmount();

  double getHSTAmount();

  double getTotal();

}


