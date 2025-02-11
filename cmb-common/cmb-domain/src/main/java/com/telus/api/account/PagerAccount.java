/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;

@Deprecated
public interface PagerAccount {
  /**
   * Creates a new unsaved Pager subscriber.  No activation fee will be
   * charged when the new subscriber is activated.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serialNumber/ESN to be validated and attached to
   *        the new subscriber.
   *
   * @exception UnknownSerialNumberException the number does not exist in our datastore.
   * @exception SerialNumberInUseException the number is already being used by a subscriber.
   * @exception InvalidSerialNumberException the number is inappropriate for this
   *            operation.
   *
   */
  PagerSubscriber newPagerSubscriber(String serialNumber) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;

  /**
   * Creates a new unsaved Pager subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber the serialNumber/ESN to be validated and attached to
   *        the new subscriber.
   * @param activationFeeChargeCode the chargeCode to use when activating the
   *        newely created subscriber or <CODE>null</CODE> if the fee is waived.
   *
   * @exception UnknownSerialNumberException the number does not exist in our datastore.
   * @exception SerialNumberInUseException the number is already being used by a subscriber.
   * @exception InvalidSerialNumberException the number is inappropriate for this
   *            operation.
   *
   */
  PagerSubscriber newPagerSubscriber(String serialNumber, String activationFeeChargeCode) throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException;
}


