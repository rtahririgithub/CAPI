package com.telus.cmb.subscriber.utilities;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.api.message.ApplicationMessage;
import com.telus.api.message.ApplicationMessageType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;

/**
 * @author x168277
 * @version 1.0, 20-Jan-2006
 */
public final class ApplicationMessageManager {
	
	private static Logger logger = LoggerFactory.getLogger(ApplicationMessageManager.class);

	// application messages mapped by their message IDs.
	private final Map<String, Map<String, Map<String, Map<String, ApplicationMessage>>>> messagesById = new HashMap<String, Map<String, Map<String, Map<String, ApplicationMessage>>>>();

	// application message IDs grouped by source application codes.
	private final Map<String, Map<String, String>> messageIdsBySourceApp = new HashMap<String, Map<String, String>>();
	
	// source application codes keyed by their corresponding application message ID.
	private final Map<String, String> sourceCodesByMessageIdsMap = new HashMap<String, String>();

	// remote interface of the ReferenceDataHelperEJB
	private final ReferenceDataHelper referenceDataHelper;

	public ApplicationMessageManager(ReferenceDataHelper referenceDataHelper) throws Throwable {
		this.referenceDataHelper = referenceDataHelper;
		loadMessages();
		loadMessageMappings();
	}

	private synchronized void loadMessages() throws Throwable {

		ApplicationMessageInfo[] applicationMessageInfoList = referenceDataHelper.retrieveApplicationMessages();

		int applicationMessageInfoListSz = applicationMessageInfoList != null ? applicationMessageInfoList.length : 0;
		logger.debug("Loaded {} application messages.", applicationMessageInfoListSz);

		long oldMessageId = -1;
		int oldApplicationId = -1;
		int oldAudienceTypeId = -1;
		Map<String, Map<String, Map<String, ApplicationMessage>>> messagesByApp = null;
		Map<String, Map<String, ApplicationMessage>> messagesByAudience = null;
		Map<String, ApplicationMessage> messagesByBrand = null;

		for (int i = 0; i < applicationMessageInfoListSz; i++) {
			long newMessageId = applicationMessageInfoList[i].getId();
			if (newMessageId != oldMessageId) {
				if (messagesByApp != null) {
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);
					messagesByApp.put(Integer.toString(oldApplicationId), messagesByAudience);
					messagesById.put(Long.toString(oldMessageId), messagesByApp);
				}
				messagesByApp = new HashMap<String, Map<String, Map<String, ApplicationMessage>>>();
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
				messagesByAudience = new HashMap<String, Map<String, ApplicationMessage>>();
				messagesByBrand = null;
				oldApplicationId = newApplicationId;
				oldAudienceTypeId = -1;
			}

			int newAudienceTypeId = applicationMessageInfoList[i].getAudienceTypeId();
			if (newAudienceTypeId != oldAudienceTypeId) {
				if (messagesByBrand != null)
					messagesByAudience.put(Integer.toString(oldAudienceTypeId), messagesByBrand);

				messagesByBrand = new HashMap<String, ApplicationMessage>();
				oldAudienceTypeId = newAudienceTypeId;
			}

			messagesByBrand.put(Integer.toString(applicationMessageInfoList[i].getBrandId()), new ApplicationMessageImpl(applicationMessageInfoList[i]));
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
		logger.debug("Loaded {} application message mappings.", applicationMessageMappingInfoListSz);
		
		String oldSourceAppCode = null;
		Map<String, String> messageIdsByCode = null;

		for (int i = 0; i < applicationMessageMappingInfoListSz; i++) {

			// map the source codes to message IDs here for future reference
			sourceCodesByMessageIdsMap.put(Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()), 
					applicationMessageMappingInfoList[i].getSourceMessageCode());
			
			String newSourceAppCode = applicationMessageMappingInfoList[i].getSourceApplicationCode();
			if (newSourceAppCode != null && (oldSourceAppCode == null || !newSourceAppCode.equals(oldSourceAppCode))) {
				if (oldSourceAppCode != null)
					messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);

				messageIdsByCode = new HashMap<String, String>();
				oldSourceAppCode = newSourceAppCode;			
			}

