/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.config1;

import com.telus.api.*;
import com.telus.api.account.*;

/**
 * This class acts as a proxy to the class of the same name in the standard
 * Configuration, please see its documentation for more information .
 *
 */
public interface ConfigurationManager {

    Configuration lookup(String[] path) throws TelusAPIException, UnknownObjectException;

    Configuration lookup(String application) throws TelusAPIException, UnknownObjectException;

    Configuration lookup(Configuration parent, String[] path) throws TelusAPIException, UnknownObjectException;

    Configuration lookup(Configuration parent, String path) throws TelusAPIException, UnknownObjectException;

    long logActivation(Subscriber subscriber, String userID, long portalUserID, long transactionID) throws TelusAPIException;

}


