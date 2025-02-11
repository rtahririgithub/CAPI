package com.telus.api.account;


public interface CancellationPenalty  {

  double getDepositAmount();
  double getDepositInterest();
  double getPenalty();
  String getSubscriberNumber();

}