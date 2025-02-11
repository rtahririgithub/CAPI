/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;

public class InvalidBillCycleChangeException extends TelusAPIException
{
    public InvalidBillCycleChangeException(String message, Throwable exception)
    {
        super(message, exception);
    }

    public InvalidBillCycleChangeException(String message)
    {
        super(message);
    }

    public InvalidBillCycleChangeException(Throwable exception)
    {
        super(exception);
    }
}



