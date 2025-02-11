
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

public interface CreditClass extends Reference {
	String getCode();
	String getDescription();
	String getDescriptionFrench();
	String getDepositReqInd();
	String getOnlineSelectInd();
	String getAccountEligibilityForLatePaymentInd();
	String getShortDescription();
	String getShortDescriptionFrench();
}