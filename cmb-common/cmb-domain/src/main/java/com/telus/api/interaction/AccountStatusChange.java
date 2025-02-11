/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface AccountStatusChange extends InteractionDetail {
  char getStatusFlag();
  char getOldStatus();
  char getOldHotlinedInd();
  char getNewStatus();
  char getNewHotlinedInd();
}


