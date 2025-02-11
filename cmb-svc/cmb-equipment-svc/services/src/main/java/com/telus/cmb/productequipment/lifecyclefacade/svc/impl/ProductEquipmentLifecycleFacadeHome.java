package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

public interface ProductEquipmentLifecycleFacadeHome extends EJBHome{
	
	ProductEquipmentLifecycleFacadeRemote create() throws CreateException, RemoteException;
}
