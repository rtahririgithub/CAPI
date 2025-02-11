

package com.telus.eas.utility.info;

import com.telus.eas.framework.info.*;
import com.telus.api.account.*;

public class LineRangeInfo extends Info {

  public static final long serialVersionUID = 7081868995664514817L;

  public final static char PROVISIONING_PLATFORM_WPS = Subscriber.PROVISIONING_PLATFORM_WPS;
  public final static char PROVISIONING_PLATFORM_WIN = Subscriber.PROVISIONING_PLATFORM_WIN;
  public final static char PROVISIONING_PLATFORM_OTHER = Subscriber.PROVISIONING_PLATFORM_OTHER;

  private int start;
  private int end;
  private int provisioningPlatformId;

  public int getStart() {
    return start;
  }

  public void setStart(int start){
    this.start = start;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end){
    this.end = end;
  }

  /**
   * Returns one of the PROVISIONING_PLATFORM_xxx constants.
   */
  public int getProvisioningPlatformId() {
    return provisioningPlatformId;
  }

  public void setProvisioningPlatformId(int provisioningPlatformId){
    this.provisioningPlatformId = provisioningPlatformId;
  }

  public boolean contains(String phoneNumber) {
    int n = Integer.parseInt(phoneNumber.substring(phoneNumber.length()-4));
    return n >= start && n <= end;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("LineRangeInfo:[\n");
      s.append("    PROVISIONING_PLATFORM_OTHER=[").append(PROVISIONING_PLATFORM_OTHER).append("]\n");
      s.append("    PROVISIONING_PLATFORM_WIN=[").append(PROVISIONING_PLATFORM_WIN).append("]\n");
      s.append("    PROVISIONING_PLATFORM_WPS=[").append(PROVISIONING_PLATFORM_WPS).append("]\n");
      s.append("    end=[").append(end).append("]\n");
      s.append("    start=[").append(start).append("]\n");
      s.append("    provisioningPlatformId=[").append(provisioningPlatformId).append("]\n");
      s.append("]");

      return s.toString();
  }

}

