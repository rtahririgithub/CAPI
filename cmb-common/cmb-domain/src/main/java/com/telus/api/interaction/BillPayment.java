/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface BillPayment extends InteractionDetail {

    char getPaymentMethod();

    double getPaymentAmount();

}


