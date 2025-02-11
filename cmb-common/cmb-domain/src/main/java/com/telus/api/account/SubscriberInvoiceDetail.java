package com.telus.api.account;

public interface SubscriberInvoiceDetail {

  /**
   * Returns one of the product_type_xxx constants
   * @return String
   */
  String getProductType();

  /**
   * Returns the unique ID of the subscriber
   * @return String
   */
  String getSubscriberId();

  /**
   * Returns the service order code (SOC) for subscriber price plan
   * @return String
   */
  String getPricePlan();

  /**
   * Returns Subscriber Name as shown on bill
   * @return String
   */
  String getBilledName();

  /**
   * Returns current charge amount
   * @return double
   */
  double getCurrentCharges();

  /**
   * Returns current credit amount
   * @return double
   */
  double getCurrentCredits();

  /**
   * Returns GST tax
   * @return double
   */
  double getGSTTaxAmount();

  /**
   * Returns PST tax
   * @return double
   */
  double getPSTTaxAmount();

  /**
   * Returns HST tax
   * @return double
   */
  double getHSTTaxAmount();

  /**
   * Returns roaming tax
   * @return double
   */
  double getRoamingTaxAmount();

  /**
   * Returns the total charge for one subscriber and one invoice
   * @return double
   */
  double getTotalCharge();
}
