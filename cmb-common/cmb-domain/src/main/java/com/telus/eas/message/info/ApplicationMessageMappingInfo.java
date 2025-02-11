package com.telus.eas.message.info;

import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 9-Jan-2007
 */

public class ApplicationMessageMappingInfo extends Info {
  static final long serialVersionUID = 1L;

  private String sourceMessageCode;
  private String sourceApplicationCode;
  private long targetMessageId;

  public String getSourceMessageCode() {
    return sourceMessageCode;
  }

  public void setSourceMessageCode(String sourceMessageCode) {
    this.sourceMessageCode = sourceMessageCode;
  }

  public String getSourceApplicationCode() {
    return sourceApplicationCode;
  }

  public void setSourceApplicationCode(String sourceApplicationCode) {
    this.sourceApplicationCode = sourceApplicationCode;
  }

  public long getTargetMessageId() {
    return targetMessageId;
  }

  public void setTargetMessageId(long targetMessageId) {
    this.targetMessageId = targetMessageId;
  }
}
