package com.telus.api.reference;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/**
 * Type of notification
 * Examples:
 * - Credit Card Expiry  SMS Notification
 * - Usage Summary Notification service using SMS
 *
 * @see <A href="http://hopper:8080/docs/Notification/ContactDatabase">Contact Event Database Description</A>
 */
public interface NotificationType extends Reference {

  public static final String NOTIFICATION_TYPE_PAYMENT_SUCCESSFUL = "1";
  public static final String NOTIFICATION_TYPE_PAYMENT_WITH_BALANCE = "2";
  public static final String NOTIFICATION_TYPE_PAYMENT_FAILED = "3";
  public static final String NOTIFICATION_TYPE_USAGE_SUMMARY = "4";
  public static final String NOTIFICATION_TYPE_CREDIT_CARD_EXPIRY = "5";
  public static final String NOTIFICATION_TYPE_FREE_INFO = "17";
  public static final String NOTIFICATION_TYPE_INTERBRAND_PORT_SUCCESS = "213";
  public static final String RLH_SOC_ACTIVE = "RLH_SOC_ACTIVE";
  public static final String RLH_SOC_EXPIRE = "RLH_SOC_EXPIRE";
  public static final String RLH_SOC_BEFORE_EXP = "RLH_SOC_BEFORE_EXP";
  
}
