package com.telus.cmb.utility.app;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;

public class UtilityApplicationInitializer extends ApplicationInitializer {
	
	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		monitoringAgent.addShakedown("ConfigurationManager", new ConfigurationManagerShakedown());
		monitoringAgent.addShakedown("ContactEventManager", new ContactEventManagerShakedown());
		monitoringAgent.addShakedown("DealerManager", new DealerManagerShakedown());
		monitoringAgent.addShakedown("QueueEventManager", new QueueEventManagerShakedown());
	}

}
