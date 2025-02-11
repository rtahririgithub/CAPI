/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class ServiceRequestNoteTypeInfo extends Info implements ServiceRequestNoteType {

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;
  private long id;
  private String name;

  public ServiceRequestNoteTypeInfo() {
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public long getId() {
	  return id;
  }
  
  public String getName() {
	  return name;
  }
  
  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public void setId(long id) {
	  this.id = id;
  }
  
  public void setName(String name) {
	  this.name = name;
  }
  
  public String toString() {

        StringBuffer s = new StringBuffer(128);

        s.append("ServiceRequestNoteTypeInfo:[\n");
        s.append("    code=[").append(code).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("    id=[").append(id).append("]\n");
        s.append("    name=[").append(name).append("]\n");
        s.append("]");

        return s.toString();

    }


}



