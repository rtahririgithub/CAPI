package com.telus.api.account;

/**
 * CollectionHistory
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public interface CollectionHistory {

  /**
   * Returns CollectionStep
   * @return CollectionStep
   */
  CollectionStep getCollectionStep();

  /**
   * Returns ActivityMode
   * @return String
   */
  String getActivityMode();

  /**
   * Returns CollectorCode
   * @return String
   */
  String getCollectorCode();

  /**
   * Returns CollectorName
   * @return String
   */
  String getCollectorName();

  /**
   * Returns AgencyCode
   * @return String
   */
  String getAgencyCode();

}
