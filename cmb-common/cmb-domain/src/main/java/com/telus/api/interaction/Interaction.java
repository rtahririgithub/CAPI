/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;

import com.telus.api.*;
import java.util.*;

public interface Interaction {

  /**
   *
   * Returns the details of this interaction.
   *
   * <P>The returned objects will be specific types of InteractionDetail (i.e. PricePlanChange,
   * BillPayment, etc.).  Use the getType() method to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   * @see #getType
   *
   * @link aggregation
   *
   */
  InteractionDetail[] getDetails() throws TelusAPIException;

  /**
   * Returns one of the InteractionManager.TYPE_xxx constants.
   *
   * @see InteractionManager
   */
  String getType();

  long getId();

  Date getDatetime();

  /**
   * Returns the subscriber associated with this interaction or <CODE>null</CODE>.
   *
   */
  String getSubscriberId();

  int getBan();

  String getApplicationId();

  Integer getOperatorId();

  String getDealerCode();

  String getSalesrepCode();

}




