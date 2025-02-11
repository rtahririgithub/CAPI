package com.telus.cmb.utility.queueevent.svc;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;


public interface QueueEventManager {

	/**
	 * Create A Queue Wait Threshold Event in CCEVENTS database
	 * @param connectionId long
	 * @param phoneNumber String
	 * @param subscriptionId long
	 * @param userId int
	 * @param queueName String
	 * @param thresholdSeconds int
	 */
	void createNewEvent(long connectionId, String phoneNumber, long subscriptionId, 
			int userId, String queueName, int thresholdSeconds) throws ApplicationException;


	/**
	 * Get Queue Wait Threshold Events given the subscription Id in CCEVENTS database
	 * @param subscriptionId long
	 * @param from java.util.Date
	 * @param to java.util.Date
	 */
	QueueThresholdEventInfo[] getEvents(long subscriptionId, Date from, Date to) throws ApplicationException;

	/**
	 * Get A Queue Wait Threshold Event given the connection Id in CCEVENTS database
	 * @param connectionId long
	 */
	QueueThresholdEventInfo getEvent(long connectionId) throws ApplicationException;

	/**
	 * Select A Queue Wait Threshold Event in CCEVENTS database
	 * @param interactionId long
	 * @param subscriptionId long
	 * @param phoneNumber String
	 * @param teamMemberId int
	 * @param userId int
	 */
	void updateEvent(long interactionId, long subscriptionId, String phoneNumber, 
			int teamMemberId, int userId) throws ApplicationException;
}
