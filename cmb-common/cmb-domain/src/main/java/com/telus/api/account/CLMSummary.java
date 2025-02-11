package com.telus.api.account;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jul-2006
 */

public interface CLMSummary {

  double getUnpaidBillCharges();
  double getUnpaidAirTime();
  double getUnpaidData();
  /*Roman: commented out
  double getTotalPendingCharges();
  double getTotalPendingAdjustments();
  */
  double getRequiredMinimumPayment();  double getUnpaidUnBilledAmount();

}
