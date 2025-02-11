package com.telus.cmb.account.informationhelper.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface AccountInformationHelperHome extends EJBHome{
	AccountInformationHelperRemote create() throws CreateException, RemoteException;
}
