package com.telus.provider;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.EquipmentManager;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.provider.reference.TMReferenceDataManager;

public class TestTMProvider extends TMProvider {

	private boolean newEjbCalled = false;


	public TestTMProvider(String user, String password, String applicationCode,
			int[] brandIds) throws TelusAPIException {
		super(user, password, applicationCode, brandIds);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public AccountInformationHelper getAccountInformationHelper() {
		newEjbCalled = true;
		return null;
	}
	
	

	public SubscriberLifecycleManager getSubscriberLifecycleManager() {
		newEjbCalled = true;
		return null;
	}

	
	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() {
		newEjbCalled = true;
		return null;
	}
	

	
	public ProductEquipmentHelper getProductEquipmentHelper() {
		newEjbCalled = true;
		return null;
	}
	
	public ProductEquipmentManager getProductEquipmentManager() {
		newEjbCalled = true;
		return null;
	}
	
	public synchronized EquipmentManager getEquipmentManager() {
		newEjbCalled  = false;
		return null;
	}
	
	public ProductEquipmentLifecycleFacade getProductEquipmentLifecycleFacade() {
		newEjbCalled = true;
		return null;
	}
	
	public synchronized TMReferenceDataManager getReferenceDataManager0() {
		//newEjbCalled  = false;
		return null;
	}
	
	
	public boolean isNewEjbCalled() {
		return newEjbCalled;
	}

	public void setNewEjbCalled(boolean newEjbCalled) {
		this.newEjbCalled = newEjbCalled;
	}
}
