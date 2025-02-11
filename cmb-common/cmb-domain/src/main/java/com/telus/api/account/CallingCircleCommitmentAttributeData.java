package com.telus.api.account;

import java.util.Date;

public interface CallingCircleCommitmentAttributeData {
	
	Date getEffectiveDate();
	int getTotalAllowedModifications();
	int getRemainingAllowedModifications();
	boolean isPrepaidModificationBlocked();

}
