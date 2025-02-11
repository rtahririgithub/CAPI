package com.telus.cmb.account.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.exception.TelusException;


public interface UserDao {
	  /**
	   * Changes Knowbility password.
	   * 
	   * @param oldPassword
	   * @param newPassword
	   * @param userId
	   */
	  public void changeKnowbilityPassword(String userId, String oldPassword, String newPassword, String sessionId) throws ApplicationException;
	  
	  /**
	   * Return User's Profile ID
	   * @returns String       User's profile id
	   * @exception ApplicationException
	   */
	  public String getUserProfileID(String sessionId) throws ApplicationException;

}
