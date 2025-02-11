/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface PaymentMethodChange extends InteractionDetail {
  char getOldPaymentMethod();
  char getNewPaymentMethod();
}


