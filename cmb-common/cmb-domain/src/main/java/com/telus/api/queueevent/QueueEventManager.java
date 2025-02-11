package com.telus.api.queueevent;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.telus.api.TelusAPIException;

public interface QueueEventManager {

  /**
   * Update A Queue Wait Threshold Event in CCEVENTS database
   * 
   * @param interactionId long
   * @param subscriptionId long
   * @param phoneNumber String
   * @param teamMemberId int
   * @param userId int
   * @throws TelusAPIException
   */
  public void updateEvent(long interactionId, long subscriptionId, String phoneNumber, int teamMemberId, int userId) throws TelusAPIException;

  /**
   * Create A Queue Wait Threshold Event in CCEVENTS database
   * 
   * @param connectionId long
   * @param phoneNumber String
   * @param subscriptionId long
   * @param userId int
   * @param queueName String
   * @param thresholdSeconds int
   * @throws TelusAPIException
   */
  public void createNewEvent(long connectionId, String phoneNumber, long subscriptionId, int userId, String queueName, int thresholdSeconds) throws TelusAPIException;


}
