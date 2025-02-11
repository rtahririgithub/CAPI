package com.telus.cmb.subscriber.lifecyclehelper.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
public interface SubscriberLifecycleHelperHome extends EJBHome{
	SubscriberLifecycleHelperRemote create() throws CreateException, RemoteException;
}
