/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

public interface ServiceChange extends InteractionDetail {

  public static final char ACTION_ADD       = 'I';
  public static final char ACTION_DELETE    = 'R';
  public static final char ACTION_UPDATE    = 'U';
//  public static final char ACTION_NO_CHANGE = 'N';


  String getServiceCode();

  /**
   * Returns one of the ACTION_xxx constants.
   *
   */
  char getAction();

}


