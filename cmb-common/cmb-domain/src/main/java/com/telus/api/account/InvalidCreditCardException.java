/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.TelusAPIException;
import com.telus.eas.framework.exception.PolicyFaultInfo;


/**
 * <CODE>InvalidCreditCardException</CODE>
 *
 */
public class InvalidCreditCardException extends TelusAPIException {

/*
  public static final int REFER_TO_CARD_ISSUER                            =  1;
  public static final int REFER_TO_CARD_ISSUER_SPECIAL_CONDITION          =  2;
  public static final int INVALID_MERCHANT                                =  3;
  public static final int PICKUP_CARD                                     =  4;
  public static final int DO_NOT_HONOUR                                   =  5;
  public static final int ERROR_TRANSACTION_FAILED_EDIT_CHECK             =  6;
  public static final int PICK_UP_CARD_SPECIAL_CONDITION                  =  7;
  public static final int HONOUR_WITH_IDENTIFICATION                      =  8;
  public static final int INVALID_TRANSACTION                             =  9;
  public static final int INVALID_AMOUNT                                  =  10;
  public static final int INVALID_CARD_NUMBER_NO_SUCH_NUMBER              =  11;
  public static final int APPROVED_UPDATE_TRACK_III                       =  12;
  public static final int FI_ERROR_REENTER_TRANSACTION                    =  13;
  public static final int FILE_UPDATE_NOT_SUPPORTED_BY_RECEIVER           =  14;
  public static final int UNABLE_TO_LOCATE_RECORD_ON_FILE                 =  15;
  public static final int DUPLICATE_FILE_UPDATE_RECORD                    =  16;
  public static final int FILE_UPDATE_FIELD_EDIT_ERROR                    =  17;
  public static final int FILE_UPDATE_NOT_SUCCESSFUL                      =  18;
  public static final int FORMAT_ERROR                                    =  19;
  //public static final int EXPIRED_CARD                                    =  20;
  //public static final int RESTRICTED_CARD                                 =  21;
  public static final int PIN_RETRIES_EXCEEDED                            =  22;
  public static final int NO_CREDIT_AMOUNT                                =  23;
  public static final int REQUESTED_FUNCTION_NOT_SUPPORTED                =  24;
  public static final int LOST_CARD                                       =  25;
  public static final int STOLEN_CARD                                     =  26;
  public static final int NOT_SUFFICIENT_FUNDS                            =  27;
  public static final int NO_CHEQUING_ACCOUNT                             =  28;
  public static final int NO_SAVINGS_ACCOUNT                              =  29;
  public static final int EXPIRED_CARD                                    =  30;
  public static final int INCORRECT_PIN                                   =  31;
  public static final int NO_CARD_RECORD                                  =  32;
  public static final int TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER         =  33;
  public static final int TRANSACTION_NOT_PERMITTED_TO_TERMINAL           =  34;
  public static final int SUSPECTED_FRAUD                                 =  35;
  public static final int CARD_ACCEPTOR_CONTRACT_ACCEPTOR                 =  36;
  public static final int EXCEEDS_WITHDRAWAL_LIMITS                       =  37;
  public static final int RESTRICTED_CARD                                 =  38;
  public static final int SECURITY_VIOLATION_MAC                          =  39;
  public static final int ORIGINAL_AMOUNT_INCORRECT                       =  40;
  public static final int EXCEEDS_WITHDRAWAL_COUNT_LIMIT                  =  41;
  public static final int RESPONSE_RECEIVED_TOO_LATE                      =  42;
  public static final int ALLOWABLE_NUMBER_OF_PIN_TRIES_EXCEEDED          =  43;
  public static final int MERCHANT_WITHDRAWALS_EXCEED_LIMIT               =  44;
  public static final int INVALID_BUSINESS_DATE                           =  45;
  public static final int KEY_EXCHANGE_VALIDATION_FAILED                  =  46;
  public static final int INVALID_PIN_BLOCK                               =  47;
  public static final int PIN_LENGTH_INVALID                              =  48;
  public static final int NO_KEYS_AVAILIABLE_FOR_USE                      =  49;
  public static final int KME_KEY_SYNC_ERROR                              =  50;
  public static final int PIN_KEY_SYNC_ERROR                              =  51;
  public static final int MAC_KEY_SYNC_ERROR                              =  52;
  public static final int CUTOFF_IN_PROGRESS                              =  53;
  public static final int ISSUER_OR_SWITCH_IS_INOPERATIVE                 =  54;
  public static final int FI_NETWORK_NOT_FOUND                            =  55;
  public static final int DUPLICATED_TRANSMISSION                         =  56;
  public static final int SYSTEM_MALFUNCTION                              =  57;
  public static final int UNSUPPORTED_CARD_TYPE                           =  58;

*/
  /**
   * @link aggregation
   */
  private final CreditCard creditCard;
  private final String reason;
  private final String reasonText;
  private String reasonTextFrench;
  private PolicyFaultInfo policyFaultInfo;


