package com.telus.cmb.utility.activitylogging.svc.impl;

import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.account.Address;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.servicerequest.TelusServiceRequestException;
import com.telus.api.util.JNDINames;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;


public class ActivityLoggingServiceImplIntTest {
	ActivityLoggingService activityLoggingService = null;
	String url="t3://sn25257:30152";
	String sessionId;

	@Before
	public void setup() throws Exception {
		getActivityLoggingService();		
//		sessionId = facadeImpl.openSession("18654", "apollo", "SMARTDESKTOP");
	}
	
	private void getActivityLoggingService() throws Exception{
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		activityLoggingService = (ActivityLoggingService) RemoteBeanProxyFactory.createProxy(ActivityLoggingService.class, JNDINames.TELUS_CMBSERVICE_ACTIVITY_LOGGING, url);
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}
	
	/* only language code and application id are mandatory */
	private ServiceRequestHeader newServiceRequestHeader(String languageCode, long applicationId, String referenceNumber, ServiceRequestParent parentRequest, ServiceRequestNote note) throws TelusServiceRequestException {
		ServiceRequestHeaderInfo headerInfo = new ServiceRequestHeaderInfo();
		
		if (languageCode == null || languageCode.equals(""))
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setLanguageCode(languageCode);
		
		if (applicationId <= 0)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		headerInfo.setApplicationId(applicationId);
		
		headerInfo.setReferenceNumber(referenceNumber);
		headerInfo.setServiceRequestParent(parentRequest);
		headerInfo.setServiceRequestNote(note);
		
		return headerInfo;
	}
	
	@Test
	public void logChangeAccountAddressActivity() throws Exception {
		String dealerCode = "";
		String salesRepCode = "";
		String userId = "18654";
		int banId = 12474;
		Address address = new AddressInfo();
		address.setProvince("ON");
		address.setCity("TORONTO");
		address.setPostalCode("M1M1M1");
		address.setStreetName("CONSILIUM PL.");
		address.setStreetNumber("300");
		
		ServiceRequestHeader header = newServiceRequestHeader("EN", 27, null, null, null);
		ChangeAccountAddressActivity activity = new ChangeAccountAddressActivity(header);
		activity.setActors(dealerCode, salesRepCode, userId);

		activity.setBanId(banId);
		activity.setAddress(address);
		
		
		activityLoggingService.logChangeAccountAddressActivity(activity);
	}
}
