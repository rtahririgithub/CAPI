/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;

public interface PaymentMethodChangeHistory{

    Date getDate();

    String getCreditCardType();

    String getCreditCardExpiry();

    String getDirectDebitStatusCode();

    String getBankCode();

    String getBankAccountNumber();

    String getBankBranchNumber();
    
    String getCreditCardTrailingDisplayDigits();
    
    String getCreditCardToken();
    
    String getCreditCardLeadingDisplayDigits ();
    
    
}
