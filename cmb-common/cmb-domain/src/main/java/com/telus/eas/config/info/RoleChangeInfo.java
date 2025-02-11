/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.RoleChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;


/**
  * Value (info) object for the role change interaction detail information.
  *
  */
public class RoleChangeInfo extends ConfigurationManagerInfo implements RoleChange {

   static final long serialVersionUID = 1L;

  private String oldRole = "";
  private String newRole = "";

  /**
    * Default empty constructor
    */
  public RoleChangeInfo() {
  }

  /**
    * Constructs a RoleChangeInfo object with the given interaction details.
    *
    * @param oldRole
    * @param newRole
    */
  public RoleChangeInfo(String oldRole, String newRole) {
    setOldRole(oldRole);
    setNewRole(newRole);
  }

  /**
    * Copies the information from the given dao object to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiRoleChangeDAO dao) {
//    setOldRole(dao.getOldRole());
//    setNewRole(dao.getNewRole());
//  }

  /**
    * Returns the type of interaction detail represented by this object.
    *
    * @return String -- Always InteractionManager.TYPE_ROLE_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_ROLE_CHANGE;
  }
  
  public String getMessageType() {
		return MESSAGE_TYPE_ROLE_CHANGE;
  }

  public String getNewRole() {
    return newRole;
  }

  public void setNewRole(String newRole) {
    this.newRole = newRole;
  }

  public String getOldRole() {
    return oldRole;
  }

  public void setOldRole(String oldRole) {
    this.oldRole = oldRole;
  }
}