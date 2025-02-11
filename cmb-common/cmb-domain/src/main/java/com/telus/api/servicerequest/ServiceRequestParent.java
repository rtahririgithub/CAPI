package com.telus.api.servicerequest;

import java.sql.Timestamp;

public interface ServiceRequestParent {

	long getServiceRequestParentId();
	Timestamp getServiceRequestTimestamp();
	long getServiceRequestRelationshipTypeId();
	
}
