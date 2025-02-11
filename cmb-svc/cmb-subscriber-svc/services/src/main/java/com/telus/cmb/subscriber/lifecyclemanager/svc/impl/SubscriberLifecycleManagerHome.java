package com.telus.cmb.subscriber.lifecyclemanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


/**
 * @author Anitha Duraisamy
 *
 */
public interface SubscriberLifecycleManagerHome extends EJBHome {
	
	SubscriberLifecycleManagerRemote create() throws CreateException, RemoteException;

}