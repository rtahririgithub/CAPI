package com.telus.cmb.reference.svc;

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
public interface ApplicationMessageFacade {

	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId);
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, long messageId);
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, long messageId) throws ApplicationException, SystemException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode);
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode) throws ApplicationException, SystemException;
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId);
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode, int brandId,ApplicationException ae) throws ApplicationException, SystemException;
	public ApplicationMessageInfo getApplicationMessage(String code, String textEn, String textFr);
	// retrieves the ApplicationMessage populated with the source application code, if there is one
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId);
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException;
}
