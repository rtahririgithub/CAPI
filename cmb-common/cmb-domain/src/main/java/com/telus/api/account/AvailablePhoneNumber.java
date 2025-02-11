/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.reference.NumberGroup;

public interface AvailablePhoneNumber {
  String getPhoneNumber();

  String getNumberLocationCode();

  NumberGroup getNumberGroup();
}


