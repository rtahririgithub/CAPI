package com.telus.provider.monitoring;

import com.telus.provider.util.AppConfiguration;

/**
 * This singleton class is used to retrieve a boolean flag to indicate whether
 * method monitoring through scheduled logging of the AspectJ method interceptor
 * is invoked or not. The class populates the class member isMethodMonitoringEnabled
 * available through its accessors.  A value of true will indicate that monitoring
 * will be made.
 * 
 * The class first tries to retrieve the value from the Java system
 * property: com.telus.provider.monitoring.isMethodMonitoringEnabled
 * 
 * If no Java system property is found, it will look in LDAP under
 * the node: Telus-ECA/ClientAPI/ClientAgentMonitoring/isMethodMonitoringEnabled
 * 
 * If there is no LDAP property, isMethodMonitoringEnabled = true.  In other words
 * method monitoring will be enabled.
 * 
 * @author t869409
 *
 */
public class MethodMonitorFlagReaderImpl implements MethodMonitorFlagReader {
	private static MethodMonitorFlagReader inst;
	private boolean isMethodMonitoringEnabled = true;
	private boolean isDirty = false;
	
	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.MethodMonitoringFlagReader#isMethodMonitoringEnabled()
	 */
	public boolean isMethodMonitoringEnabled() {
		if (isDirty) {
			return isMethodMonitoringEnabled;
		}else {
			return AppConfiguration.isMethodMonitoringEnabledForClientAgentMonitoring();
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.provider.monitoring.MethodMonitoringFlagReader#setMethodMonitoringEnabled(boolean)
	 * For testing only. This method is not supposed to be used in production code
	 * 
	 * 
	 */
	/**
	 * @deprecated
	 */
	public void setMethodMonitoringEnabled(boolean isMethodMonitoringEnabled) {
		this.isMethodMonitoringEnabled = isMethodMonitoringEnabled;
		isDirty = true;
	}

	private MethodMonitorFlagReaderImpl() {
	}
	
	public static synchronized MethodMonitorFlagReader getInstance() {
		if (inst == null) {
			inst = new MethodMonitorFlagReaderImpl();
		}
		return inst;
	}
	
}
