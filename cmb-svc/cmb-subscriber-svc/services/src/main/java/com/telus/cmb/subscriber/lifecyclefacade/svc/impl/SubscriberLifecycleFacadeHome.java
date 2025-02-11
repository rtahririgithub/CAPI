package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


/**
 * @author Anitha Duraisamy
 *
 */
public interface SubscriberLifecycleFacadeHome extends EJBHome {
	
	SubscriberLifecycleFacadeRemote create() throws CreateException, RemoteException;

}