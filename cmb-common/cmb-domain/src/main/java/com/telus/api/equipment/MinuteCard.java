/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.equipment;

import com.telus.api.*;
import com.telus.api.account.*;


public interface MinuteCard extends FeatureCard {

  /**
   * Returns the maximum number of minutes this card allows.
   *
   * @see #getProrationMinutes
   *
   */
  int getTotalMinutes();


  /**
   * Returns this card's total minutes prorated over a set of months.  The array will be time-ordered on the
   * expiryDate field.
   *
   * ProrationMinutes are calculated based on the customer's billing cycle, current date,
   * total-number-of-months and total-number-of-minutes.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @see #getTotalMinutes()
   * @see Account#getProrationMinutes
   *
   *
   * @link aggregationByValue
   *
   *
   */
  ProrationMinutes[] getProrationMinutes(Account account, int months) throws TelusAPIException;

}


