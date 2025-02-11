/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.task;

import com.telus.api.task.*;
import com.telus.provider.util.*;

public abstract class TMTask implements Runnable, Task {

  private final TMCondition finished = new TMCondition();
  private Throwable exception;


  public TMTask() {
  }

  /**
   * Waits for this task to complete, normally or with exception.
   *
   * @exception  InterruptedException if another thread has interrupted
   *             the current thread. The <i>interrupted status</i> of the
   *             current thread is cleared when this exception is thrown.
   */
  public final void waitUntilFinished() throws InterruptedException {
    finished.waitForTrue();
  }

  /**
   * Returns <code>true</code> if this task has completed,
   * normally or with exception.
   */
  public boolean isFinished() {
    return finished.isTrue();
  }

  public Throwable getException() {
    return exception;
  }

  public final void run() {
    try {
      runImpl();
    } catch (Throwable e) {
      exception = e;
    } finally {
      finished.setTrue();
    }
  }

  /**
   * Returns <CODE>true</CODE> if this task has something to do when run.
   *
   */
  public boolean hasWork() {
    return true;
  }

  /**
   * Sub-classes should implement the task's code here.
   *
   */
  protected abstract void runImpl() throws Throwable;

}




