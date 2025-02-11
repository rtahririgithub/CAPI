/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;

/**
 * <CODE>InvoiceProperties</CODE>
 *
 */
public interface InvoiceProperties {

    String getHoldRedirectDestinationCode();
    
    void setHoldRedirectDestinationCode(String holdRedirectDestinationCode);

    Date getHoldRedirectFromDate();

    void setHoldRedirectFromDate(Date holdRedirectFromDate);

    Date getHoldRedirectToDate();

    void setHoldRedirectToDate(Date holdRedirectToDate);

    String getInvoiceSuppressionLevel();

    void setInvoiceSuppressionLevel(String invoiceSuppressionLevel);

    boolean isOnHoldOrRedirected();

}
