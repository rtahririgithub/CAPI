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


  public class EncodingFormatInfo implements EncodingFormat{

  static final long serialVersionUID = 1L;

  private String code;
  private String description ;
  private String descriptionFrench;
  private String encoderString;

  public EncodingFormatInfo(){}

  public String getCode() {
    return code;
  }
  public String getDescription() {
    return description;
  }
  public String getDescriptionFrench() {
    return descriptionFrench;
  }
  public String getEncoderString() {
    return descriptionFrench;
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
  public void setEncoderString(String encoderString) {
    this.encoderString = encoderString;
  }
  public String toString()
  {
      StringBuffer s = new StringBuffer(1024);

      s.append("EncodingFormatInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    encoderString=[").append(encoderString).append("]\n");
      s.append("]");

      return s.toString();
  }
}
