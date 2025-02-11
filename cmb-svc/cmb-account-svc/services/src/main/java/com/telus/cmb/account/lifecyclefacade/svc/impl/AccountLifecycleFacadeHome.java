package com.telus.cmb.account.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface AccountLifecycleFacadeHome extends EJBHome{
	
	AccountLifecycleFacadeRemote create() throws CreateException, RemoteException;
}
