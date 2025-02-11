package com.telus.cmb.account.lifecyclemanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface AccountLifecycleManagerHome extends EJBHome{
	
	AccountLifecycleManagerRemote create() throws CreateException, RemoteException;
}
