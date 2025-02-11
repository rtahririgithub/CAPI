package com.telus.api.servicerequest;

import java.sql.Timestamp;
import java.util.Date;

public interface ServiceRequestManager {
	
	/* all parameters are required */
	ServiceRequestNote newServiceRequestNote(long noteTypeId, String noteText) throws TelusServiceRequestException;

	/* all parameters are required */
	ServiceRequestParent newServiceRequestParent(long parentId, Timestamp timestamp, long relationshipTypeId) throws TelusServiceRequestException;
	
	/* application id and language code are required */
	/* referenceNumber, parentRequest and ServiceRequestNote are optional, can be null */
	ServiceRequestHeader newServiceRequestHeader(String languageCode, long applicationId, String referenceNumber, ServiceRequestParent parentRequest, ServiceRequestNote note) throws TelusServiceRequestException;

	void reportPortCancel(int banId, String subscriberId, String dealerCode, String salesRepCode, String user, String phoneNumber, char oldSubscriberStatus, char newSubscriberStatus, String deactivationReason, Date activityDate, ServiceRequestHeader header);
}
