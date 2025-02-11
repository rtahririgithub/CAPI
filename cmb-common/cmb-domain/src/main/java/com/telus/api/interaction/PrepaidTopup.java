/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface PrepaidTopup extends InteractionDetail {
  double getAmount();
  char getCardType();
  char getTopUpType();
}


