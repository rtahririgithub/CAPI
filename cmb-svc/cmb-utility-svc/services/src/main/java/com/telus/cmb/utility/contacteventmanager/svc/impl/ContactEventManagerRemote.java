package com.telus.cmb.utility.contacteventmanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.eas.contactevent.info.SMSNotificationInfo;

public interface ContactEventManagerRemote extends EJBObject {
	
	void processNotification(SMSNotificationInfo notification) throws ApplicationException, RemoteException;

	void logSubscriberAuthentication(long subcriptionID,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException, RemoteException;

	void logSubscriberAuthentication(String min,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException, RemoteException;


	void logAccountAuthentication(long accountID,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,
			String applicationID,String userID) throws ApplicationException, RemoteException;


	void logAccountAuthentication(String ban,boolean isAuthenticationSucceeded,
			String channelOrganizationID,String outletID,String salesRepID,String applicationID,
			String userID) throws ApplicationException, RemoteException;
	
}