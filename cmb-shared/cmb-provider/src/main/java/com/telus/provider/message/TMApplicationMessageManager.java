package com.telus.provider.message;

import java.util.Hashtable;
import java.util.Locale;

import com.telus.api.ApplicationException;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.message.ApplicationMessageType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;
import com.telus.provider.util.Logger;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 20-Jan-2006
 */
public final class TMApplicationMessageManager {

	// application messages mapped by their message IDs.
	private final Hashtable messagesById = new Hashtable();

	// application message IDs grouped by source application codes.
	private final Hashtable messageIdsBySourceApp = new Hashtable();
	
	// source application codes keyed by their corresponding application message ID.
	private final Hashtable sourceCodesByMessageIdsMap = new Hashtable();

	// remote interface of the ReferenceDataHelperEJB
	private final ReferenceDataHelper referenceDataHelper;

	public TMApplicationMessageManager(ReferenceDataHelper referenceDataHelper) throws Throwable {
		this.referenceDataHelper = referenceDataHelper;
		loadMessages();
		loadMessageMappings();
	}

	private synchronized void loadMessages() throws Throwable {

		ApplicationMessageInfo[] applicationMessageInfoList = referenceDataHelper.retrieveApplicationMessages();

		int applicationMessageInfoListSz = applicationMessageInfoList != null ? applicationMessageInfoList.length : 0;
		Logger.debug("Loaded " + applicationMessageInfoListSz + " application messages.");

		long oldMessageId = -1;
		int oldApplicationId = -1;
		int oldAudienceTypeId = -1;
		Hashtable messagesByApp = null;
		Hashtable messagesByAudience = null;
		Hashtable messagesByBrand = null;

		for (int i = 0; i < applicationMessageInfoListSz; i++) {
			long newMessageId = applicationMessageInfoList[i].getId();
			if (newMessageId != oldMessageId) {
				if (messagesByApp != null) {
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
					messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
					messagesById.put(Long.toString(oldMessageId), messagesByApp);
				}
				messagesByApp = new Hashtable();
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
				messagesByAudience = new Hashtable();
				messagesByBrand = null;
				oldApplicationId = newApplicationId;
				oldAudienceTypeId = -1;
			}

			int newAudienceTypeId = applicationMessageInfoList[i].getAudienceTypeId();
			if (newAudienceTypeId != oldAudienceTypeId) {
				if (messagesByBrand != null)
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);

				messagesByBrand = new Hashtable();
				oldAudienceTypeId = newAudienceTypeId;
			}

			messagesByBrand.put(Integer.toString(applicationMessageInfoList[i].getBrandId()), new TMApplicationMessage(applicationMessageInfoList[i]));
		}

