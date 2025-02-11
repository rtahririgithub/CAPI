/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.task;

public interface Task {

  /**
   * Returns a <code>Throwable</code> if this task has completed
   * abnormally with an exception, otherwise <code>null</code>.
   */
  Throwable getException();

}



