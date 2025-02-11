package com.telus.eas.subscriber.info;

import com.telus.api.account.VoiceToTextOptions;
import com.telus.eas.framework.info.Info;

/**
 * Title:        VoiceToTextOptionsInfo<p>
 * Description:  The VoiceToTextOptionsInfo class holds the attributes for a list of VoiceToText options.<p>
 * Copyright:    Copyright (c) 2007<p>
 * Company:      Telus Mobility Inc<p>
 * @author A. Pereira
 * @version 1.0
 */
public class VoiceToTextOptionsInfo extends Info implements VoiceToTextOptions {
  public final static String SMS_OR_MMS_DELIVERY_KEY = "VTTHANDSET";
  public final static String EMAIL_DELIVERY_KEY = "VTTEMAIL";
  public final static String EMAIL_NAME_KEY = "VTTEMAILNAME";
  public final static String EMAIL_DOMAIN_KEY = "VTTEMAILDOM";
  public final static String VOICE_FILE_KEY = "VTTWAV";
  public final static String ROLLING_VM_KEY = "VTTROLL";
	
  boolean smsOrMmsDelivery;
  boolean emailDelivery;
  String emailName = "N";
  String emailDomain;
  boolean voiceFileAttached;
  boolean rollingVoiceMail;

  public boolean isSMSORMMSDelivery() {
    return smsOrMmsDelivery;
  }
  
  public boolean isEmailDelivery() {
	return emailDelivery;
  }
  
  public String  getEmailName() {
	return emailName;
  }
  
  public String  getEmailDomain() {
	return emailDomain;
  }
  
  public boolean  isVoiceFileAttached() {
	return voiceFileAttached;  
  }
  
  public boolean isRollingVoiceMail() {
	return rollingVoiceMail;
  }

  public void setSMSORMMSDelivery(boolean smsOrMmsDelivery) {
	this.smsOrMmsDelivery = smsOrMmsDelivery;
  }
  
  public void setEmailDelivery(boolean emailDelivery) {
	this.emailDelivery = emailDelivery;
  }
  
  public void setEmailName(String emailName) {
    if (emailName == null)
      this.emailName = "";
    else
	  this.emailName = emailName;
  }
  
  public void setEmailDomain(String emailDomain) {
	if (emailDomain == null)
	  this.emailDomain = "";
	else
	  this.emailDomain = emailDomain;
  }
  
  public void setVoiceFileAttached(boolean voiceFileAttached) {
	this.voiceFileAttached = voiceFileAttached;
  }
  
  public void setRollingVoiceMail(boolean rollingVoiceMail) {
	this.rollingVoiceMail = rollingVoiceMail;
  }

  
	/**
	 * toString method: creates a String representation of the object
	 * @return the String representation
	 * 
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("VoiceToTextOptionsInfo[");
		buffer.append("smsOrRmmsDelivery=").append(smsOrMmsDelivery);
		buffer.append(" emailDelivery=").append(emailDelivery);
		buffer.append(" emailName='").append(emailName).append("'");
		buffer.append(" emailDomain='").append(emailDomain).append("'");
		buffer.append(" voiceFileAttached=").append(voiceFileAttached);
		buffer.append(" rollingVoiceMail=").append(rollingVoiceMail);
		buffer.append("]");
		return buffer.toString();
	}
}
