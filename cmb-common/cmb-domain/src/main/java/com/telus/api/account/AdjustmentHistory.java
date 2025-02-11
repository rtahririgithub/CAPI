/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.*;

public interface AdjustmentHistory {

	/**
	 * Gets Adjustment Creation Date.
	 *
	 */
    Date getDate();
    
    String getCode();
 
    String getReasonCode();

    double getAmount();

    double getAmountPST();

    double getAmountGST();

    double getAmountHST();

    String getServiceCode();

    String getFeatureCode();

    String getSubscriberMobile();
    
		Date getAppliedDate();
		
		String getBalanceImpactCode();
		
		int getOperatorId();    
}
