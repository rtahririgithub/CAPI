/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;

public interface StatusChangeHistory {

    Date getDate();

    String getActivityTypeCode();

    String getReasonCode();

    String getBanStatus();

}
