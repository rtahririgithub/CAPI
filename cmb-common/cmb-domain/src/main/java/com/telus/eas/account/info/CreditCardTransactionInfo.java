package com.telus.eas.account.info;
/**
 * Title:        CreditCardTransactionInfo<p>
 * Description:  The CreditCardTransactionInfo holds all necessary information about a credit card transaction.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import com.telus.api.account.AuditHeader;
import com.telus.api.reference.Brand;
import com.telus.eas.framework.info.Info;

public class CreditCardTransactionInfo extends Info {

   static final long serialVersionUID = 1L; //PCI-AT-TODO, update this field to 2

  public static final String TYPE_CREDIT_CARD_VALIDATION = "CCV";
  public static final String TYPE_CREDIT_CARD_PAYMENT    = "CCP";
  public static final String TYPE_CREDIT_CARD_REFUND     = "CCR";
  public static final String TYPE_CREDIT_CARD_VOID     = "CCU";
  public static String TYPE_CREDIT_CARD_REFUND_VOID ="CCRV";

  public static final String REASON_PAYMENT_REFUND = "CCPR";

  private String transactionType;
  private double amount;
  private  CreditCardInfo creditCardInfo = new CreditCardInfo();
  private String chargeAuthorizationNumber;
  private int ban;
  private  CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
  private int brandId = Brand.BRAND_ID_TELUS;
  private AuditHeader auditHeader;

  public CreditCardTransactionInfo(){}

  public CreditCardTransactionInfo(int pBAN,
                                   String pTransactionType,
                                   double pAmount,
                                   CreditCardInfo pCreditCardInfo,
                                   String pChargeAuthorizationNumber,
                                   CreditCardHolderInfo pCreditCardHolderInfo){

    setBan(pBAN);
    setTransactionType(pTransactionType);
    setAmount(pAmount);
    setCreditCardInfo(pCreditCardInfo);
    setChargeAuthorizationNumber(pChargeAuthorizationNumber);
    setCreditCardHolderInfo(pCreditCardHolderInfo);
  }

  public String getTransactionType() {
    return transactionType;
  }
  public void setTransactionType(String newTransactionType) {
    transactionType = newTransactionType;
  }
  public double getAmount() {
    return amount;
  }
  public void setAmount(double newAmount) {
    amount = newAmount;
  }
  public CreditCardInfo getCreditCardInfo() {
    return creditCardInfo;
  }
  public void setCreditCardInfo(CreditCardInfo newCreditCardInfo) {
    creditCardInfo = newCreditCardInfo;
  }
  public String getChargeAuthorizationNumber() {
    return chargeAuthorizationNumber;
  }
  public void setChargeAuthorizationNumber(String newChargeAuthorizationNumber) {
    chargeAuthorizationNumber = newChargeAuthorizationNumber;
  }
  public CreditCardHolderInfo getCreditCardHolderInfo() {
    return creditCardHolderInfo;
  }
  public void setCreditCardHolderInfo(CreditCardHolderInfo creditCardHolderInfo) {
    this.creditCardHolderInfo = creditCardHolderInfo;
  }
  public int getBrandId() {
	  return brandId;
  }
  public void setBrandId(int brandId) {
	  this.brandId = brandId;
  }
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CreditCardTransactionInfo:{\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    transactionType=[").append(transactionType).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    creditCardInfo=[").append(creditCardInfo).append("]\n");
    s.append("    chargeAuthorizationNumber=[").append(chargeAuthorizationNumber).append("]\n");
    s.append("    creditCardHolderInfo=[").append(creditCardHolderInfo).append("]\n");
    s.append("    brandId=[").append(brandId).append("]\n");
    s.append("}");

    return s.toString();
  }
  public void setBan(int ban) {
    this.ban = ban;
  }
  public int getBan() {
    return ban;
  }
  
  public AuditHeader getAuditHeader() {
	  return auditHeader;
  }
  public void setAuditHeader( AuditHeader auditHeader ) {
	  this.auditHeader  =auditHeader;
  }

}
