package com.telus.api.servicerequest;

public interface ServiceRequestHeader {
	
	String getLanguageCode();
	long getApplicationId();
	String getReferenceNumber();
	ServiceRequestParent getServiceRequestParent();
	ServiceRequestNote getServiceRequestNote();

}
