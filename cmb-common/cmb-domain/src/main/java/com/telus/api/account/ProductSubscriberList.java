package com.telus.api.account;

public interface ProductSubscriberList {
  String getProductType();
  
  /**
   * @deprecated Method has been replaced by {@link #getActiveSubscriberIdentifiers()}. Signature will be completely removed in Feb 2011 release
   * @return String[]
   */
  String[] getActiveSubscribers();
  /**
   * @deprecated Method has been replaced by {@link #getCancelledSubscriberIdentifiers()}. Signature will be completely removed in Feb 2011 release
   * @return String[]
   */
  String[] getCancelledSubscribers();
  /**
   * @deprecated Method has been replaced by {@link #getReservedSubscriberIdentifiers()}. Signature will be completely removed in Feb 2011 release
   * @return String[]
   */
  String[] getReservedSubscribers();
  /**
   * @deprecated Method has been replaced by {@link #getSuspendedSubscriberIdentifiers()}. Signature will be completely removed in Feb 2011 release
   * @return String[]
   */
  String[] getSuspendedSubscribers();
  
  /**
   * 
   * @return {@link SubscriberIdentifier} object for all subscriber in active status
   */
  SubscriberIdentifier[] getActiveSubscriberIdentifiers();
  
  /**
   * 
   * @return {@link SubscriberIdentifier} object for all subscriber in cancelled status
   */
  SubscriberIdentifier[] getCancelledSubscriberIdentifiers();
  
  /**
   * 
   * @return {@link SubscriberIdentifier} object for all subscriber in reserved status
   */
  SubscriberIdentifier[] getReservedSubscriberIdentifiers();
  
  /**
   * 
   * @return {@link SubscriberIdentifier} object for all subscriber in suspended status
   */
  SubscriberIdentifier[] getSuspendedSubscriberIdentifiers();
}
