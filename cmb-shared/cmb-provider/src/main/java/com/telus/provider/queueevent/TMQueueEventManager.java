package com.telus.provider.queueevent;

import com.telus.api.TelusAPIException;
import com.telus.api.queueevent.QueueEventManager;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TMQueueEventManager extends BaseProvider implements QueueEventManager {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public TMQueueEventManager(TMProvider provider) throws TelusAPIException {
	    super(provider);
	  }

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
  public void updateEvent(long interactionId, long subscriptionId, String phoneNumber, int teamMemberId, int userId) throws TelusAPIException{
    try {
    	provider.getQueueEventManagerNew().updateEvent(interactionId,subscriptionId,phoneNumber,teamMemberId,userId);
      } catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
      }
  }

  
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
  public void createNewEvent(long connectionId, String phoneNumber, long subscriptionId, int userId, String queueName, int thresholdSeconds) throws TelusAPIException {
    try {
    	provider.getQueueEventManagerNew().createNewEvent(connectionId,phoneNumber,subscriptionId,userId,queueName,thresholdSeconds);
      } catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
      }
  }
}
