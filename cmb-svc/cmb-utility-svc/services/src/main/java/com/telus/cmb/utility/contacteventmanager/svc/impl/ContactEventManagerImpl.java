package com.telus.cmb.utility.contacteventmanager.svc.impl;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.utility.contacteventmanager.dao.ContactEventDao;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManagerTestPoint;
import com.telus.eas.contactevent.info.SMSNotificationInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name = "ContactEventManager", mappedName = "ContactEventManager")
@Remote({ContactEventManager.class, ContactEventManagerTestPoint.class})
@RemoteHome(ContactEventManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ContactEventManagerImpl implements ContactEventManager, ContactEventManagerTestPoint {
	
	@Autowired
	private ContactEventDao contactEventDao;

	@Autowired
	@Qualifier("ctmTestPointDao")
	private DataSourceTestPointDao testPointDao;
	
	public void setContactEventDao(ContactEventDao contactEventDao) {
		this.contactEventDao = contactEventDao;
	}

	/**
	 * Record an Account Authentication Contact Event in CONE database
	 * with optional associated dealer ids (could be null).
	 *
	 * @param accountID is CODS.CLIENT_ACCOUNT_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 * @throws TelusAPIException
	 */
	@Override
	public void logAccountAuthentication(long accountID, boolean isAuthenticationSucceeded, String channelOrganizationID, String outletID, String salesRepID, String applicationID, String userID)
			throws ApplicationException {
		contactEventDao.logAccountAuthentication(accountID, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}

	/**
	 * Record an Account Authentication Contact Event in CONE database
	 * with optional associated dealer ids (could be null).
	 *
	 * @param accountID is CODS.CLIENT_ACCOUNT_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 * @throws TelusAPIException
	 *
	 */
	@Override
	public void logAccountAuthentication(String ban, boolean isAuthenticationSucceeded, String channelOrganizationID, String outletID, String salesRepID, String applicationID, String userID)
			throws ApplicationException {
		long accountId = contactEventDao.getAccountID(ban);
		contactEventDao.logAccountAuthentication(accountId, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}

	/**
	 * Record a Subscriber Authentication Contact Event in CONE database
	 * with optional associated dealer ids (could be null).
	 *
	 * @param subcriptionID is CODS.SUBSCRIPTION_ID
	 * @param isAuthenticationSuccedded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 * @throws TelusAPIException
	 *
	 */
	@Override
	public void logSubscriberAuthentication(long subcriptionID, boolean isAuthenticationSucceeded, String channelOrganizationID, String outletID, String salesRepID, String applicationID, String userID)
			throws ApplicationException {
		contactEventDao.logSubscriberAuthentication(subcriptionID, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}

	/**
	 * Record a Subscriber Authentication Contact Event in CONE database
	 * with optional associated dealer ids (could be null).
	 *
	 * @param min is Knowbility subscriber number
	 * @param isAuthenticationSucceeded
	 * @param channelOrganizationID is CODS.CHNL_ORG_CD
	 * @param outletID is CODS.OUTLET_CODE
	 * @param salesRepID is CODS.SALES_REP_CD
	 * @param applicationID is Telus API - Provider application ID
	 * @param userID is Telus API - Provider user ID
	 * @throws TelusAPIException
	 *
	 */
	@Override
	public void logSubscriberAuthentication(String min, boolean isAuthenticationSucceeded, String channelOrganizationID, String outletID, String salesRepID, String applicationID, String userID)
			throws ApplicationException {		
		long subscriptionId = contactEventDao.getSubscriptionID(min);
		contactEventDao.logSubscriberAuthentication(subscriptionId, isAuthenticationSucceeded, channelOrganizationID, outletID, salesRepID, applicationID, userID);
	}

	@Override
	public void processNotification(SMSNotificationInfo notification) throws ApplicationException {
		contactEventDao.processNotification(notification);
	}
	
	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}

	@Override
	public TestPointResultInfo testConeDataSource() {
		return testPointDao.testConeDataSource();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

}