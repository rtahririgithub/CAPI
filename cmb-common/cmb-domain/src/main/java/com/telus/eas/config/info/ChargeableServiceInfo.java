/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.*;
import com.telus.api.chargeableservices.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class ChargeableServiceInfo extends Info implements ChargeableService {
   static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;
  private String roleName;
  private double charge;
  private boolean applied;
  private boolean waived;
//  private final Set waivers = new HashSet();
  private final Map waivers = new HashMap();

  public String getCode() {
    return code;
  }

  public void setCode(String code){
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description){
    this.description = description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescriptionFrench(String descriptionFrench){
    this.descriptionFrench = descriptionFrench;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName){
    this.roleName = roleName;
  }

  public double getCharge() {
    return charge;
  }

  public void setCharge(double charge){
    this.charge = charge;
  }

  public boolean isApplied() {
    return applied;
  }

  public void setApplied(boolean applied){
    this.applied = applied;
  }

  public boolean isWaived() {
    return waived;
  }

  public void setWaived(boolean waived){
    this.waived = waived;
  }

  public boolean isAppliedOrWaived() {
    return applied || waived;
  }

  public Waiver[] getWaivers() {
    return (Waiver[])waivers.values().toArray(new Waiver[waivers.size()]);
  }

  public Waiver getWaiver(String code) {
    return (Waiver)waivers.get(code);
  }

  public boolean containsWaiver(String code) {
    return waivers.containsKey(code);
  }

  public void clearWaivers() {
    waivers.clear();
  }

  public void addWaiver(Waiver waiver) {
    waivers.put(waiver.getCode(), waiver);
  }

  public void apply() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void waive(Waiver waiver) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("ChargeableServiceInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    roleName=[").append(roleName).append("]\n");
      s.append("    charge=[").append(charge).append("]\n");
      s.append("    applied=[").append(applied).append("]\n");
      s.append("    waived=[").append(waived).append("]\n");
      s.append("    waivers=[").append(waivers).append("]\n");
      s.append("]");

      return s.toString();
  }

}



