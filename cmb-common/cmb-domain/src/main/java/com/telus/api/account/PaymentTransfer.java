package com.telus.api.account;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jun-2006
 */

public interface PaymentTransfer {

  int getTargetBanId();
  String getReason();
  double getAmount();
}
