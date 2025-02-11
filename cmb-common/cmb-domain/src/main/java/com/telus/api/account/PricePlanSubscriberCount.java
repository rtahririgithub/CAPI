/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface PricePlanSubscriberCount extends ServiceSubscriberCount {
  String getPricePlanCode();

  boolean isMaximumSubscriberReached();

  String[]  getFutureDatedSubscribers();

  ServiceSubscriberCount[] getServiceSubscriberCounts();
}



