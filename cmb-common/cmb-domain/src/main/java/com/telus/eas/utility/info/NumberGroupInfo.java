

/**
 * Title:       NumberGroupInfo <p>
 * Description:  The  NumberGroupInfo holds all Number Group(City) ralated info<p>
 * Copyright:    Copyright (c) Ludmila Pomirche <p>
 * Company:     Telus Mobilty INC. <p>
 * @author Ludmila Pomirche
 * @version 1.0
 */

package com.telus.eas.utility.info;

import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class NumberGroupInfo extends Info implements NumberGroup {

  public static final long serialVersionUID = -5827036443837766157L;

  private static final NumberRangeInfo[] EMPTY_NUMBER_RANGE_INFO = new NumberRangeInfo[0];

  protected String code;
  protected String description ;
  protected String provinceCode ;
  protected String[] npaNXX ;
  protected String descriptionFrench ;
  protected int networkId;
  private String numberLocation;
  private String defaultDealerCode;
  private String defaultSalesCode;
  private NumberRangeInfo[] numberRanges = EMPTY_NUMBER_RANGE_INFO;


  public int hashCode() {
    return code.hashCode();
  }

  public boolean equals(Object o) {
    return o != null &&
    o instanceof NumberGroupInfo &&
    code.equals(((NumberGroupInfo)o).code);
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public String[] getNpaNXX() {
    return npaNXX;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }


  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setProvinceCode(String newProvinceCode) {
    provinceCode = newProvinceCode;
  }

  public void setNpaNXX(String[] newNpaNXX) {
    npaNXX = newNpaNXX;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }

  public void setNetworkId(int networkId) {
    this.networkId = networkId;
  }

  public int getNetworkId() {
    return networkId;
  }

  public void setNumberLocation(String numberLocation) {
    this.numberLocation = numberLocation;
  }

  public String getNumberLocation() {
    return numberLocation;
  }

  public void setDefaultDealerCode(String defaultDealerCode) {
    this.defaultDealerCode = defaultDealerCode;
  }

  public String getDefaultDealerCode() {
    return defaultDealerCode;
  }

  public void setDefaultSalesCode(String defaultSalesCode) {
    this.defaultSalesCode = defaultSalesCode;
  }

  public String getDefaultSalesCode() {
    return defaultSalesCode;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("NumberGroupInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    defaultDealerCode=[").append(defaultDealerCode).append("]\n");
      s.append("    defaultSalesCode=[").append(defaultSalesCode).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    networkId=[").append(networkId).append("]\n");
      s.append("    numberLocation=[").append(numberLocation).append("]\n");
      s.append("    provinceCode=[").append(provinceCode).append("]\n");
      if(npaNXX == null)
      {
          s.append("    npaNXX=[null]\n");
      }
      else if(npaNXX.length == 0)
      {
          s.append("    npaNXX={}\n");
      }
      else
      {
          for(int i=0; i<npaNXX.length; i++)
          {
              s.append("    npaNXX["+i+"]=[").append(npaNXX[i]).append("]\n");
          }
      }
      if(numberRanges == null)
      {
          s.append("    numberRanges=[null]\n");
      }
      else if(numberRanges.length == 0)
      {
          s.append("    numberRanges={}\n");
      }
      else
      {
          for(int i=0; i<numberRanges.length; i++)
          {
              s.append("    numberRanges["+i+"]=[").append(numberRanges[i]).append("]\n");
          }
      }
      s.append("]");

      return s.toString();
  }

  /**
   * Returns the NumberRange for a given NPA-NXX.
   *
   */
  public NumberRangeInfo getNumberRange(String npanxx) {
    for (int i = 0; i < numberRanges.length; i++) {
      if (numberRanges[i].getNPANXX().equals(npanxx)) {
        return numberRanges[i];
      }
    }
    return null;
  }


  /**
   * Returns the NumberRanges (NPA-NXXs) supported by this NumberGroup (City).
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public NumberRangeInfo[] getNumberRanges() throws TelusAPIException {
    return numberRanges;
  }

  public void setNumberRanges(NumberRangeInfo[] numberRanges) {
    if (numberRanges == null) {
      this.numberRanges = EMPTY_NUMBER_RANGE_INFO;
    } else {
      this.numberRanges = numberRanges;
    }
  }

  public void addNumberRange(NumberRangeInfo numberRange) {
    if (numberRange == null) {
      throw new NullPointerException("numberRange is null");
    }


    //-------------------------------------------------------------------
    // Add new numberRange to this.numberRanges
    //-------------------------------------------------------------------
    if (this.numberRanges == null) {
      this.numberRanges = new NumberRangeInfo[]{numberRange};
    } else {
      int oldLength = this.numberRanges.length;

      NumberRangeInfo[] newArray = new NumberRangeInfo[oldLength + 1];
      System.arraycopy(this.numberRanges, 0, newArray, 0, oldLength);
      newArray[oldLength] = numberRange;

      this.numberRanges = newArray;
    }

  }

  public boolean isProvisionedOn(int provisioningPlatformId) {
    if (numberRanges != null) {
      for (int i = 0; i < numberRanges.length; i++) {
        if (numberRanges[i].isProvisionedOn(provisioningPlatformId)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns the NumberRange for a given NPA-NXX.
   *
   */
  public void removeNPANXXsWithoutNumberRanges() {
    if (numberRanges != null && numberRanges.length > 0) {
      String[] newArray = new String[numberRanges.length];
      for (int i = 0; i < numberRanges.length; i++) {
        newArray[i] = numberRanges[i].getNPANXX();
      }
      this.npaNXX = newArray;
    }

  }



}