 public InvalidCreditCardException(Throwable e, String reason, CreditCard creditCard, String message, String messageFR){
     super(e);
     this.reason = reason;
     this.creditCard = creditCard;
     this.reasonText = message;
     this.reasonTextFrench = messageFR;
 }
 
 public InvalidCreditCardException(Throwable e, String reason, CreditCard creditCard, String message, String messageFR, PolicyFaultInfo policyFaultInfo){
     this(e, reason, creditCard, message, messageFR);
     this.policyFaultInfo = policyFaultInfo;
 }
/*

  public InvalidCreditCardException(int reason, Throwable exception, CreditCard creditCard) {
    super(getReasonText(reason), exception);
    this.creditCard = creditCard;
    this.reason     = reason;
    this.reasonText = getReasonText(reason);
    reasonTextFrench = exception.getMessage();
  }

  public InvalidCreditCardException(String message, Throwable exception, CreditCard creditCard) {
    super(message, exception);
    this.creditCard = creditCard;
    this.reasonText = parseReason(exception);
    this.reason     = getReason(this.reasonText);
    reasonTextFrench = exception.getMessage();
  }

  public InvalidCreditCardException(Throwable exception, CreditCard creditCard) {
    super(exception);
    this.creditCard = creditCard;
    this.reasonText = parseReason(exception);
    this.reason     = getReason(this.reasonText);
    reasonTextFrench = exception.getMessage();
  }

*/
  /*
  public InvalidCreditCardException(String message, CreditCard creditCard) {
    super(message);
    this.creditCard = creditCard;
  }
  */

  public CreditCard getCreditCard() {
    return creditCard;
  }

  /**
   * Returns one of the constants on this class (i.e. DUPLICATED_TRANSMISSION).
   *
   */
  public String getReason() {
    return reason;
  }

  public String getReasonText() {
    return reasonText;
  }


  public String getReasonTextFrench() {
    return reasonTextFrench;
  }
  
