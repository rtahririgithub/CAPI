package com.telus.cmb.utility.queueevent.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface QueueEventManagerHome extends EJBHome{
	
	QueueEventManagerRemote create() throws CreateException, RemoteException;
}
