package com.telus.api.account;

public interface ActivationTopUp {
  double getAmount();
  void setAmount(double amount);
  double getRate();
  void setRate(double rate);
  int getExpiryDays();
  void setExpiryDays(int days);  
  String getReasonCode();  
  void setReasonCode( String reasonCode );
}
