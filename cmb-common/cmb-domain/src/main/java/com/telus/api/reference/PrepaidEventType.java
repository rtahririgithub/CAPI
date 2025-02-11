/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;


public interface PrepaidEventType extends Reference {
  boolean isClientVisible();
  boolean isTopUpEvent();
  boolean isBalanceAffectingTopUpEvent();
}



