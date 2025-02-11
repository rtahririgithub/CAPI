package com.telus.api.account;

/**
 * <CODE>Cheque</CODE>
 *
 */
public interface Cheque {

  String getChequeNumber();

  void setChequeNumber(String chequeNumber);

  /**
   *
   * @link aggregatione
   */
  BankAccount getBankAccount();

  /**
   * Set Bank Account
   * @param bankAccount BankAccount
   */
  void setBankAccount(BankAccount bankAccount);

}


