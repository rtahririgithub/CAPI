/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.equipment;

import com.telus.api.TelusAPIException;
import com.telus.api.account.PaymentCard;

public interface AirtimeCard extends Card, PaymentCard {
	/*
	 * Used by activation and migration to check the amount associated with the airtime card is greater than the configed minimum amount.
	 * return true if amount is greater than 0 and in Prepaid recharge denomination list depend on application 
	 */
	boolean isValidAmountForApp() throws TelusAPIException;
}


