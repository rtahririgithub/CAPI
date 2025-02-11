/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;

import com.telus.api.*;
import java.util.*;

public interface DiscountPlan extends Reference {

	public static final String DISCOUNT_PLAN_LEVEL_BAN = "B";
	public static final String DISCOUNT_PLAN_LEVEL_SUBSCRIBER = "C";

	Date getEffectiveDate();
	Date getExpiration();
	int getMonths();
	double getAmount();
	double getPercent();
	Date getOfferExpirationDate();
	String getProductType();
	String getLevel();
	int[] getDiscountBrandIDs(); 

	boolean isAssociated(ServiceSummary serviceSummary, int term)
		throws TelusAPIException;
}
