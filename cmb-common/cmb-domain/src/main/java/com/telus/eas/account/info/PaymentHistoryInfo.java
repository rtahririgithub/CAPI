/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.*;
import com.telus.eas.framework.info.Info;

public class PaymentHistoryInfo extends Info implements PaymentHistory {

	 static final long serialVersionUID = 1L;
	
    private int seqNo;
    private Date date;
    private String paymentMethodCode;
    private String paymentMethodSubCode;
    private String sourceTypeCode;
    private String sourceID;
    private double originalAmount;
    private double amountDue;
    private Date depositDate;
    private double actualAmount;
    private Date billDate;
    private String activityCode;
    private String activityReasonCode;
    private boolean isBalanceIgnoreFlag;
    private CreditCardInfo creditCard = new CreditCardInfo();
    private ChequeInfo cheque = new ChequeInfo();    
    private int originalBanId;
    private int fileSequenceNumber;
    private int batchNumber;
    private int batchLineNumber;

    public PaymentHistoryInfo() {
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Date getDate() {
        return date;
    }

    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    public String getPaymentMethodSubCode() {
        return paymentMethodSubCode;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public String getSourceID() {
        return sourceID;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public Date getDepositDate() {
        return depositDate;
    }

    public double getActualAmount() {
        return actualAmount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public String getActivityReasonCode() {
        return activityReasonCode;
    }

    public boolean isBalanceIgnoreFlag() {
        return isBalanceIgnoreFlag;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    public void setPaymentMethodSubCode(String paymentMethodSubCode) {
        this.paymentMethodSubCode = paymentMethodSubCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }

    public void setActualAmount(double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public void setActivityReasonCode(String activityReasonCode) {
        this.activityReasonCode = activityReasonCode;
    }

    public void isBalanceIgnoreFlag(String value) {
    	/*
        if (value != null && value.length() > 0 && value.charAt(0) == 'Y')
            isBalanceIgnoreFlag = true;
        */
    	isBalanceIgnoreFlag = "N".equals(value)? false: true;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public CreditCardInfo getCreditCard0() {
        return creditCard;
    }

    public Cheque getCheque() {
        return cheque;
    }

    public ChequeInfo getCheque0() {
        return cheque;
    }

    public void setCheque0(ChequeInfo cheque0) {
        this.cheque = cheque0;
    }

    public void setCreditCard0(CreditCardInfo creditCard0) {
        this.creditCard = creditCard0;
    }
    
    public int getBatchLineNumber() {
		return batchLineNumber;
	}

	public void setBatchLineNumber(int batchLineNumber) {
		this.batchLineNumber = batchLineNumber;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	public int getFileSequenceNumber() {
		return fileSequenceNumber;
	}

	public void setFileSequenceNumber(int fileSequenceNumber) {
		this.fileSequenceNumber = fileSequenceNumber;
	}

	public int getOriginalBanId() {
		return originalBanId;
	}

	public void setOriginalBanId(int originalBanId) {
		this.originalBanId = originalBanId;
	}
	
	/**
	 * NO-OP
	 */
	public PaymentActivity[] getPaymentActivities() throws TelusAPIException {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

  public void transferPayment(PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText) throws TelusAPIException {
   	throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

  public String toString() {
        StringBuffer s = new StringBuffer(128);

        s.append("PaymentHistoryInfo:[\n");
        s.append("    seqNo=[").append(seqNo).append("]\n");
        s.append("    date=[").append(date).append("]\n");
        s.append("    paymentMethodCode=[").append(paymentMethodCode).append("]\n");
        s.append("    paymentMethodSubCode=[").append(paymentMethodSubCode).append("]\n");
        s.append("    sourceTypeCode=[").append(sourceTypeCode).append("]\n");
        s.append("    sourceID=[").append(sourceID).append("]\n");
        s.append("    originalAmount=[").append(originalAmount).append("]\n");
        s.append("    amountDue=[").append(amountDue).append("]\n");
        s.append("    depositDate=[").append(depositDate).append("]\n");
        s.append("    actualAmount=[").append(actualAmount).append("]\n");
        s.append("    billDate=[").append(billDate).append("]\n");
        s.append("    activityCode=[").append(activityCode).append("]\n");
        s.append("    activityReasonCode=[").append(activityReasonCode).append("]\n");
        s.append("    isBalanceIgnoreFlag=[").append(isBalanceIgnoreFlag).append("]\n");
        s.append("    originalBanId=[").append(originalBanId).append("]\n");
        s.append("    fileSequenceNumber=[").append(fileSequenceNumber).append("]\n");
        s.append("    batchNumber=[").append(batchNumber).append("]\n");
        s.append("    batchLineNumber=[").append(batchLineNumber).append("]\n");
        s.append("    creditCard=[").append(creditCard).append("]\n");
        s.append("    cheque=[").append(cheque).append("]\n");
        s.append("]");

        return s.toString();
    }
  
  	public void copyFrom(PaymentHistoryInfo o){
  		seqNo = o.seqNo;
  	    date = cloneDate(o.date);
  	    paymentMethodCode = o.paymentMethodCode;
  	    paymentMethodSubCode = o.paymentMethodSubCode;
  	    sourceTypeCode = o.sourceTypeCode;
  	    sourceID = o.sourceID;
  	    originalAmount = o.originalAmount;
  	    amountDue = o.amountDue;
  	    depositDate = cloneDate(o.depositDate);
  	    actualAmount = o.actualAmount;
  	    billDate = cloneDate(o.billDate);
  	    activityCode = o.activityCode;
  	    activityReasonCode = o.activityReasonCode;
  	    isBalanceIgnoreFlag = o.isBalanceIgnoreFlag;
  	    creditCard.copyFrom(o.creditCard);
  	    cheque.copyFrom(o.cheque);
  	    originalBanId = o.originalBanId;
  	    fileSequenceNumber = o.fileSequenceNumber;
  	    batchNumber = o.batchNumber;
  	    batchLineNumber = o.batchLineNumber;

  	}
  
  	public boolean isPaymentBackedout() {
  		return (activityCode==null)? false: PaymentActivity.ACTIVITY_PAYMENT_BACKOUT.equals(activityCode.trim());
  	}
  	
  	public boolean isPaymentFullyTransferred() {
  		return (activityCode==null)? false: PaymentActivity.ACTIVITY_TRANSFER_OUT.equals(activityCode.trim()) && actualAmount==0;
  	}
  	
  	public boolean isPaymentRefunded() {
  		return (activityCode==null)? false: PaymentActivity.ACTIVITY_REFUND.equals(activityCode.trim());
  	}
  	
  	public boolean isPaymentSufficient() {
  		//defect PROD00170745 fix, remove actualAmount > 0 check.
  		// when account has no payment history, actualAmount is zero. So as long as it equals original amount 
  		//return true. 
  		return actualAmount==originalAmount;
  	}
  	
  	public void refundPayment(String businessRole, String reasonCode,
		String memoText, boolean isManual, AuditHeader auditHeader)	throws TelusAPIException {
  	   	throw new java.lang.UnsupportedOperationException("Method not implemented here");
  	}
}



