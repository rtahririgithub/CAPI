package com.telus.cmb.productequipment.app;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;

public class ProductEquipmentApplicationInitializer extends ApplicationInitializer {

	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		monitoringAgent.addShakedown("ProductEquipmentFacade", new ProductEquipmentFacadeShakedown());
		monitoringAgent.addShakedown("ProductEquipmentHelper", new ProductEquipmentHelperShakedown());
		monitoringAgent.addShakedown("ProductEquipmentManager", new ProductEquipmentManagerShakedown());
	}
	
}
