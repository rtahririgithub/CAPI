package com.telus.cmb.utility.contacteventmanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ContactEventManagerHome extends EJBHome{
	
	ContactEventManagerRemote create() throws CreateException, RemoteException;
}