			messageIdsByCode.put(applicationMessageMappingInfoList[i].getSourceMessageCode(), Long.toString(applicationMessageMappingInfoList[i].getTargetMessageId()));
		}

		if (oldSourceAppCode != null)
			messageIdsBySourceApp.put(oldSourceAppCode, messageIdsByCode);
	}

	/**[Naresh Annabathula] , 26-Apr-2016  , commenting out below two methods as there is no references found,
	 * assume it was just copy over from provider when we have done refactor.
	 *
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode, int brandId) {
		
		if (application == null || audienceType == null || sourceApplicationCode == null || sourceMessageCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		Map<String, String> messagesBySourceCode = messageIdsBySourceApp.get(sourceApplicationCode);
		if (messagesBySourceCode == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		String messageId = (String) messagesBySourceCode.get(sourceMessageCode);
		if (messageId == null)
			return getMessageNotFoundApplicationMessage(sourceMessageCode);

		return getApplicationMessage(application, audienceType, brandId, Long.parseLong(messageId));
	}

	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, String sourceApplicationCode, String sourceMessageCode) {
		return getApplicationMessage(application, audienceType, sourceApplicationCode, sourceMessageCode, Brand.BRAND_ID_ALL);
	}*/

	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {

		if (application == null || audienceType == null || brandId <= 0 || messageId <= 0)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Map<String, Map<String, Map<String, ApplicationMessage>>> messagesByApp = messagesById.get(Long.toString(messageId));
		if (messagesByApp == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Map<String, Map<String, ApplicationMessage>> messagesByAudience = messagesByApp.get(Integer.toString(application.getId()));
		if (messagesByAudience == null)
			messagesByAudience = messagesByApp.get(Integer.toString(ApplicationSummary.DEFAULT.getId()));

		if (messagesByAudience == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		Map<String, ApplicationMessage> messagesByBrand = messagesByAudience.get(Integer.toString(audienceType.getId()));
		if (messagesByBrand == null)
			messagesByBrand = messagesByAudience.get(Integer.toString(AudienceType.DEFAULT.getId()));

		if (messagesByBrand == null)
			return getMessageNotFoundApplicationMessage(Long.toString(messageId));

		ApplicationMessage applicationMessage = (ApplicationMessage) messagesByBrand.get(Integer.toString(brandId));
		if (applicationMessage == null)
			applicationMessage = (ApplicationMessage) messagesByBrand.get(Integer.toString(Brand.BRAND_ID_ALL));

		return applicationMessage != null ? applicationMessage : getMessageNotFoundApplicationMessage(Long.toString(messageId));
	}

	

	public ApplicationMessage getApplicationMessage(String code, String textEn, String textFr) {
		ApplicationMessageInfo info = new ApplicationMessageInfo(code, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(), ApplicationMessageType.MESSAGE_TYPE_ID_ERROR, Brand.BRAND_ID_ALL);
		info.setText(Locale.ENGLISH.getLanguage(), textEn);
		info.setText(Locale.FRENCH.getLanguage(), textFr);

		return new ApplicationMessageImpl(info);
	}
	
	/**[Naresh Annabathula] 26-Apr-2016 , commenting out below two methods as there is no references found,
	 * assume it was just copy over from provider when we have done refactor.
	 
	// retrieves the ApplicationMessage populated with the source application code, if there is one
	public ApplicationMessage getApplicationMessageWithSourceCode(ApplicationSummary application, AudienceType audienceType, int brandId, long messageId) {
		ApplicationMessage message = getApplicationMessage(application, audienceType, brandId, messageId);
		String sourceCode = (String)sourceCodesByMessageIdsMap.get(message.getCode());
		return decorate(message, sourceCode);
	}
	
	public ApplicationMessage getApplicationMessage(ApplicationSummary application, AudienceType audienceType, long messageId) {
		return getApplicationMessage(application, audienceType, Brand.BRAND_ID_ALL, messageId);
	}
	*/

	private ApplicationMessage getMessageNotFoundApplicationMessage(String unknownMessageCode) {
		return new ApplicationMessageImpl(
				new ApplicationMessageInfo(
						0, ApplicationSummary.DEFAULT.getId(), AudienceType.DEFAULT.getId(),
						ApplicationMessageType.MESSAGE_TYPE_ID_WARNING, Brand.BRAND_ID_ALL,
						"No message found for this code [" + unknownMessageCode + "].", "Aucun message est trouvé pour ce code [" + unknownMessageCode + "]."));
	}
	
	// returns a new ApplicationMessage object populated with the source application code, if there is one
	private ApplicationMessage decorate(ApplicationMessage message, String sourceCode) {
		ApplicationMessageImpl tmMessage = (ApplicationMessageImpl)message;
		return new ApplicationMessageImpl(new ApplicationMessageInfo(tmMessage.getId(), sourceCode != null ? sourceCode : tmMessage.getCode(), 
				tmMessage.getApplicationId(), tmMessage.getAudienceTypeId(), tmMessage.getMessageTypeId(), tmMessage.getBrandId(), 
				tmMessage.getText(Locale.ENGLISH), tmMessage.getText(Locale.FRENCH)));
	}
}