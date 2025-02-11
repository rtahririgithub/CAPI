package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MultiRingInfo extends Info {

  static final long serialVersionUID = 1L;

  public static final byte ADD = (byte)'I';
  public static final byte DELETE = (byte)'D';
  public static final byte NO_CHANGE = (byte)'N';

  private byte mode = NO_CHANGE;
  private String phone;
  private String socCode;

  public MultiRingInfo() {
  }

  public byte getMode() {
    return mode;
  }

  public String getPhone() {
    return phone;
  }

  public void setMode(byte mode) {
    this.mode = mode;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getSocCode() {
    return socCode;
  }

  public void setSocCode(String socCode) {
    this.socCode = socCode;
  }
}
