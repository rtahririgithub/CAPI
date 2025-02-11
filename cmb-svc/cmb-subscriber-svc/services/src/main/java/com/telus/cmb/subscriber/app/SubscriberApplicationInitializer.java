package com.telus.cmb.subscriber.app;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;

public class SubscriberApplicationInitializer extends ApplicationInitializer {

	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		monitoringAgent.addShakedown("SubscriberLifecycleFacade", new SubscriberFacadeShakedown());
		monitoringAgent.addShakedown("SubscriberLifecycleHelper", new SubscriberHelperShakedown());
		monitoringAgent.addShakedown("SubscriberLifecycleManager", new SubscriberManagerShakedown());
	}
	
}
