/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.DepositHistory;
import com.telus.eas.framework.info.Info;

public class DepositHistoryInfo extends Info implements DepositHistory {

   static final long serialVersionUID = 1L;

  private Date invoiceCreationDate;
  private Date invoiceDueDate;
  private char invoiceStatus;
  private double chargesAmount;
  private double depositPaidAmount;
  private Date depositPaidDate;
  private Date depositReturnDate;
  private char depositReturnMethod;
  private char depositTermsCode;
  private Date cancellationDate;
  private String cancellationReasonCode;
  private char paymentExpIndicator;
  private String subscriberId;
  private int operatorId;
  private double returnedAmount;
  private double cancelledAmount;

  public Date getInvoiceCreationDate() {
    return invoiceCreationDate;
  }

  public Date getInvoiceDueDate() {
    return invoiceDueDate;
  }

  public char getInvoiceStatus() {
    return invoiceStatus;
  }

  public double getChargesAmount() {
    return chargesAmount;
  }

  public double getDepositPaidAmount() {
    return depositPaidAmount;
  }

  public Date getDepositPaidDate() {
    return depositPaidDate;
  }

  public Date getDepositReturnDate() {
    return depositReturnDate;
  }

  public char getDepositReturnMethod() {
    return depositReturnMethod;
  }

  public char getDepositTermsCode() {
    return depositTermsCode;
  }

  public Date getCancellationDate() {
    return cancellationDate;
  }

  public String getCancellationReasonCode() {
    return cancellationReasonCode;
  }

  public char getPaymentExpIndicator() {
    return paymentExpIndicator;
  }

  public String getSubscriberId() {
    return subscriberId;
  }
  public int getOperatorId() {
    return operatorId;
  }

  public void setInvoiceCreationDate(Date value) {
    invoiceCreationDate = value;
  }

  public void setInvoiceDueDate(Date value) {
    invoiceDueDate = value;
  }

  public void setInvoiceStatus(String value) {
    if (value != null && value.length() > 0)
      invoiceStatus = value.charAt(0);
  }

  public void setChargesAmount(double value) {
    chargesAmount = value;
  }

  public void setDepositPaidAmount(double value) {
    depositPaidAmount = value;
  }

  public void setDepositPaidDate(Date value) {
    depositPaidDate = value;
  }

  public void setDepositReturnDate(Date value) {
    depositReturnDate = value;
  }

  public void setDepositReturnMethod(String value) {
    if (value != null && value.length() > 0)
     depositReturnMethod = value.charAt(0);
  }

  public void setDepositTermsCode(String value) {
    if (value != null && value.length() > 0)
      depositTermsCode = value.charAt(0);
  }

  public void setCancellationDate(Date value) {
    cancellationDate = value;
  }

  public void setCancellationReasonCode(String value) {
    cancellationReasonCode = value;
  }

  public void setPaymentExpIndicator(String value) {
    if (value != null && value.length() > 0)
      paymentExpIndicator = value.charAt(0);
  }

  public void setSubscriberId(String value) {
    subscriberId = value;
  }
  
  public void setOperatorId(int value) {
    operatorId = value;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);
    s.append("DepositHistoryInfo:[\n");
    s.append("    invoiceCreationDate=[").append(invoiceCreationDate).append("]\n");
    s.append("    invoiceDueDate=[").append(invoiceDueDate).append("]\n");
    s.append("    invoiceStatus=[").append(invoiceStatus).append("]\n");
    s.append("    chargesAmount=[").append(chargesAmount).append("]\n");
    s.append("    depositPaidAmount=[").append(depositPaidAmount).append("]\n");
    s.append("    depositPaidDate=[").append(depositPaidDate).append("]\n");
    s.append("    depositReturnDate=[").append(depositReturnDate).append("]\n");
    s.append("    depositReturnMethod=[").append(depositReturnMethod).append("]\n");
    s.append("    depositTermsCode=[").append(depositTermsCode).append("]\n");
    s.append("    cancellationDate=[").append(cancellationDate).append("]\n");
    s.append("    cancellationReasonCode=[").append(cancellationReasonCode).append("]\n");
    s.append("    paymentExpIndicator=[").append(paymentExpIndicator).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    operatorId=[").append(operatorId).append("]\n");
    s.append("    cancelledAmount=[").append(cancelledAmount).append("]\n");
    s.append("    returnedAmount=[").append(returnedAmount).append("]\n");
    s.append("]");

    return s.toString();
  }
/**
 * @return Returns the cancelledAmount.
 */
public double getCancelledAmount() {
	return cancelledAmount;
}
/**
 * @param cancelledAmount The cancelledAmount to set.
 */
public void setCancelledAmount(double cancelledAmount) {
	this.cancelledAmount = cancelledAmount;
}
/**
 * @return Returns the returnedAmount.
 */
public double getReturnedAmount() {
	return returnedAmount;
}
/**
 * @param returnedAmount The returnedAmount to set.
 */
public void setReturnedAmount(double returnedAmount) {
	this.returnedAmount = returnedAmount;
}
}