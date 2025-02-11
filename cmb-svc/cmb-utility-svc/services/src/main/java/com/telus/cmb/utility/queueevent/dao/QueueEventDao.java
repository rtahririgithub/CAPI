package com.telus.cmb.utility.queueevent.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;


public interface QueueEventDao {
	SearchResultsInfo getEvents(long subscriptionId, Date from, Date to) throws ApplicationException;
	
	QueueThresholdEventInfo getEvent(long connectionId) throws ApplicationException;

	void updateEvent(long interactionId, long subscriptionId, 
			String phoneNumber, int teamMemberId, int userId) throws ApplicationException;

	void createNewEvent(long connectionId, String phoneNumber, 
			long subscriptionId, int userId, String queueName, 
			int thresholdSeconds) throws ApplicationException;
}
