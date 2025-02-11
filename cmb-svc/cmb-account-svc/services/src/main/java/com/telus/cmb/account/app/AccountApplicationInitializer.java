package com.telus.cmb.account.app;

import weblogic.application.ApplicationLifecycleEvent;

import com.telus.cmb.common.app.ApplicationInitializer;

public class AccountApplicationInitializer extends ApplicationInitializer {

	@Override
	public void preStart(ApplicationLifecycleEvent evt) {
		super.preStart(evt);
		monitoringAgent.addShakedown("AccountInformationHelper", new AccountInformationHelperShakedown());
		monitoringAgent.addShakedown("AccountLifecycleFacade", new AccountLifecycleFacadeShakedown());
		monitoringAgent.addShakedown("AccountLifecycleManager", new AccountLifecycleManagerShakedown());
	}
	
}
