package com.telus.cmb.reference.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ApplicationMessageFacadeHome extends EJBHome {

	ApplicationMessageFacadeRemote create() throws CreateException, RemoteException;
	
}
