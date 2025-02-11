package com.telus.cmb.monitoring.svc.impl;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.cmb.monitoring.svc.MonitoringFacade;
import com.telus.cmb.monitoring.svc.dao.DaoFactory;
import com.telus.cmb.monitoring.svc.dao.MonitoringDataDaoWriteException;
import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;
import com.telus.eas.framework.exception.TelusException;


@Stateless(name="MonitoringFacade", mappedName="MonitoringFacade")
@Remote(MonitoringFacade.class)
@RemoteHome(MonitoringFacadeHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class MonitoringFacadeImpl implements MonitoringFacade{	
	private static final Log logger = LogFactory.getLog(MonitoringFacadeImpl.class);
	
	
	public void log(IMonitoringDataPointInfo[] monitoringDataPoints) throws TelusException{
		try {
			if (monitoringDataPoints != null) {
				logger.debug(monitoringDataPoints.length + " monitoring records being logged.");			
				DaoFactory.getMonitoringDataDao().write(monitoringDataPoints);
			}
		} catch (MonitoringDataDaoWriteException e) {
			throw new TelusException(e);
		}
	}
}
