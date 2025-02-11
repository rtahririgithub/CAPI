/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface PricePlanChange extends InteractionDetail {
  String getNewPricePlanCode();
  String getOldPricePlanCode();
}


