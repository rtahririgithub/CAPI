/*  */

package com.telus.api.contactevent;

import java.util.Date;
import com.telus.api.reference.NotificationType;

/**
 *
 * Notification is an outgoing contact event for the purpose of sending information
 * to a Telus Mobility subscriber.
 * The subscribers is identified by the billing ID.
 *
 * Pre-Conditions
 * - Subscription is has been arranged before notification can be called.
 * - Preferences is pre-defined only as language code.
 * - Content is defined
 * - Subcriber ID is defined
 *
 * Post-Conditions
 * - Notification Request with details has been created in the Contact Event database
 *
 *
 * @see <A href="http://hopper:8080/docs/Notification/ContactDatabase">Contact Event Database Description</A>
 * @version 2.1
 */

 // TODO to offer this interface to both batch and online applications
public interface Notification {
  // Contact Event Database Reference data
  public static final int PRIORITY_SMS_NORMAL = 1;
  public static final int PRIORITY_SMS_HIGH = 2;
  public static final String LANGUAGE_ENGLISH = "EN";
  public static final String LANGUAGE_FRENCH = "FR";
  public static final String PRODUCT_TYPE_PCS = "C";
  public static final String PRODUCT_TYPE_MIKE = "I";

  // --------------------- SubscriberIdentify ------------------------

  /**
   * Knowbility Account Number associated to the subscriber
   */
  int getBanId();
  void setBanId(int ban);

  /**
   * Knowbility Subscriber Number
   */
  String getSubscriberNumber();
  void setSubscriberNumber(String subscriberNumber);

  /**
   * Knowbility Product Type associated to the subscriber
   */
  String getProductType();
  void setProductType(String productType);

  /**
   * Language code which is a preference attribute associated to the subscriber
   */
  String getLanguage();
  void setLanguage(String language);


  // --------------------- Content -----------------------------------

  /**
   * The Message content is expresed as a list of parameter values.
   * For instance {"$99.99","01-JUN-2004","Cheque"}
   * Since the NotificationType contains the template, the provider will
   * assemble the final message content by merging the template with the
   * given parameter values.   The provider will also use the language code to
   * select the right template.
   */
  String[] getContentParameters();
  void setContentParameters(String[] contentParameters);

  /**
   * Type of Notification is synonym of Contact Content Type, which classifies
   * Notificationss as payment notification, airtime time summary notification, etc.
   *
   * @return NotificationType reference object
   * @link aggregationByValue
   */

  NotificationType getNotificationType();
  void setNotificationType(NotificationType notificationType);


  // --------------------- Rules -------------------------------------

  /**
   * Date when the notification is requested to be delivered.
   */
  Date getDeliveryDate();
  void setDeliveryDate(Date deliveryDate);

  /**
   * Time in seconds that a requested notification can live.
   * After this time expires, the notification status will be updated to
   * avoid subsequent deliver attemtps.
   */
  long getTimeToLive();
  void setTimeToLive(long timeTolive);

  /**
   * Priority is not currently supported, but eventually will have differente numeric
   * values to represent normal, high or low priorities.
   */
  int getPriority();
  void setPriority(int priority);

  /**
   * Boolean flag to enable validation of subscriber or device before
   * creating the notification
   */
  boolean isValidatingInputRequest();
  void setValidatingInputRequest(boolean validatingInputRequest);

  /**
   * Indicates weather notification duplicate is prevented or not.
   * When true, a duplicate notification search is done before creating
   * the notification in the database.
   * Default is false.
   */
  boolean isPreventingDuplicate();
  void setPreventingDuplicate(boolean preventingDuplicate);

  /**
   * Return application name.
   */
  String getApplication();

}
