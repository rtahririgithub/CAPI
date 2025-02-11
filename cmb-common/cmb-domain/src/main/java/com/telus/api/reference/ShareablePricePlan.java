/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

public interface ShareablePricePlan extends PricePlan {

  /**
   * Returns maximum number of subscribers allowed on this plan or zero (0).
   *
   */
  int getMaximumSubscriberCount();

  /**
   * Returns the service added to the contract of subsequent subscribers
   * on this priceplan.
   *
   */
  String getSecondarySubscriberService();

}



