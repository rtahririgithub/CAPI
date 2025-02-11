/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.fleet.*;

public class InvalidFleetException extends TelusAPIException
{
    private FleetIdentity fleetIdentity;

    public InvalidFleetException(String message, Throwable exception, FleetIdentity fleetIdentity)
    {
        super(message, exception);
        this.fleetIdentity = fleetIdentity;
    }

    public InvalidFleetException(Throwable exception, FleetIdentity fleetIdentity)
    {
        super(exception);
        this.fleetIdentity = fleetIdentity;
    }

    public InvalidFleetException(String message, FleetIdentity fleetIdentity)
    {
        super(message);
        this.fleetIdentity = fleetIdentity;
    }

    public FleetIdentity getFleetIdentity()
    {
        return fleetIdentity;
    }
}



