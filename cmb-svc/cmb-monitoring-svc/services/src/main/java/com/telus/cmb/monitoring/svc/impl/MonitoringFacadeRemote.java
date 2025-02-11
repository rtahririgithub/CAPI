package com.telus.cmb.monitoring.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.cmb.monitoring.svc.domain.IMonitoringDataPointInfo;
import com.telus.eas.framework.exception.TelusException;

public interface MonitoringFacadeRemote extends EJBObject{
	
	void log(IMonitoringDataPointInfo[] monitoringDataPoints) throws TelusException, RemoteException;
}
