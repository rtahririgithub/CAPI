package com.telus.api.account;

import java.util.Date;

public interface FeatureParameterHistory {

	String getServiceCode();
	String getFeatureCode();
	String getParameterName();
	String getParameterValue();
	Date getEffectiveDate();
	Date getExpirationDate();
	String getKnowbilityOperatorID();
	String getApplicationID();
	Date getCreationDate();
	Date getUpdateDate();
}
