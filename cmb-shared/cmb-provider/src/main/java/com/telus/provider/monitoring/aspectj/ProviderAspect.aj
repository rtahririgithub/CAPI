package com.telus.provider.monitoring.aspectj;


import java.util.Date;

import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.monitoring.IMethodInvocationManager;
import com.telus.provider.monitoring.MethodInvocationManager;
import com.telus.provider.monitoring.MethodMonitorFlagReader;
import com.telus.provider.monitoring.MethodMonitorFlagReaderImpl;
import com.telus.provider.util.Logger;

public aspect ProviderAspect extends ProviderPointcutAbstractAspect{
	private MethodMonitorFlagReader methodMonitorFlagReader = null;
	private IMethodInvocationManager methodInvocationManager = null;
	
	private IMethodInvocationManager getMethodInvocationManager() {
		if (methodInvocationManager == null)
			methodInvocationManager = MethodInvocationManager.getInstance();
		return methodInvocationManager;
	}
	
	public void setMethodInvocationManager(
			IMethodInvocationManager methodInvocationManager) {
		this.methodInvocationManager = methodInvocationManager;
	}
	
	public MethodMonitorFlagReader getMethodMonitorFlagReader() {
		if (methodMonitorFlagReader == null)
			methodMonitorFlagReader = MethodMonitorFlagReaderImpl.getInstance();
		return methodMonitorFlagReader;
	}
	
	/**
	 * This method allows dependency injection of the MethodMonitorFlagReader
	 * 
	 * @param flagReader
	 */
	public void setMethodMonitorFlagReader(MethodMonitorFlagReader flagReader) {
		this.methodMonitorFlagReader = flagReader;
	}
	
	Object around() : allScope() {			
		if (!getMethodMonitorFlagReader().isMethodMonitoringEnabled()) {			
			return proceed();
		}		
		
		Object returnValue = null;
		
		Date startTime = new Date();

		try {
			returnValue = proceed();
		} finally {
			try {
				Date endTime = new Date();
				String signatureLong = thisJoinPoint.getSignature().toLongString();

				long executionTime = endTime.getTime() - startTime.getTime();

				String appName = "";

				if (thisJoinPoint.getTarget() instanceof BaseProvider) {
					if (((BaseProvider) thisJoinPoint.getTarget()).getProvider() != null) {
						TMProvider provider = ((BaseProvider) thisJoinPoint.getTarget()).getProvider();			
						appName = provider.getApplication();
					}
				}
				getMethodInvocationManager().logMethodInvocation(appName, signatureLong, executionTime);
			} catch (Throwable t) {
				Logger.debug0(t);
			}
		}
		return returnValue;
	}

}
