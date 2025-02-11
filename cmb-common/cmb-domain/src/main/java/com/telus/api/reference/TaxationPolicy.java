/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

public interface TaxationPolicy extends Reference {

  public static final char METHOD_TAX_ON_BASE = 'B';
  public static final char METHOD_PST_ON_GST  = 'T';

  String getProvince(); // also returned by getCode()

  double getGSTRate();
  double getPSTRate();
  double getHSTRate();

  double getMinimumPSTTaxableAmount();

  char getMethod();

}


