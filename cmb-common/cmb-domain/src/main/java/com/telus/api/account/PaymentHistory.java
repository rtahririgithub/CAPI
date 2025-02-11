/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;

public interface PaymentHistory {
	
    int getSeqNo();

    Date getDate();

    String getPaymentMethodCode();

    String getPaymentMethodSubCode();

    String getSourceTypeCode();

    String getSourceID();

    double getOriginalAmount();

    double getAmountDue();
    
    Date getDepositDate();
    
    double getActualAmount();
    
    Date getBillDate();
    
    String getActivityCode();
    
    String getActivityReasonCode();

    public boolean isBalanceIgnoreFlag();

    CreditCard getCreditCard();

    Cheque getCheque();
    
    int getOriginalBanId();
    
    int getFileSequenceNumber();
    
    int getBatchNumber();
    
    int getBatchLineNumber();
    
	/**
	 * Retrieves the payment activities.
	 *
	 * <P>This method may involve a remote method call.
	 */
    PaymentActivity[] getPaymentActivities() throws TelusAPIException;
    
    void transferPayment(PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText) throws TelusAPIException;
    
    /**
     * This method refund this payment.
     *  
     * @param businessRole
     * @param reasonCode
     * @param memoText
     * @param isManual false indicate the application want the System to issue refund, true means the refund already being issued, only need
     * to record this refund transaction in KB 
     * @param auditHeader
     * 
     * @throws TelusAPIException
     */
    void refundPayment(String businessRole, String reasonCode, String memoText, boolean isManual, AuditHeader auditHeader) throws InvalidCreditCardException, TelusAPIException;
    boolean isPaymentRefunded();
    boolean isPaymentBackedout();
    boolean isPaymentFullyTransferred();
    boolean isPaymentSufficient();

}
