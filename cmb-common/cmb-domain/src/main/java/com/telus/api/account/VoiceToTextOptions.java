package com.telus.api.account;

public interface VoiceToTextOptions {

  /**
   * Returns the delivery mode flag
   * @return boolean
   */
  boolean isSMSORMMSDelivery();

  /**
   * Returns the email delivery mode
   * @return boolean
   */
  boolean isEmailDelivery();
  
  /**
   * Returns the subscriber email name
   * @return String
   */
  String  getEmailName();
  
  /**
   * Returns the subscriber email domain
   * @return String
   */
  String  getEmailDomain();
  
  /**
   * Returns the attached voice file
   * @return boolean
   */
  boolean  isVoiceFileAttached();
  
  /**
   * Returns the Rolling Voice mail
   * @return boolean
   */
  boolean isRollingVoiceMail();

  /**
   * Set to SMS or RMMS delivery option
   * 
   */
  void setSMSORMMSDelivery(boolean smsOrMmsDelivery);
  
  /**
   * Set the email delivery mode
   * 
   */
  void setEmailDelivery(boolean emailDelivery);
  
  /**
   * Set the email name
   * 
   */
  void setEmailName(String emailName);
  
  /**
   * Set the email domain
   * 
   */
  void setEmailDomain(String emailDomain);
  
  /**
   * Set the attached voice file
   * 
   */
  void setVoiceFileAttached(boolean voiceFileAttached);
  
  /**
   * Set the rolling voice mail.
   * 
   */
  void setRollingVoiceMail(boolean rollingVoiceMail);

}




