package com.telus.api.reference;

import java.util.Date;

public interface ServiceAirTimeAllocation extends Reference {
	String getProductType();
	String getServiceType();
	String getBillCycleTreatmentCode();
	Date getEffectiveDate();
	Date getExpriationDate();
	FeatureAirTimeAllocation[] getFeatureAirTimeAllocations();
	boolean isValidSOC();
	String getErrorMessage();
	String getErrorCode();
	String getLocale() ;
	Date getTimeStamp();
	
}
