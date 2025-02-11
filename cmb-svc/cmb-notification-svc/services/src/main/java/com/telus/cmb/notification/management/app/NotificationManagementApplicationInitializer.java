package com.telus.cmb.notification.management.app;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;

public class NotificationManagementApplicationInitializer extends ApplicationInitializer {

	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		//monitoringAgent.addShakedown("AccountLifecycleManager", new AccountLifecycleManagerShakedown());
	}
	
}
