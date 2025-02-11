/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;
import com.telus.api.*;

public interface InvoiceHistory {

    Date getDate();

    boolean getMailedIndicator();

    Date getDueDate();

    double getPreviousBalance();

    double getInvoiceAmount();

    double getAmountDue();

    double getLatePaymentCharge();

    double getPastDue();

    double getAdjustmentAmount();

    double getPaymentReceivedAmount();

    double getCurrentCharges();

    double getTotalTax();

    int getBillSeqNo();

    int getCycleRunYear();

    int getCycleRunMonth();

    int getCycleCode();

    String getStatus();

    int getHomeCallCount();

    int getRoamingCallCount();

    double getHomeCallMinutes();

    double getRoamingCallMinutes();

    double getMonthlyRecurringCharge();

    double getLocalCallingCharges();

    double getOtherCharges();

    double getZoneUsageCharges() ;

    double getEHAUsageCharges() ;

    /**
     * Returns SubscriberInvoiceDetail[]. It's a remote call.
     *
     * @throws TelusAPIException
     * @return SubscriberInvoiceDetail[]
     */
    SubscriberInvoiceDetail[] getSubscriberInvoiceDetails() throws TelusAPIException;
}
