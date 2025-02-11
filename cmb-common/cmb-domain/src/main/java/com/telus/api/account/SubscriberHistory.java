/*

 * $Id$

 * %E% %W%

 * Copyright (c) TELUS Mobility. All Rights Reserved.

 */

package com.telus.api.account;

import java.util.*;


public interface SubscriberHistory {

  Date getDate();
  char getStatus();
  String getActivityCode();
  String getActivityReasonCode();
  int getPreviousBanId();
  int getNextBanId();
  int getBrandId();

}

