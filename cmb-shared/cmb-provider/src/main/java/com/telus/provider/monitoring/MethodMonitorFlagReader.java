package com.telus.provider.monitoring;

/**
 * This interface is used to retrieve a boolean flag to indicate if the AspectJ
 * monitoring is enabled or not.
 * 
 * @author t869409
 *
 */
public interface MethodMonitorFlagReader {
	public final String METHOD_MONITORING_FLAG_SYS_PROPERTY = "com.telus.provider.monitoring.isMethodMonitoringEnabled";

	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.MethodMonitoringFlagReader#isMethodMonitoringEnabled()
	 */
	public abstract boolean isMethodMonitoringEnabled();

	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.MethodMonitoringFlagReader#setMethodMonitoringEnabled(boolean)
	 */
	public abstract void setMethodMonitoringEnabled(
			boolean isMethodMonitoringEnabled);

}