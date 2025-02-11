package com.telus.provider.monitoring.scheduling;

import com.telus.provider.monitoring.IMethodInvocationManager;
import com.telus.provider.monitoring.MethodInvocationManager;
import com.telus.provider.scheduling.AJob;
import com.telus.provider.scheduling.IJob;


public class MonitoringWriteJob extends AJob implements IJob{
	
	public void execute() {
		IMethodInvocationManager methodInvocationManager = MethodInvocationManager.getInstance();
		
		methodInvocationManager.write();

	}
}
