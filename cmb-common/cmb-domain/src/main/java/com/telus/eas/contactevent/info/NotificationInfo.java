package com.telus.eas.contactevent.info;

import com.telus.api.contactevent.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.*;
import java.util.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class NotificationInfo extends Info implements Notification {

	static final long serialVersionUID = 1L;

	public static final int CONTACT_EVENT_TYPE_NOTIFICATION = 1;
	public static final int CONTACT_EVENT_MECHANISM_TYPE_ID_SMS = 1;
	public static final String CONTACT_EVENT_STATUS_SCHEDULED = "S";
	public static final char PLACE_HOLDER = '?';
	public static final long CLIENT_NOTIFICATION_EVENT_TYPE = 1;

	private int banId;
	private String subscriberNumber;
	private String productType;
	private String language;
	private Date deliveryDate;
	private long timeToLive;
	private int priority;
	private boolean validatingInputRequest;
	private boolean preventingDuplicate;
	private String[] contentParameters;
	private String application;
	private String user;
	private long contactEventId;
	private NotificationTypeInfo notificationTypeInfo;
	private String message;
	private long contactEventTypeId = CLIENT_NOTIFICATION_EVENT_TYPE;
	private String templateCode;

	public NotificationInfo() {	}

	public NotificationType getNotificationType() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void setNotificationType(NotificationType notificationType) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public NotificationTypeInfo getNotificationTypeInfo() {
		return notificationTypeInfo;
	}

	public void setNotificationTypeInfo(
			NotificationTypeInfo notificationTypeInfo) {
		this.notificationTypeInfo = notificationTypeInfo;
	}

	public int getBanId() {
		return banId;
	}

	public void setBanId(int banId) {
		this.banId = banId;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isValidatingInputRequest() {
		return validatingInputRequest;
	}

	public void setValidatingInputRequest(boolean validatingInputRequest) {
		this.validatingInputRequest = validatingInputRequest;
	}

	public boolean isPreventingDuplicate() {
		return preventingDuplicate;
	}

	public void setPreventingDuplicate(boolean preventingDuplicate) {
		this.preventingDuplicate = preventingDuplicate;
	}

	public String[] getContentParameters() {
		return contentParameters;
	}

	public void setContentParameters(String[] contentParameters) {
		this.contentParameters = contentParameters;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getContactEventId() {
		return contactEventId;
	}

	public void setContactEventId(long contactEventId) {
		this.contactEventId = contactEventId;
	}

	public String getNotificationTypeCode() {
		return notificationTypeInfo.getCode();
	}

	public String getOriginatingUser() {
		return notificationTypeInfo.getOriginatingUser();
	}

	public String getDeliveryReceiptRequired() {
		return notificationTypeInfo.getDeliveryReceiptRequired();
	}

	public String getBillable() {
		return notificationTypeInfo.getBillable();
	}

	public String getMessage() {
		return message;
	}

	public long getContactEventTypeId() {
		return contactEventTypeId;
	}

	public void setContactEventTypeId(long contactEventTypeId) {
		this.contactEventTypeId = contactEventTypeId;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setNotificationMessageTemplateInfo(NotificationMessageTemplateInfo templateInfo) {
		this.templateCode = templateInfo.getCode();
		if (Notification.LANGUAGE_ENGLISH.equalsIgnoreCase(language)) {
			message = assembleMessage(templateInfo.getMessageTemplate());
		} else if (Notification.LANGUAGE_FRENCH.equalsIgnoreCase(language)) {
			message = assembleMessage(templateInfo.getMessageTemplateFrench());
		} else {
			throw new UnsupportedOperationException("The specified language is not supported. Use either English (EN) or French (FR) only");
		}
	}

	private int countPlaceHolder(String messageTemplate) {
		char temp;
		int count = 0;
		for (int i = 0; i < messageTemplate.length(); i++) {
			temp = messageTemplate.charAt(i);
			if (PLACE_HOLDER == temp)
				count++;
		}

		return count;
	}

	private String assembleMessage(String messageTemplate) {

		if (messageTemplate == null || messageTemplate.trim().length() == 0) {
			throw new java.lang.IllegalArgumentException(
					"message template is not set");
		}

		int holderCount = countPlaceHolder(messageTemplate);

		if (contentParameters == null || contentParameters.length == 0) {
			if (holderCount > 0)
				throw new java.lang.IllegalStateException("Content Parameters are required, but not set yet");
			else
				return messageTemplate;
		}

		if (holderCount != contentParameters.length) {
			throw new java.lang.IndexOutOfBoundsException("Number of Content Parameters is wrong");
		}

		StringBuffer msg = new StringBuffer();

		// Check that template has more than a single place holder ( most of the
		// cases)
		if (!messageTemplate.equals("" + PLACE_HOLDER)) {

			StringTokenizer template = new StringTokenizer(messageTemplate, "?");
			int i = 0;
			while (template.hasMoreTokens()) {
				msg.append(template.nextToken());
				if (i < contentParameters.length) {
					if (contentParameters[i] == null) {
						throw new java.lang.IllegalArgumentException("Content Parameters [" + i + "] is null");
					}
					msg.append(contentParameters[i]);
					i++;
				}
			}
		}
		// In this case the template, is identical to a single holder, therefore
		// is safe to return the single parameter
		else {
			if (contentParameters[0] == null) {
				throw new java.lang.IllegalArgumentException("Content Parameter is null");
			}
			msg.append(contentParameters[0]);
		}

		return msg.toString();
	}

	public String toString() {

		StringBuffer string = new StringBuffer();

		string.append("\nNotificationInfo:");

		string.append("\t  banId=" + this.banId);
		string.append("\t  subscriberNumber=" + this.subscriberNumber);
		string.append("\t  productType=" + this.productType);
		string.append("\t  language=" + this.language);
		string.append("\t  deliveryDate=" + this.deliveryDate);
		string.append("\t  timeToLive=" + this.timeToLive);
		string.append("\t  priority=" + this.priority);
		string.append("\t  validatingInputRequest=" + this.validatingInputRequest);
		string.append("\t  preventingDuplicate=" + this.preventingDuplicate);

		string.append("\t  contentParemeters=[");
		if (this.contentParameters != null) {
			for (int i = 0; i < this.contentParameters.length; i++) {
				string.append(this.contentParameters[i]);
				if (i < (this.contentParameters.length - 1)) {
					string.append(',');
				}
			}
		}
		string.append("]");

		string.append("\t  message=" + this.message);

		string.append("\t  notificationType=" + (this.notificationTypeInfo != null ? this.notificationTypeInfo.getCode() : null));
		string.append("\t  application=" + this.application);
		string.append("\t  user=" + this.user);
		string.append("\t  contactEventId=" + this.contactEventId);
		string.append("\t  contactEventTypeId=" + this.contactEventTypeId);

		return string.toString();
	}

}