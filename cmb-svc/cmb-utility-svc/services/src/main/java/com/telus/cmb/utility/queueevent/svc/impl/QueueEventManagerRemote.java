package com.telus.cmb.utility.queueevent.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;

public interface QueueEventManagerRemote extends EJBObject {

	void createNewEvent(long connectionId, String phoneNumber, long subscriptionId, 
			int userId, String queueName, 
			int thresholdSeconds) throws ApplicationException, RemoteException;

	QueueThresholdEventInfo[] getEvents(long subscriptionId, Date from, 
			Date to) throws ApplicationException, RemoteException;


	QueueThresholdEventInfo getEvent(long connectionId) throws ApplicationException, RemoteException;


	void updateEvent(long interactionId, long subscriptionId, String phoneNumber, 
			int teamMemberId, int userId) throws ApplicationException, RemoteException;

}
