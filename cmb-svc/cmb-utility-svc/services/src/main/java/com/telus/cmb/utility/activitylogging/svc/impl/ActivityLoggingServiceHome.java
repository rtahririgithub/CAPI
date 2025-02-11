package com.telus.cmb.utility.activitylogging.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ActivityLoggingServiceHome extends EJBHome{
	
	ActivityLoggingServiceRemote create() throws CreateException, RemoteException;
}
