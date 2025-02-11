package com.telus.eas.message.info;

import java.util.Hashtable;
import java.util.Locale;

import com.telus.api.message.ApplicationMessage;
import com.telus.api.message.ApplicationMessageType;
import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 23-Jan-2006
 */
public final class ApplicationMessageInfo extends Info implements ApplicationMessage {

	static final long serialVersionUID = 1L;
	private static final long DUMMY_ID = -1;
	private final long id;
	private final String code;
	private final int applicationId;
	private final int audienceTypeId;
	private final int messageTypeId;
	private final int brandId;
	private Hashtable text;

	public ApplicationMessageInfo(long id, int applicationId, int audienceTypeId, int messageTypeId, int brandId) {
		super();
		this.id = id;
		this.code = String.valueOf(id);
		this.applicationId = applicationId;
		this.audienceTypeId = audienceTypeId;
		this.messageTypeId = messageTypeId;
		this.brandId = brandId;
	}

	public ApplicationMessageInfo(String code, int applicationId, int audienceTypeId, int messageTypeId, int brandId) {
		super();
		this.id = DUMMY_ID;
		this.code = code;
		this.applicationId = applicationId;
		this.audienceTypeId = audienceTypeId;
		this.messageTypeId = messageTypeId;
		this.brandId = brandId;
	}

	public ApplicationMessageInfo(long id, int applicationId, int audienceTypeId, int messageTypeId, int brandId, String textEn, String textFr) {
		super();
		this.id = id;
		this.code = String.valueOf(id);
		this.applicationId = applicationId;
		this.audienceTypeId = audienceTypeId;
		this.messageTypeId = messageTypeId;
		this.brandId = brandId;
		setText(Locale.ENGLISH.getLanguage(), textEn);
		setText(Locale.FRENCH.getLanguage(), textFr);
	}	

	public ApplicationMessageInfo(long id, String code, int applicationId, int audienceTypeId, int messageTypeId, int brandId, String textEn, String textFr) {
		super();
		this.id = id;
		this.code = code;
		this.applicationId = applicationId;
		this.audienceTypeId = audienceTypeId;
		this.messageTypeId = messageTypeId;
		this.brandId = brandId;
		setText(Locale.ENGLISH.getLanguage(), textEn);
		setText(Locale.FRENCH.getLanguage(), textFr);
	}

	public long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public int getAudienceTypeId() {
		return audienceTypeId;
	}

	public int getMessageTypeId() {
		return messageTypeId;
	}

	public int getBrandId() {
		return brandId;
	}

	public ApplicationMessageType getType() {
		throw new UnsupportedOperationException("Method not implemented here.");
	}

	public String getText(Locale language) {
		return language != null ? getText(language.getLanguage()) : getText(Locale.ENGLISH.getLanguage());
	}

	public String getText(String language) {
		String msgText = text != null ? (String) text.get(language) : "No message text is provided for Message ID [" + getId() + "]";

		if (msgText == null && !language.equals(Locale.ENGLISH.getLanguage()))
			msgText = (String) text.get(Locale.ENGLISH.getLanguage());

		if (msgText == null || msgText.equals(""))
			msgText = "No message text is provided for Message ID [" + getId() + "]";

		return msgText;
	}

	public void setText(String language, String text) {
		if (this.text == null)
			this.text = new Hashtable();

		this.text.put(language, text != null ? text : "");
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("ApplicationMessageInfo:{\n");
		s.append("    id=[").append(id).append("]\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    applicationId=[").append(applicationId).append("]\n");
		s.append("    audienceTypeId=[").append(audienceTypeId).append("]\n");
		s.append("    messageTypeId=[").append(messageTypeId).append("]\n");
		s.append("    brandId=[").append(brandId).append("]\n");
		s.append("    text=[").append(text).append("]\n");
		s.append("}");

		return s.toString();
	}
}
