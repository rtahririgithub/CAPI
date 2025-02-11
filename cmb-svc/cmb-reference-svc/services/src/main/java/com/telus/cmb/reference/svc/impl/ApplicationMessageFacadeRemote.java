package com.telus.cmb.reference.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * 
 * @author tongts
 *
 */
public interface ApplicationMessageFacadeRemote extends EJBObject {

	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) throws RemoteException;
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException, RemoteException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, long messageId) throws RemoteException;
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, long messageId) throws ApplicationException, SystemException, RemoteException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode) throws RemoteException;
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode) throws ApplicationException, SystemException, RemoteException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId) throws RemoteException;
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode, int brandId,ApplicationException ae) throws ApplicationException, SystemException, RemoteException;
	public ApplicationMessageInfo getApplicationMessage(String code, String textEn, String textFr) throws RemoteException;
	// retrieves the ApplicationMessage populated with the source application code, if there is one
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) throws RemoteException;
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException, RemoteException;
	
}
