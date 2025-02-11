/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

public class CancellationPenaltyInfo  extends Info implements CancellationPenalty {

   static final long serialVersionUID = 1L;

  private double depositAmount;
  private double depositInterest;
  private double penalty;
  private String subscriberNumber;

  public double getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(double depositAmount) {
    this.depositAmount = depositAmount;
  }

  public double getDepositInterest() {
    return depositInterest;
  }

  public void setDepositInterest(double depositInterest) {
    this.depositInterest = depositInterest;
  }

  public double getPenalty() {
    return penalty;
  }

  public void setPenalty(double penalty) {
    this.penalty = penalty;
  }
   
  public String getSubscriberNumber() {
	return subscriberNumber;
  }

  public void setSubscriberNumber(String subscriberNumber) {
	this.subscriberNumber = subscriberNumber;
  }

public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CancellationPenaltyInfo:{\n");
    s.append("    depositAmount=[").append(depositAmount).append("]\n");
    s.append("    depositInterest=[").append(depositInterest).append("]\n");
    s.append("    penalty=[").append(penalty).append("]\n");
    s.append("    subscriberNumber=[").append(subscriberNumber).append("]\n");
    s.append("}");

    return s.toString();
  }
}



