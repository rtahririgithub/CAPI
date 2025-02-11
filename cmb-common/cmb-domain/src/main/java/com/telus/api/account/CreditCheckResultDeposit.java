package com.telus.api.account;



/**
 * <CODE>CreditCheckResult</CODE>
 *
 */
public interface CreditCheckResultDeposit {

  String getProductType();

  double getDeposit();
  
  void setDeposit(double deposit);

}