		if (messagesByBrand != null && messagesByAudience != null && messagesByApp != null) {
			messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
			messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
			messagesById.put(Long.toString(oldMessageId), messagesByApp);
		}
	}

	private synchronized void loadMessageMappings() throws Throwable {

		ApplicationMessageMappingInfo[] applicationMessageMappingInfoList = referenceDataHelper.retrieveApplicationMessageMappings();

		int applicationMessageMappingInfoListSz = applicationMessageMappingInfoList != null ? applicationMessageMappingInfoList.length : 0;
		Logger.debug("Loaded " + applicationMessageMappingInfoListSz + " application message mappings.");
		
		String oldSourceAppCode = null;
		Hashtable messageIdsByCode = null;

		for (int i = 0; i < applicationMessageMappingInfoListSz; i++) {

			// map the source codes to message IDs here for future reference
			sourceCodesByMessageIdsMap.put(Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()), 
					applicationMessageMappingInfoList[i].getSourceMessageCode());
			
			String newSourceAppCode = applicationMessageMappingInfoList[i].getSourceApplicationCode();
			if (newSourceAppCode != null && (oldSourceAppCode == null || !newSourceAppCode.equals(oldSourceAppCode))) {
				if (oldSourceAppCode != null)
					messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);

				messageIdsByCode = new Hashtable();
				oldSourceAppCode = newSourceAppCode;			
			}

			messageIdsByCode.put(applicationMessageMappingInfoList[i].getSourceMessageCode(), Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()));
		}

		if (oldSourceAppCode != null)
			messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);
	}

	// called by TMProvider getApplicationMessage methods..
	
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId) {
		
		if (application == null || audienceType == null || sourceApplicationCode == null || sourceMessageCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		Hashtable messagesBySourceCode = (Hashtable) messageIdsBySourceApp.get(sourceApplicationCode);
		if (messagesBySourceCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		String messageId = (String) messagesBySourceCode.get(sourceMessageCode);
		if (messageId == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		return getApplicationMessage(application, audienceType, brandId, Long.parseLong(messageId));
	}

	
	/**
	 * [Naresh Annabathula ] - 25-April -2016 , WNP came up with requirements to return the WNP thrown error messages if error message not found in EAS DB,
	    we added below two methods to map the WNP returned error instead of returning our own custom messages..  ie "not found message one".
	 */
   public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId, ApplicationException ae) {
		
	if (application == null || audienceType == null || sourceApplicationCode == null || sourceMessageCode == null)
		return wrapApplicationMessageFromApplicationException(ae,sourceMessageCode);

	Hashtable messagesBySourceCode = (Hashtable) messageIdsBySourceApp.get(sourceApplicationCode);
	if (messagesBySourceCode == null)
		return wrapApplicationMessageFromApplicationException(ae,sourceMessageCode);

	String messageId = (String) messagesBySourceCode.get(sourceMessageCode);
	if (messageId == null)
		return wrapApplicationMessageFromApplicationException(ae,sourceMessageCode);

	return getApplicationMessage(application, audienceType, brandId, Long.parseLong(messageId));
}


	private ApplicationMessage wrapApplicationMessageFromApplicationException(ApplicationException ae, String sourceMessageCode) {

		ApplicationMessageInfo info = new ApplicationMessageInfo(
				sourceMessageCode, ApplicationSummary.DEFAULT.getId(),AudienceType.DEFAULT.getId(),
				ApplicationMessageType.MESSAGE_TYPE_ID_ERROR,Brand.BRAND_ID_ALL);

		info.setText(Locale.ENGLISH.getLanguage(), ae.getErrorMessage());
		info.setText(Locale.FRENCH.getLanguage(), ae.getErrorMessageFr());
		
		return info;
	}
	
	/** {Naresh Annabathula 25-April -2016}  There is  no references found for below method - so commented out below one..
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode) {
		return getApplicationMessage(application, audienceType, sourceApplicationCode, sourceMessageCode, Brand.BRAND_ID_ALL);
	}
	 	*/
	
	// below method being called by TMProvider getApplicationMessage method and few local methods..
	
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {

		if (application == null || audienceType == null || brandId <= 0 || messageId <= 0)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable messagesByApp = (Hashtable) messagesById.get(Long.toString(messageId));
		if (messagesByApp == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable messagesByAudience = (Hashtable) messagesByApp.get(Integer.toString(application.getId()));
		if (messagesByAudience == null)
			messagesByAudience = (Hashtable) messagesByApp.get(Integer.toString(ApplicationSummary.DEFAULT.getId()));

		if (messagesByAudience == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Hashtable messagesByBrand = (Hashtable) messagesByAudience.get(Integer.toString(audienceType.getId()));
		if (messagesByBrand == null)
			messagesByBrand = (Hashtable) messagesByAudience.get(Integer.toString(AudienceType.DEFAULT.getId()));

		if (messagesByBrand == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		ApplicationMessage applicationMessage = (ApplicationMessage) messagesByBrand.get(Integer.toString(brandId));
		if (applicationMessage == null)
			applicationMessage = (ApplicationMessage) messagesByBrand.get(Integer.toString(Brand.BRAND_ID_ALL));

		return applicationMessage != null ? applicationMessage : getMessageNotFoundApplicationMessage(Long.toString(messageId));
	}

	/** {Naresh Annabathula 25-April -2016}  There is  any references found for below method - so commented out below one..
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, long messageId) {
		return getApplicationMessage(application, audienceType, Brand.BRAND_ID_ALL, messageId);
	} */

	// below method being called by TMProvder getApplicationMessage methods..
	
	public ApplicationMessage getApplicationMessage(String code, String textEn, String textFr) {
		ApplicationMessageInfo info = new ApplicationMessageInfo(code, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(), ApplicationMessageType.MESSAGE_TYPE_ID_ERROR, Brand.BRAND_ID_ALL);
		info.setText(Locale.ENGLISH.getLanguage(), textEn);
		info.setText(Locale.FRENCH.getLanguage(), textFr);

		return new TMApplicationMessage(info);
	}
	
	// below method being called by TMProvder getApplicationMessage methods..
	
	// retrieves the ApplicationMessage populated with the source application code, if there is one
	public ApplicationMessage getApplicationMessageWithSourceCode(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {
		ApplicationMessage message = getApplicationMessage(application, audienceType, brandId, messageId);
		String sourceCode = (String)sourceCodesByMessageIdsMap.get(message.getCode());
		return decorate(message, sourceCode);
	}

	private ApplicationMessage getMessageNotFoundApplicationMessage(String unknownMessageCode) {
		return new TMApplicationMessage(
				new ApplicationMessageInfo(
						0, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(),
						ApplicationMessageType.MESSAGE_TYPE_ID_WARNING, Brand.BRAND_ID_ALL,
						"No message found for this code [" + unknownMessageCode + "].", "Aucun message est trouvé pour ce code [" + unknownMessageCode + "]."));
	}
	
	// returns a new ApplicationMessage object populated with the source application code, if there is one
	private ApplicationMessage decorate(ApplicationMessage message, String sourceCode) {
		TMApplicationMessage tmMessage = (TMApplicationMessage)message;
		return new TMApplicationMessage(new ApplicationMessageInfo(tmMessage.getId(), sourceCode != null ? sourceCode : tmMessage.getCode(), 
				tmMessage.getApplicationId(), tmMessage.getAudienceTypeId(), tmMessage.getMessageTypeId(), tmMessage.getBrandId(), 
				tmMessage.getText(Locale.ENGLISH), tmMessage.getText(Locale.FRENCH)));
	}
}