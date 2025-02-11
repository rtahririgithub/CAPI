package com.telus.api.account;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jul-2006
 */

public interface ActivationOption {
// ActivationOption getInstance(ActivationOptionType optionType,
  //		double deposit, double creditLimit, String optionMessageKey);
  ActivationOptionType getOptionType();
  double getDeposit();
  double getCreditLimit();
  String getCreditClass();
  
  /**
   * @return maximum contract term length allowed (in months).
   */
  int getMaxContractTerm();
  
  double getCLPPricePlanLimitAmount();
}
