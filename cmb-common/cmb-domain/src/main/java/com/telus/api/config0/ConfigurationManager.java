/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.config0;

import com.telus.api.*;
import com.telus.api.account.Subscriber;

public interface ConfigurationManager
{
    Configuration getApplications() throws TelusAPIException;
    Configuration getConfiguration(String[] path) throws TelusAPIException, UnknownObjectException;
    long logActivation(Subscriber subscriber, String userID, long portalUserID, long transactionID) throws TelusAPIException;
}


