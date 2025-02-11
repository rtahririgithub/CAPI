package com.telus.cmb.reference.svc.impl;

import java.util.Hashtable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.message.ApplicationMessageType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.cmb.reference.svc.ApplicationMessageFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;

@Stateless(name="ApplicationMessageFacade", mappedName="ApplicationMessageFacade")
@Interceptors({ApplicationMessageFacadeSvcInvocationInterceptor.class})
@Remote(ApplicationMessageFacade.class)
@RemoteHome(ApplicationMessageFacadeHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ApplicationMessageFacadeImpl implements ApplicationMessageFacade {
	private static final Logger logger = Logger.getLogger(ApplicationMessageFacadeImpl.class);
	
	@EJB
	ReferenceDataHelper referenceDataHelper;
	@EJB
	ReferenceDataFacade referenceDataFacade;
	
	// application messages mapped by their message IDs.
	@SuppressWarnings("rawtypes")
	private final Hashtable<String, Hashtable> messagesById = new Hashtable<String, Hashtable>();

	// application message IDs grouped by source application codes.
	@SuppressWarnings("rawtypes")
	private final Hashtable<String, Hashtable> messageIdsBySourceApp = new Hashtable<String, Hashtable>();
	
	// source application codes keyed by their corresponding application message ID.
	private final Hashtable<String, String> sourceCodesByMessageIdsMap = new Hashtable<String, String>();

	@PostConstruct
	public void init()  {
		try {
			loadMessages();
			loadMessageMappings();
		}catch (TelusException e) {
			throw new SystemException ("ApplicationMessageFacade", ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, e.getMessage(), "", e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {
		if (application == null || audienceType == null || brandId <= 0 || messageId <= 0)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable<String, Hashtable> messagesByApp = messagesById.get(Long.toString(messageId));
		if (messagesByApp == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable<String, Hashtable> messagesByAudience = messagesByApp.get(Integer.toString(application.getId()));
		if (messagesByAudience == null)
			messagesByAudience = messagesByApp.get(Integer.toString(ApplicationSummary.DEFAULT.getId()));

		if (messagesByAudience == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable<String, ApplicationMessageInfo> messagesByBrand = (Hashtable) messagesByAudience.get(Integer.toString(audienceType.getId()));
		if (messagesByBrand == null)
			messagesByBrand = messagesByAudience.get(Integer.toString(AudienceType.DEFAULT.getId()));

		if (messagesByBrand == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		ApplicationMessageInfo applicationMessage = messagesByBrand.get(Integer.toString(brandId));
		if (applicationMessage == null)
			applicationMessage = messagesByBrand.get(Integer.toString(Brand.BRAND_ID_ALL));

		return applicationMessage != null ? applicationMessage : getMessageNotFoundApplicationMessage(Long.toString(messageId));
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, long messageId) {
		return getApplicationMessage(application, audienceType, Brand.BRAND_ID_ALL, messageId);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode) {
		return getApplicationMessage(application, audienceType, sourceApplicationCode, sourceMessageCode, Brand.BRAND_ID_ALL);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId) {
		if (application == null || audienceType == null || sourceApplicationCode == null || sourceMessageCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		@SuppressWarnings("unchecked")
		Hashtable<String, String> messagesBySourceCode = messageIdsBySourceApp.get(sourceApplicationCode);
		if (messagesBySourceCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		String messageId = messagesBySourceCode.get(sourceMessageCode);
		if (messageId == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		return getApplicationMessage(application, audienceType, brandId, Long.parseLong(messageId));
	}

	/**
	 * [Naresh Annabathula ] - 26-April -2016 , WNP came up with requirements to return the WNP thrown error messages if error message not found in EAS DB,
	    we added below two methods to map the WNP returned error instead of returning our own custom messages..  ie "not found message one".
	 */
	
	private ApplicationMessageInfo getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId,ApplicationException ae) {
		if (application == null || audienceType == null || sourceApplicationCode == null || sourceMessageCode == null)
			return wrapApplicationMessageFromApplicationException(sourceMessageCode,ae);

		@SuppressWarnings("unchecked")
		Hashtable<String, String> messagesBySourceCode = messageIdsBySourceApp.get(sourceApplicationCode);
		if (messagesBySourceCode == null)
			return wrapApplicationMessageFromApplicationException(sourceMessageCode,ae);

		String messageId = messagesBySourceCode.get(sourceMessageCode);
		if (messageId == null)
			return wrapApplicationMessageFromApplicationException(sourceMessageCode,ae);

		return getApplicationMessage(application, audienceType, brandId, Long.parseLong(messageId));
	}
	
	
	private ApplicationMessageInfo wrapApplicationMessageFromApplicationException(
			String sourceMessageCode,ApplicationException ae) {

		ApplicationMessageInfo info = new ApplicationMessageInfo(
				sourceMessageCode, ApplicationSummary.DEFAULT.getId(),
				AudienceType.DEFAULT.getId(),ApplicationMessageType.MESSAGE_TYPE_ID_ERROR,
				Brand.BRAND_ID_ALL);

		info.setText(Locale.ENGLISH.getLanguage(), ae.getErrorMessage());
		info.setText(Locale.FRENCH.getLanguage(), ae.getErrorMessageFr());

		return info;
	}
	
	@Override
	public ApplicationMessageInfo getApplicationMessage(String code, String textEn, String textFr) {
		ApplicationMessageInfo info = new ApplicationMessageInfo(code, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(), ApplicationMessageType.MESSAGE_TYPE_ID_ERROR, Brand.BRAND_ID_ALL);
		info.setText(Locale.ENGLISH.getLanguage(), textEn);
		info.setText(Locale.FRENCH.getLanguage(), textFr);

		return info;
	}

	/**
	 * retrieves the ApplicationMessage populated with the source application code, if there is one
	 */
	@Override
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {
		ApplicationMessageInfo message = getApplicationMessage(application, audienceType, brandId, messageId);
		String sourceCode = (String)sourceCodesByMessageIdsMap.get(message.getCode());
		return new ApplicationMessageInfo(message.getId(), sourceCode != null ? sourceCode : message.getCode(), 
				message.getApplicationId(), message.getAudienceTypeId(), message.getMessageTypeId(), message.getBrandId(), 
				message.getText(Locale.ENGLISH), message.getText(Locale.FRENCH));
	}
	

	@Override
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException {
		ApplicationSummary applicationSummary = getApplicationSummary (applicationCode, audienceTypeCode == null);
		AudienceType audienceType = getAudienceType (audienceTypeCode, audienceTypeCode == null);
		
		return getApplicationMessage (applicationSummary, audienceType, brandId, messageId);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, long messageId) throws ApplicationException, SystemException {
		ApplicationSummary applicationSummary = getApplicationSummary (applicationCode, audienceTypeCode == null);
		AudienceType audienceType = getAudienceType (audienceTypeCode, audienceTypeCode == null);
		
		return getApplicationMessage (applicationSummary, audienceType, messageId);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode)throws ApplicationException, SystemException {
		ApplicationSummary applicationSummary = getApplicationSummary (applicationCode, audienceTypeCode == null);
		AudienceType audienceType = getAudienceType (audienceTypeCode, audienceTypeCode == null);
		
		return getApplicationMessage (applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessage(String applicationCode, String audienceTypeCode, String sourceApplicationCode, String sourceMessageCode, int brandId,ApplicationException ae)throws ApplicationException, SystemException {
		ApplicationSummary applicationSummary = getApplicationSummary (applicationCode, audienceTypeCode == null);
		AudienceType audienceType = getAudienceType (audienceTypeCode, audienceTypeCode == null);
		
		return getApplicationMessage (applicationSummary, audienceType, sourceApplicationCode, sourceMessageCode, brandId,ae);
	}

	@Override
	public ApplicationMessageInfo getApplicationMessageWithSourceCode(String applicationCode, String audienceTypeCode, int brandId, long messageId) throws ApplicationException, SystemException{
		ApplicationSummary applicationSummary = getApplicationSummary (applicationCode, audienceTypeCode == null);
		AudienceType audienceType = getAudienceType (audienceTypeCode, audienceTypeCode == null);
		
		return getApplicationMessageWithSourceCode (applicationSummary, audienceType, brandId, messageId);
	}

	private void loadMessages() throws TelusException {
		ApplicationMessageInfo[] applicationMessageInfoList = referenceDataHelper.retrieveApplicationMessages();

		int applicationMessageInfoListSz = applicationMessageInfoList != null ? applicationMessageInfoList.length : 0;
		logger.info("Loaded " + applicationMessageInfoListSz + " application messages.");

		long oldMessageId = -1;
		int oldApplicationId = -1;
		int oldAudienceTypeId = -1;
		Hashtable<String, Hashtable> messagesByApp = null;
		Hashtable<String, Hashtable> messagesByAudience = null;
		Hashtable<String, ApplicationMessageInfo> messagesByBrand = null;

		for (int i = 0; i < applicationMessageInfoListSz; i++) {
			long newMessageId = applicationMessageInfoList[i].getId();
			if (newMessageId != oldMessageId) {
				if (messagesByApp != null) {
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
					messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
					messagesById.put(Long.toString(oldMessageId), messagesByApp);
				}
				messagesByApp = new Hashtable<String, Hashtable>();
				messagesByAudience = null;
				messagesByBrand = null;
				oldMessageId = newMessageId;
				oldApplicationId = -1;
			}

			int newApplicationId = applicationMessageInfoList[i].getApplicationId();
			if (newApplicationId != oldApplicationId) {
				if (messagesByAudience != null) {
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
					messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
				}
				messagesByAudience = new Hashtable<String, Hashtable>();
				messagesByBrand = null;
				oldApplicationId = newApplicationId;
				oldAudienceTypeId = -1;
			}

			int newAudienceTypeId = applicationMessageInfoList[i].getAudienceTypeId();
			if (newAudienceTypeId != oldAudienceTypeId) {
				if (messagesByBrand != null)
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);

				messagesByBrand = new Hashtable<String, ApplicationMessageInfo>();
				oldAudienceTypeId = newAudienceTypeId;
			}

			messagesByBrand.put(Integer.toString(applicationMessageInfoList[i].getBrandId()), applicationMessageInfoList[i]);
		}

		if (messagesByBrand != null && messagesByAudience != null && messagesByApp != null) {
			messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
			messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
			messagesById.put(Long.toString(oldMessageId), messagesByApp);
		}
	}

	private void loadMessageMappings() throws TelusException {

		ApplicationMessageMappingInfo[] applicationMessageMappingInfoList = referenceDataHelper.retrieveApplicationMessageMappings();

		int applicationMessageMappingInfoListSz = applicationMessageMappingInfoList != null ? applicationMessageMappingInfoList.length : 0;
		logger.info("Loaded " + applicationMessageMappingInfoListSz + " application message mappings.");
		
		String oldSourceAppCode = null;
		Hashtable<String, String> messageIdsByCode = null;

		for (int i = 0; i < applicationMessageMappingInfoListSz; i++) {

			// map the source codes to message IDs here for future reference
			sourceCodesByMessageIdsMap.put(Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()), 
					applicationMessageMappingInfoList[i].getSourceMessageCode());
			
			String newSourceAppCode = applicationMessageMappingInfoList[i].getSourceApplicationCode();
			if (newSourceAppCode != null && (oldSourceAppCode == null || !newSourceAppCode.equals(oldSourceAppCode))) {
				if (oldSourceAppCode != null)
					messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);

				messageIdsByCode = new Hashtable<String, String>();
				oldSourceAppCode = newSourceAppCode;			
			}

			messageIdsByCode.put(applicationMessageMappingInfoList[i].getSourceMessageCode(), Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()));
		}

		if (oldSourceAppCode != null)
			messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);
	}


	private ApplicationMessageInfo getMessageNotFoundApplicationMessage(String unknownMessageCode) {
		return 	new ApplicationMessageInfo(
						0, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(),
						ApplicationMessageType.MESSAGE_TYPE_ID_WARNING, Brand.BRAND_ID_ALL,
						"No message found for this code [" + unknownMessageCode + "].", "Aucun message est trouvé pour ce code [" + unknownMessageCode + "].");
	}
	
	private ApplicationSummary getApplicationSummary(String applicationCode, boolean useDefault) throws ApplicationException {
		ApplicationSummary applicationSummary = null;
		try {
			applicationSummary = referenceDataFacade.getApplicationSummary(applicationCode);
		}catch (TelusException e) {
			logger.warn("ApplicationSummary for applicationCode = [" + applicationCode + "] returned exception.  Using default ApplicationSummary.", e);
		}
			
		if (applicationSummary == null) {
			if (!useDefault) {
				throw new ApplicationException(SystemCodes.CMB_AMF_EJB, ErrorCodes.UNKNOWN_APPLICATION_CODE, "applicationCode doesn't correspond to any registered application: " + applicationCode);
			}else {
				return ApplicationSummary.DEFAULT;
			}
		}
		
		return applicationSummary;
	}
	
	private AudienceType getAudienceType(String audienceTypeCode, boolean useDefault) throws ApplicationException, SystemException {
		AudienceType audienceType = null;
		try {
			audienceType = referenceDataFacade.getAudienceType(audienceTypeCode);
		}catch (TelusException e) {
			throw new SystemException (SystemCodes.CMB_AMF_EJB, e.id, e.getMessage(), "", e);
		}

		if (audienceType == null) {
			if (!useDefault) {
				throw new ApplicationException(SystemCodes.CMB_AMF_EJB, ErrorCodes.UNKNOWN_AUDIENCE_TYPE, "audienceTypeCode doesn't correspond to any registered audience type: " + audienceType);
			}else {
				return AudienceType.DEFAULT;
			}
		}
		
		return audienceType;
	}
	

}
