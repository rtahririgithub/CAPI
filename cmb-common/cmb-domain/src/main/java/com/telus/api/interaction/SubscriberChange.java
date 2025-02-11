/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface SubscriberChange extends InteractionDetail {

  String getNewLastName();
  String getNewMiddleInitial();
  String getNewFirstName();
  String getNewEmailAddress();

  String getOldLastName();
  String getOldMiddleInitial();
  String getOldFirstName();
  String getOldEmailAddress();
}


