package com.telus.cmb.productequipment.helper.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ProductEquipmentHelperHome extends EJBHome{
	
	ProductEquipmentHelperRemote create() throws CreateException, RemoteException;
}
