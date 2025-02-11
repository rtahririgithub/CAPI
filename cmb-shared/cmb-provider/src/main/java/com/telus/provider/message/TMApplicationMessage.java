package com.telus.provider.message;

import java.util.Locale;

import com.telus.api.message.ApplicationMessage;
import com.telus.api.message.ApplicationMessageType;
import com.telus.eas.message.info.ApplicationMessageInfo;

/**
 * @author Vladimir Tsitrin *
 * @version 1.0, 26-Dec-2005
 */
public final class TMApplicationMessage implements ApplicationMessage {
	
	private final ApplicationMessageInfo delegate;
	private final ApplicationMessageType messageType;

	public TMApplicationMessage(ApplicationMessageInfo delegate) {
		this.delegate = delegate;
		this.messageType = ApplicationMessageType.getApplicationMessageTypeById(delegate.getMessageTypeId());
	}

	public long getId() {
		return delegate.getId();
	}

	public String getCode() {
		return delegate.getCode();
	}

	public ApplicationMessageType getType() {
		return messageType;
	}

	public String getText(Locale language) {
		return delegate.getText(language);
	}

	public String getText(String language) {
		return delegate.getText(language);
	}

	public String toString() {
		return delegate.toString();
	}

	//-----------------------------------------------------------------------
	// Note:  The following methods are not exposed in the ApplicationMessage
	// interface.
	//-----------------------------------------------------------------------
	public int getAudienceTypeId() {
		return delegate.getAudienceTypeId();
	}

	public int getApplicationId() {
		return delegate.getApplicationId();
	}
	
	public int getBrandId() {
		return delegate.getBrandId();
	}
	
	public int getMessageTypeId() {
		return delegate.getMessageTypeId();
	}
}
