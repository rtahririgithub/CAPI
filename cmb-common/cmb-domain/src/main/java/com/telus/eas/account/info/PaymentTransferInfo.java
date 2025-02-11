package com.telus.eas.account.info;

import com.telus.eas.framework.info.*;
import com.telus.api.account.PaymentTransfer;

/**
 * @author Roman Tov
 * @version 1.0, 27-Jun-2006
 */

public class PaymentTransferInfo extends Info implements PaymentTransfer {

  private int TargetBanId;
  private String Reason;
  private double Amount;

  public int getTargetBanId() {
    return TargetBanId;
  }

  public String getReason() {
    return Reason;
  }

  public double getAmount() {
    return Amount;
  }

   public PaymentTransferInfo(int targetBanId , String reason, double amount) {

   TargetBanId = targetBanId;
   Reason = reason;
   Amount = amount;
  }
}
