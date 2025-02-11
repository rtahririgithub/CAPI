package com.telus.api.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

@Deprecated
public interface PCSFidoSubscriber extends PCSSubscriber {

  public static final int PREPAID = 1;
  public static final int POSTPAID = 2;
  /**
   * Set equipment serial number, usually it's the mule equipment serial number
   * @param serialNumber String
   */
  void setFidoSerialNumber(String serialNumber);

  /**
   * Set SIM card number
   * @param simCardNumber String
   */
  void setFidoSimCard(String simCardNumber);

  /**
   * Set phone number
   * @param phoneNumber String
   */
  void setFidoPhoneNumber(String phoneNumber);

  /**
   * Set account number/ID
   * @param accountId String
   */
  void setFidoAccountId(String accountId);

  /**
   * Set first name
   * @param firstName String
   */
  void setFidoFirstName(String firstName);

  /**
   * Set last name
   * @param lastName String
   */
  void setFidoLastName(String lastName);

  /**
   * Set account type, prepaid, postpaid, etc.
   * @param prepaid int
   */
  void setFidoAccountType(int prepaid);

  /**
   * Set Fido Phone Number HoldDays
   * @param phoneNumberHoldDays int
   */
  void setFidoPhoneNumberHoldDays(int phoneNumberHoldDays);
}
