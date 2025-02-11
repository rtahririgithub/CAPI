package com.telus.cmb.utility.activitylogging.svc.impl;

import java.util.Date;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.reference.ServiceRequestNoteType;
import com.telus.api.reference.ServiceRequestRelationshipType;
import com.telus.api.util.VersionReader;
import com.telus.cmb.utility.activitylogging.dao.ActivityLoggingDao;
import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService;
import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingServiceTestPoint;
import com.telus.eas.activitylog.domain.ActivityLoggingResult;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountPinActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.framework.config.ConfigContext;


@Stateless(name="ActivityLoggingService", mappedName="ActivityLoggingService")
@Remote({ActivityLoggingService.class,ActivityLoggingServiceTestPoint.class})
@RemoteHome(ActivityLoggingServiceHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ActivityLoggingServiceImpl implements ActivityLoggingService ,ActivityLoggingServiceTestPoint{

	@Autowired
	private ActivityLoggingDao activityLoggingDao;

	public void setActivityLoggingDao(ActivityLoggingDao activityLoggingDao) {
		this.activityLoggingDao = activityLoggingDao;
	}
	
	@Override
	public ActivityLoggingResult logChangeAccountPinActivity(ChangeAccountPinActivity activity) {
		return activityLoggingDao.logChangeAccountPinActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangeAccountTypeActivity(ChangeAccountTypeActivity activity) {
		return activityLoggingDao.logChangeAccountTypeActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangeContractActivity(ChangeContractActivity activity) {
		return activityLoggingDao.logChangeContractActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangeEquipmentActivity(ChangeEquipmentActivity activity) {
		return activityLoggingDao.logChangeEquipmentActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangePaymentMethodActivity(ChangePaymentMethodActivity activity) {
		return activityLoggingDao.logChangePaymentMethodActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangePhoneNumberActivity(ChangePhoneNumberActivity activity) {
		return activityLoggingDao.logChangePhoneNumberActivity(activity);
	}

	@Override
	public ActivityLoggingResult logChangeSubscriberStatusActivity(ChangeSubscriberStatusActivity activity) {
		return activityLoggingDao.logChangeSubscriberStatusActivity(activity);
	}

	@Override
	public ActivityLoggingResult logMoveSubscriberActivity(MoveSubscriberActivity activity) {
		return activityLoggingDao.logMoveSubscriberActivity(activity);
	}

	@Override
	public ServiceRequestNoteType[] getServiceRequestNoteTypes() {
		return activityLoggingDao.getServiceRequestNoteTypes();
	}
	
	@Override
	public ServiceRequestRelationshipType[] getServiceRequestRelationshipTypes() {
		return activityLoggingDao.getServiceRequestRelationshipTypes();
	}

	@Override
	public ActivityLoggingResult logChangeAccountAddressActivity(ChangeAccountAddressActivity activity) {
		return activityLoggingDao.logChangeAccountAddressActivity(activity);
	}

	@Override
	public TestPointResultInfo testRequestPersistenceService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public TestPointResultInfo testReferenceDataProvider() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("ActivityLoggingService");
		try {
			activityLoggingDao.getServiceRequestNoteTypes();
			resultInfo.setResultDetail("ActivityLoggingService invoked for ReferenceDataProvider");
			resultInfo.setPass(true);
		}catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		
		return resultInfo;
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");		
	}
	
}