/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.api.account;

import java.util.Date;

/**
 * <CODE>ProvisioningTransactionDetail</CODE>
 *
 */
public interface ProvisioningTransactionDetail {

    String getStatus();
    
    Date getEffectiveDate();
    
    String getService();
    
    String getErrorReason();
    
}
