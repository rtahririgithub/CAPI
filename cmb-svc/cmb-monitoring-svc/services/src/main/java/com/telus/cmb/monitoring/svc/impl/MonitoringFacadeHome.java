package com.telus.cmb.monitoring.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;


public interface MonitoringFacadeHome extends EJBHome{
	MonitoringFacadeRemote create() throws CreateException, RemoteException;

}
