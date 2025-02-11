/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;


/**
 * <CODE>RatedFeature</CODE>
 **/
public interface RatedFeature extends Feature {

  double getRecurringCharge();
  int getRecurringChargeFrequency();
  double getUsageCharge();
  double getAdditionalCharge();
  boolean  isMinutePoolingContributor();
  int getCallingCircleSize();
  boolean isPrepaidCallingCircle();
  /**
   * Returns true when this is a manipulated feature created under a Prepaid service
   * @return boolean
   */
  boolean isWPS();
}




