/*
 * This class was generated by Object Harmony on [Wed Jun 16 10:57:20 EDT 2004] - DO NOT MODIFY.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class ProvisioningPlatformTypeInfo extends Info implements Reference {

  public static final long serialVersionUID = -6204567047622211289L;


    private long ProvisioningPlatformId;
    private char ProvisioningPlatformGroup;
    private String ProvisioningPlatformCode;


    public ProvisioningPlatformTypeInfo() {
    }

    public int getProvisioningPlatformId() {
        return (int)ProvisioningPlatformId;
    }

    public void setProvisioningPlatformId(int ProvisioningPlatformId) {
        this.ProvisioningPlatformId = ProvisioningPlatformId;
    }

    public String getProvisioningPlatformCode() {
        return ProvisioningPlatformCode;
    }

    public void setProvisioningPlatformCd(String ProvisioningPlatformCode) {
        this.ProvisioningPlatformCode = ProvisioningPlatformCode;
    }

    public char getProvisioningPlatformGroup() {
        return ProvisioningPlatformGroup;
    }

    public void setProvisioningPlatformGroup(char ProvisioningPlatformGroup) {
        this.ProvisioningPlatformGroup = ProvisioningPlatformGroup;
    }

    public String getDescription() {
      return getProvisioningPlatformCode();
    }

    public String getDescriptionFrench() {
      return getProvisioningPlatformCode();
    }

    public String getCode() {
      return String.valueOf(getProvisioningPlatformId());
    }



    public String toString() {
        StringBuffer s = new StringBuffer(128);

        s.append("ProvisioningPlatformTypeInfo[\n");
        s.append("    ProvisioningPlatformId=[").append(ProvisioningPlatformId).append("]\n");
        s.append("    ProvisioningPlatformCode=[").append(ProvisioningPlatformCode).append("]\n");
        s.append("    ProvisioningPlatformGroup=[").append(ProvisioningPlatformGroup).append("]\n");
        s.append("]");

        return s.toString();
    }

}
