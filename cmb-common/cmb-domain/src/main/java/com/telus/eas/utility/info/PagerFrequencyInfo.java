package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import com.telus.api.reference.*;
import java.util.*;


public class PagerFrequencyInfo  implements PagerFrequency{

  static final long serialVersionUID = 1L;

  private String code;
  private String description ;
  private String descriptionFrench;
  private Date startDate;
  private Date endDate;


  public PagerFrequencyInfo() {
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
  public Date getStartDate() {
    return startDate;
  }
  public Date getEndDate() {
    return endDate;
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
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public String toString()
  {
      StringBuffer s = new StringBuffer(1024);

      s.append("PagerFrequencyInfo:[\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    startDate=[").append(startDate).append("]\n");
      s.append("    endDate=[").append(endDate).append("]\n");
      s.append("]");

      return s.toString();
  }
}