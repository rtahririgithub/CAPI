/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


public class InvalidNetworkException extends TelusAPIException
{
    private int networkId;

    public InvalidNetworkException(String message, Throwable exception, int networkId)
    {
        super(message, exception);
        this.networkId = networkId;
    }

    public InvalidNetworkException(Throwable exception, int networkId)
    {
        super(exception);
        this.networkId = networkId;
    }

    public InvalidNetworkException(String message, int networkId)
    {
        super(message);
        this.networkId = networkId;
    }

    public int getNetworkId()
    {
        return networkId;
    }
}



