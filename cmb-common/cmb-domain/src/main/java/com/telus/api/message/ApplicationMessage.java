package com.telus.api.message;

import java.util.Locale;

/**
 * This interface defines the application message.
 *
 * @author Vladimir Tsitrin
 * @version 1.0, 12/22/05
 * @see ApplicationMessageType
 * @since 2.863.0.0
 */
public interface ApplicationMessage {
  /**
   * @return Application message ID.
   */
  long getId();

  /**
   * @return Application message code that is just a String representation of the message ID.
   */
  String getCode();

  /**
   * @return Application message type.
   * @see ApplicationMessageType
   */
  ApplicationMessageType getType();

  /**
   * This method returns message text in the language specified in <tt>language</tt>.
   *
   * @param language Instance of <tt>Locale</tt>.   
   * @return Message text.
   */
  String getText(Locale language);

  /**
   * This method returns message text in the language specified in <tt>language</tt>.
   *
   * @param language Language code that should be a valid ISO Language Code (http://ftp.ics.uci.edu/pub/ietf/http/related/iso639.txt).
   * @return Message text.
   */
  String getText(String language);
}