  public PolicyFaultInfo getPolicyFaultInfo() {
	  return policyFaultInfo;
  }

/*
  //private static final String REASON_START_TOKEN = "(ResponseMsg = ";
  //private static final String REASON_END_TOKEN   = ")";

  //private static String parseReason(Throwable exception) {
  //public static String parseReason(Throwable exception) {

    try {
      String message  = exception.getMessage();

      if(message == null) {
        return "";
      }

      int start = message.indexOf(REASON_START_TOKEN) + REASON_START_TOKEN.length();
      int end   = message.lastIndexOf(REASON_END_TOKEN);

      return message.substring(start, end);
    } catch (Throwable e) {
      System.err.println(e);
      e.printStackTrace(System.err);
      return "";
    }
  }

  private static final Map map = new HashMap();
  public static int getReason(String reasonText) {
    try {
      Integer i = (Integer)map.get(reasonText);
      return i.intValue();
    } catch (Throwable e) {
      System.err.println(e);
      e.printStackTrace(System.err);
      return 0;
    }
  }

  public static String getReasonText(int reason) {
    try {
      String[] keys = (String[])map.keySet().toArray(new String[map.size()]);
      for (int i=0; i<keys.length; i++) {
        Integer j = (Integer)map.get(keys[i]);
        if(reason == j.intValue()){
          return keys[i];
        }
      }
    } catch (Throwable e) {
      System.err.println(e);
      e.printStackTrace(System.err);
    }
    return "";
  }



  static {
    map.put("REFER TO CARD ISSUER", new Integer(REFER_TO_CARD_ISSUER));
    map.put("REFER TO CARD ISSUER (SPECIAL CONDITION", new Integer(REFER_TO_CARD_ISSUER_SPECIAL_CONDITION));
    map.put("INVALID MERCHANT", new Integer(INVALID_MERCHANT));
    map.put("PICK-UP CARD", new Integer(PICKUP_CARD));
    map.put("DO NOT HONOUR", new Integer(DO_NOT_HONOUR));
    map.put("ERROR, TRANSACTION FAILED EDIT CHECK", new Integer(ERROR_TRANSACTION_FAILED_EDIT_CHECK));
    map.put("PICK UP CARD (SPECIAP CONDITION", new Integer(PICK_UP_CARD_SPECIAL_CONDITION));
    map.put("HONOUR WITH IDENTIFICATION", new Integer(HONOUR_WITH_IDENTIFICATION));
    map.put("INVALID TRANSACTION", new Integer(INVALID_TRANSACTION));
    map.put("INVALID AMOUNT", new Integer(INVALID_AMOUNT));
    map.put("INVALID CARD NUMBER (NO SUCH NUMBER)", new Integer(INVALID_CARD_NUMBER_NO_SUCH_NUMBER));
    map.put("APPROVED, UPDATE TRACK III", new Integer(APPROVED_UPDATE_TRACK_III));
    map.put("FI ERROR (RE-ENTER TRANSACTION)", new Integer(FI_ERROR_REENTER_TRANSACTION));
    map.put("FILE UPDATE NOT SUPPORTED BY RECEIVER", new Integer(FILE_UPDATE_NOT_SUPPORTED_BY_RECEIVER));
    map.put("UNABLE TO LOCATE RECORD ON FILE", new Integer(UNABLE_TO_LOCATE_RECORD_ON_FILE));
    map.put("DUPLICATE FILE UPDATE RECORD", new Integer(DUPLICATE_FILE_UPDATE_RECORD));
    map.put("FILE UPDATE FIELD EDIT ERROR", new Integer(FILE_UPDATE_FIELD_EDIT_ERROR));
    map.put("FILE UPDATE NOT SUCCESSFUL", new Integer(FILE_UPDATE_NOT_SUCCESSFUL));
    map.put("FORMAT ERROR", new Integer(FORMAT_ERROR));
    //map.put("EXPIRED CARD (PICK-UP)", new Integer(EXPIRED_CARD));
    //map.put("RESTRICTED CARD (PICK-UP)", new Integer(RESTRICTED_CARD));
    map.put("PIN RETRIES EXCEEDED (PICK UP)", new Integer(PIN_RETRIES_EXCEEDED));
    map.put("NO CREDIT AMOUNT", new Integer(NO_CREDIT_AMOUNT));
    map.put("REQUESTED FUNCTION NOT SUPPORTED", new Integer(REQUESTED_FUNCTION_NOT_SUPPORTED));
    map.put("LOST CARD (PICK UP)", new Integer(LOST_CARD));
    map.put("STOLEN CARD (PICK UP)", new Integer(STOLEN_CARD));
    map.put("NOT SUFFICIENT FUNDS", new Integer(NOT_SUFFICIENT_FUNDS));
    map.put("NO CHEQUING ACCOUNT", new Integer(NO_CHEQUING_ACCOUNT));
    map.put("NO SAVINGS ACCOUNT", new Integer(NO_SAVINGS_ACCOUNT));
    map.put("EXPIRED CARD", new Integer(EXPIRED_CARD));
    map.put("INCORRECT PIN", new Integer(INCORRECT_PIN));
    map.put("NO CARD RECORD", new Integer(NO_CARD_RECORD));
    map.put("TRANSACTION NOT PERMITTED TO CARDHOLDER/ISSUER", new Integer(TRANSACTION_NOT_PERMITTED_TO_CARDHOLDER));
    map.put("TRANSACTION NOT PERMITTED TO TERMINAL/ACCEPTOR", new Integer(TRANSACTION_NOT_PERMITTED_TO_TERMINAL));
    map.put("SUSPECTED FRAUD", new Integer(SUSPECTED_FRAUD));
    map.put("CARD ACCEPTOR CONTRACT ACCEPTOR", new Integer(CARD_ACCEPTOR_CONTRACT_ACCEPTOR));
    map.put("EXCEEDS WITHDRAWAL LIMITS", new Integer(EXCEEDS_WITHDRAWAL_LIMITS));
    map.put("RESTRICTED CARD", new Integer(RESTRICTED_CARD));
    map.put("SECURITY VIOLATION (MAC)", new Integer(SECURITY_VIOLATION_MAC));
    map.put("ORIGINAL AMOUNT INCORRECT", new Integer(ORIGINAL_AMOUNT_INCORRECT));
    map.put("EXCEEDS WITHDRAWAL COUNT LIMIT", new Integer(EXCEEDS_WITHDRAWAL_COUNT_LIMIT));
    map.put("RESPONSE RECEIVED TOO LATE", new Integer(RESPONSE_RECEIVED_TOO_LATE));
    map.put("ALLOWABLE NUMBER OF PIN TRIES EXCEEDED", new Integer(ALLOWABLE_NUMBER_OF_PIN_TRIES_EXCEEDED));
    map.put("MERCHANT WITHDRAWALS EXCEED LIMIT", new Integer(MERCHANT_WITHDRAWALS_EXCEED_LIMIT));
    map.put("INVALID BUSINESS DATE", new Integer(INVALID_BUSINESS_DATE));
    map.put("KEY EXCHANGE VALIDATION FAILED", new Integer(KEY_EXCHANGE_VALIDATION_FAILED));
    map.put("INVALID PIN BLOCK", new Integer(INVALID_PIN_BLOCK));
    map.put("PIN LENGTH INVALID", new Integer(PIN_LENGTH_INVALID));
    map.put("NO KEYS AVAILIABLE FOR USE", new Integer(NO_KEYS_AVAILIABLE_FOR_USE));
    map.put("KME KEY SYNC ERROR", new Integer(KME_KEY_SYNC_ERROR));
    map.put("PIN KEY SYNC ERROR", new Integer(PIN_KEY_SYNC_ERROR));
    map.put("MAC KEY SYNC ERROR", new Integer(MAC_KEY_SYNC_ERROR));
    map.put("CUTOFF IN PROGRESS", new Integer(CUTOFF_IN_PROGRESS));
    map.put("ISSUER OR SWITCH IS INOPERATIVE", new Integer(ISSUER_OR_SWITCH_IS_INOPERATIVE));
    map.put("FI/NETWORK NOT FOUND", new Integer(FI_NETWORK_NOT_FOUND));
    map.put("DUPLICATED TRANSMISSION", new Integer(DUPLICATED_TRANSMISSION));
    map.put("SYSTEM MALFUNCTION", new Integer(SYSTEM_MALFUNCTION));
    map.put("SYSTEM MALFUNCTION", new Integer(SYSTEM_MALFUNCTION));
    map.put("UNSUPPORTED CARD TYPE", new Integer(UNSUPPORTED_CARD_TYPE));
  }

*/
}


