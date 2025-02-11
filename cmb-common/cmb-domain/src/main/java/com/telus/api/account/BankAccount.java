package com.telus.api.account;



/**
 * <CODE>BankAccount</CODE>
 *
 */
public interface BankAccount {

  String BANK_ACCOUNT_TYPE_INDIVIDUAL = "I";
  String BANK_ACCOUNT_TYPE_BUSINESS = "B";

  String getBankCode();

  void setBankCode(String bankCode);

  String getBankAccountNumber();

  void setBankAccountNumber(String bankAccountNumber);

  String getBankBranchNumber();

  void setBankBranchNumber(String bankBranchNumber);

  String getBankAccountHolder();

  void setBankAccountHolder(String bankAccountHolder);

  String getBankAccountType();

  void setBankAccountType(String bankAccountType);

}


