/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface RoleChange extends InteractionDetail {
    String getNewRole();
    String getOldRole();
}


