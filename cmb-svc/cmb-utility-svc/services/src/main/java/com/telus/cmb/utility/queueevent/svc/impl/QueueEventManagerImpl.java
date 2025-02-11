package com.telus.cmb.utility.queueevent.svc.impl;

import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.utility.queueevent.dao.QueueEventDao;
import com.telus.cmb.utility.queueevent.svc.QueueEventManager;
import com.telus.cmb.utility.queueevent.svc.QueueEventManagerTestPoint;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;
import com.telus.framework.config.ConfigContext;


@Stateless(name="QueueEventManager", mappedName="QueueEventManager")
@Remote({QueueEventManager.class, QueueEventManagerTestPoint.class})
@RemoteHome(QueueEventManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class QueueEventManagerImpl implements QueueEventManager, QueueEventManagerTestPoint {
	private static final Logger LOGGER = Logger.getLogger(QueueEventManagerImpl.class);

	@Autowired
	private QueueEventDao queueEventDao;
	
	@Autowired
	@Qualifier("qeTestPointDao")
	private DataSourceTestPointDao testPointDao;
	
	
	public void setQueueEventDao(QueueEventDao queueEventDao) {
		this.queueEventDao = queueEventDao;
	}

	  /**
	   * Create A Queue Wait Threshold Event in CCEVENTS database
	   * 
	   * @param connectionId long
	   * @param phoneNumber String
	   * @param subscriptionId long
	   * @param userId int
	   * @param queueName String
	   * @param thresholdSeconds int
	   * @throws TelusAPIException
	   */
	@Override
	public void createNewEvent(long connectionId, String phoneNumber, long subscriptionId, int userId, String queueName,
			int thresholdSeconds) throws ApplicationException {
		queueEventDao.createNewEvent(connectionId, phoneNumber, subscriptionId, userId, queueName, thresholdSeconds);
		
	}

	  /**
	   * Get A Queue Wait Threshold Event given the connection Id in CCEVENTS database
	   * 
	   * @param connectionId long
	   * @throws TelusAPIException
	   *
	   */
	@Override
	public QueueThresholdEventInfo getEvent(long connectionId) throws ApplicationException {
		return queueEventDao.getEvent(connectionId);
	}

	  /**
	   * Get Queue Wait Threshold Events given the subscriber Id in CCEVENTS database
	   * 
	   * @param subscriberId long
	   * @param from java.util.Date
	   * @param to java.util.Date
	   * @throws TelusAPIException
	   *
	   */
	@Override
	public QueueThresholdEventInfo[] getEvents(long subscriptionId, Date from, Date to) throws ApplicationException {
		SearchResultsInfo searchResultsInfo = queueEventDao.getEvents(subscriptionId, from, to);
		
		return (QueueThresholdEventInfo[])searchResultsInfo.getItems();
	}

	  /**
	   * Select A Queue Wait Threshold Event in CCEVENTS database
	   * 
	   * @param interactionId long
	   * @param subscriptionId long
	   * @param phoneNumber String
	   * @param teamMemberId int
	   * @param userId int
	   * @throws TelusAPIException
	   *
	   */
	@Override
	public void updateEvent(long interactionId, long subscriptionId, String phoneNumber, int teamMemberId, int userId) throws ApplicationException {
		queueEventDao.updateEvent(interactionId, subscriptionId, phoneNumber, teamMemberId, userId);	
	}

	@Override
	public TestPointResultInfo testCCEventsDataSource() {
		return testPointDao.testCcEventsDataSource();
	}

	@Override
	public TestPointResultInfo getccEventPkgVersion() {
		return testPointDao.getccEventPkgVersion();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}
	
}
