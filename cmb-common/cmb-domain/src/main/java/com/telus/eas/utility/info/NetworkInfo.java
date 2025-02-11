package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

 import com.telus.api.reference.*;
 import com.telus.eas.framework.info.*;

public class NetworkInfo extends Info implements Network{

  static final long serialVersionUID = 1L;

  protected String description  ;
  protected String descriptionFrench ;
  private String alias;
  private int networkId;

  public String getCode() {
    return String.valueOf(networkId);
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }


  public void setDescription(String newDescription) {
    description = newDescription;
  }


  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }
  public String getAlias() {
    return alias;
  }
  public void setNetworkId(int networkId) {
    this.networkId = networkId;
  }
  public int getNetworkId() {
    return networkId;
  }

    public String toString()
    {

        StringBuffer s = new StringBuffer(128);



        s.append("NetworkInfo:[\n");

        s.append("    code=[").append(String.valueOf(networkId)).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("    alias=[").append(alias).append("]\n");
        s.append("    networkId=[").append(networkId).append("]\n");

        s.append("]");



        return s.toString();

    }


}