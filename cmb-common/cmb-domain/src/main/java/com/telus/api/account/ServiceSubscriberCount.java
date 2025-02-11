/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface ServiceSubscriberCount {
  String getServiceCode();

  String[]  getActiveSubscribers();
  String[]  getReservedSubscribers();
  String[]  getCanceledSubscribers();
  String[]  getSuspendedSubscribers();

  String[]  getActiveAndReservedSubscribers();

}



