/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface PhoneNumberChange extends InteractionDetail {
    String getNewPhoneNumber();
    String getOldPhoneNumber();
}


