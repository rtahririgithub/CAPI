package com.telus.cmb.utility.configurationmanager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ConfigurationManagerHome extends EJBHome{
	
	ConfigurationManagerRemote create() throws CreateException, RemoteException;
}
