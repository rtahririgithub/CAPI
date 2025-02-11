package com.telus.cmb.utility.dealermanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface DealerManagerHome extends EJBHome{
	
	DealerManagerRemote create() throws CreateException, RemoteException;
}
