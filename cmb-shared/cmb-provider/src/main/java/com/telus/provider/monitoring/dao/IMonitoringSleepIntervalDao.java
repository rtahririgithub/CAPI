package com.telus.provider.monitoring.dao;


/**
 * 
 * @author t859517 Hilton Poon
 *
 */
public interface IMonitoringSleepIntervalDao {
	
	/**
	 * Returns an the write interval in seconds. No exception is thrown because you MUST define a default.
	 * @return The write interval in seconds.
	 * 
	 */
	public int getSleepInterval();

}
