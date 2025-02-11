/*
 * Copyright (c) Telus Mobility 2005  All Rights Reserved.
 *
 */

package com.telus.api.account;

import java.util.*;

/**
 * @author      Carlos Manjarres
 * @version     1.0
 * @since       May-2005
 */
public interface ResourceChangeHistory {

  /**
   *
   * Return the type of resource. Posible values are Susbcriber.RESOURCE_TYPE_* such as 'N','H','X',...
   * @see com.telus.api.account.Subscriber
   */
  String getType();

  /**
   *
   * Return the value of the resource such as phone number, mike number (urban*fleet*memberId ) or IP address
   */
  String getValue();

  /**
   *
   * Return the status which could be Subscriber.RESOURCE_STATUS_*
   * @see com.telus.api.account.Subscriber
   */
  String getStatus();

  /**
   *
   * Return Date and time of the status update.
   */
  Date getStatusDate();

  /**
   * Returns the Knowbility Operator ID.
   *
   */
  String getKnowbilityOperatorID();

  /**
   * Returns the Application ID.
   *
   */
  String getApplicationID();


}
