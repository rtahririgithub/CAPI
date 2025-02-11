

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class NumberRangeInfo extends Info implements Reference {

  public static final long serialVersionUID = -4512583931599268124L;
  private static final char prependChar = '0';

  public static NumberRangeInfo newInstanmce(String nPANXX) {
    return newInstanmce(nPANXX.substring(0, 3), nPANXX.substring(3, 6));
  }

  public static NumberRangeInfo newInstanmce(String nPA, String nXX) {
    return newInstanmce(Integer.parseInt(nPA), Integer.parseInt(nXX));
  }

  public static NumberRangeInfo newInstanmce(int nPA, int nXX) {
    NumberRangeInfo numberRangeInfo = new NumberRangeInfo();
    numberRangeInfo.setNPA(nPA);
    numberRangeInfo.setNXX(nXX);

    LineRangeInfo lineRangeInfo = new LineRangeInfo();
    lineRangeInfo.setStart(0);
    lineRangeInfo.setEnd(9999);
//    lineRangeInfo.setProvisioningPlatformId(0);

    numberRangeInfo.setLineRanges(new LineRangeInfo[]{lineRangeInfo});

    return numberRangeInfo;
  }



  private int nPA;
  private int nXX;
  private LineRangeInfo[] lineRanges;
  private String nPANXX;


  public Object clone() {
    NumberRangeInfo info = (NumberRangeInfo)super.clone();
//  info.lineRanges = (LineRangeInfo[])PublicCloneable.Helper.clone(lineRanges);
    if (lineRanges != null) {
      info.lineRanges = (LineRangeInfo[])lineRanges.clone();
    }
    return info;
  }


  public int getNPA() {
    return nPA;
  }

  public void setNPA(int nPA){
    this.nPA = nPA;
    setNPANXX();
  }

  public int getNXX() {
    return nXX;
  }

  public void setNXX(int nXX){
    this.nXX = nXX;
    setNPANXX();
  }

  public LineRangeInfo[] getLineRanges() {
    return lineRanges;
  }

  public void setLineRanges(LineRangeInfo[] lineRanges){
    this.lineRanges = lineRanges;
  }

  public LineRangeInfo getLineRange(String phoneNumber) {
    if (lineRanges != null) {
      for (int i = 0; i < lineRanges.length; i++) {
        if (lineRanges[i].contains(phoneNumber)) {
          return lineRanges[i];
        }
      }
    }
    return null;
  }

  public boolean contains(String phoneNumber) {
    if (!phoneNumber.startsWith(nPANXX)) {
      return false;
    }

    if (lineRanges == null  || lineRanges.length == 0) {
      return true;
    } else {
      for (int i = 0; i < lineRanges.length; i++) {
        if (lineRanges[i].contains(phoneNumber)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean contains(String phoneNumber, int provisioningPlatformId) {
    if (!phoneNumber.startsWith(nPANXX)) {
      return false;
    }

    if (lineRanges != null && lineRanges.length == 0) {
      return true;
    } else {
      for (int i = 0; i < lineRanges.length; i++) {
        if (lineRanges[i].contains(phoneNumber) && lineRanges[i].getProvisioningPlatformId() == provisioningPlatformId) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isProvisionedOn(int provisioningPlatformId) {
    if (lineRanges != null) {
      for (int i = 0; i < lineRanges.length; i++) {
        if (lineRanges[i].getProvisioningPlatformId() == provisioningPlatformId) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isProvisionedOn(int[] provisioningPlatformId) {
    boolean result = false;

    for (int i = 0; result == false && i < provisioningPlatformId.length; i++) {
      result = isProvisionedOn(provisioningPlatformId[i]);
    }

    return result;
  }

  /**
   * Removes line ranges from this <code>NumberRangeInfo</code> that aren't
   * provisioned on any of the specified platforms.
   *
   * @param provisioningPlatformId
   */
  public void retainLineRangesByProvisioningPlatform(int[] provisioningPlatformId) {

    if (lineRanges != null) {
      List list = new ArrayList(lineRanges.length);

      for (int i = 0; i < lineRanges.length; i++) {

        PROVISIONING_PLATFORM_ID_LOOP:
        for (int j = 0; j < provisioningPlatformId.length; j++) {
          if (lineRanges[i].getProvisioningPlatformId() == provisioningPlatformId[j]) {
            list.add(lineRanges[i]);
            break PROVISIONING_PLATFORM_ID_LOOP;
          }
        }
      }

      lineRanges = (LineRangeInfo[])list.toArray(new LineRangeInfo[list.size()]);
    }
  }


  /**
   * Removes line ranges from this <code>NumberRangeInfo</code> that aren't
   * provisioned on the specified platform.
   *
   * @param provisioningPlatformId
   */
  public void retainLineRangesByProvisioningPlatform(int provisioningPlatformId) {
    retainLineRangesByProvisioningPlatform(new int[]{provisioningPlatformId});
  }

  private StringBuffer prependNxx(int number) {
	  
	  String nxx = String.valueOf(number);
	  StringBuffer s = new StringBuffer(3);
	  
	  for (int i = 0 ; i < (3 - nxx.length()); i++) {
		  s.append(prependChar);
	  }
	  
	  s.append(nxx);
	  
	  return s;
  }

  private void setNPANXX() {
	StringBuffer s = new StringBuffer(6);
    nPANXX = s.append(nPA).append(prependNxx(nXX)).toString();
  }

  public String getNPANXX() {
    return nPANXX;
  }

  public String getDescription() {
    return nPANXX;
  }

  public String getDescriptionFrench() {
    return nPANXX;
  }

  public String getCode() {
    return nPANXX;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("NumberRangeInfo:[\n");
      if(lineRanges == null)
      {
          s.append("    lineRanges=[null]\n");
      }
      else if(lineRanges.length == 0)
      {
          s.append("    lineRanges={}\n");
      }
      else
      {
          for(int i=0; i<lineRanges.length; i++)
          {
              s.append("    lineRanges["+i+"]=[").append(lineRanges[i]).append("]\n");
          }
      }
      s.append("    nPA=[").append(nPA).append("]\n");
      s.append("    nPANXX=[").append(nPANXX).append("]\n");
      s.append("    nXX=[").append(nXX).append("]\n");
      s.append("]");

      return s.toString();
  }

}

