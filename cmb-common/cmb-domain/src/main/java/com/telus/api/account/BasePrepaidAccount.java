/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import java.util.*;

public interface BasePrepaidAccount extends Account{
   ConsumerName getName();


  boolean getBlockOutgoing976Numbers();

  void setBlockOutgoing976Numbers(boolean blockOutgoing976Numbers);

  
  Date getBirthDate();

  void setBirthDate(Date birthDate);

  /**
   * Creates a new unsaved PCS/Cellular subscriber.  No activation fee will be
   * charged when the new subscriber is activated.
   *
   * <P>This method may involve a remote method call.
   *
   */
  PCSSubscriber newPCSSubscriber() throws TelusAPIException;

  /**
   * Creates a new unsaved PCS/Cellular subscriber.
   *
   * <P>This method may involve a remote method call.
   *
   * @param activationFeeChargeCode the chargeCode to use when activating the
   *        newely created subscriber or <CODE>null</CODE> if the fee is waived.
   *
   */
  PCSSubscriber newPCSSubscriber(String activationFeeChargeCode) throws TelusAPIException;
}


