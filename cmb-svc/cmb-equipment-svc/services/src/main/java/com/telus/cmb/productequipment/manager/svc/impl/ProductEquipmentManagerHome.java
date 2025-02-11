package com.telus.cmb.productequipment.manager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ProductEquipmentManagerHome extends EJBHome{
	
	ProductEquipmentManagerRemote create() throws CreateException, RemoteException;
}
