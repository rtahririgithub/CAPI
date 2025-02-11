/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.task;

import com.telus.api.account.*;
import com.telus.api.reference.*;

public interface ContractChangeTask extends Task {

  Subscriber getSubscriber();

  Contract getContract();

  Service[] getServicesToAdd();

  Service[] getServicesToDelete();

}




