package com.telus.provider.monitoring.dao;

import java.util.Collection;



public interface IMonitoringDataPointWriteDao {

	/**
	 * Writes the data atomically, ie all or nothing.
	 * @param data The data to write.
	 * @throws MonitoringDataPointWriteDaoException Thrown if there is an exception writing.
	 */
	void write(Collection data) throws MonitoringDataPointWriteDaoException;
}
