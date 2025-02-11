package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

public class CollectionHistoryInfo extends Info implements CollectionHistory {

  private static final long serialVersionUID = 1L;

  private CollectionStepInfo collectionStepInfo;
  private String activityMode;
  private String collectorCode;
  private String collectorName;
  private String agencyCode;

  public CollectionHistoryInfo() {
  }

  public CollectionStep getCollectionStep() {
    return collectionStepInfo;
  }

  public CollectionStepInfo getCollectionStepInfo() {
    return collectionStepInfo;
  }

  public void setCollectionStepInfo(CollectionStepInfo info) {
    this.collectionStepInfo = info;
  }

  // ActivityMode
  public String getActivityMode() {
    return this.activityMode;
  }

  public void setActivityMode(String mode) {
    this.activityMode = mode;
  }

  // CollectorCode
  public String getCollectorCode() {
    return this.collectorCode;
  }

  public void setCollectorCode(String code) {
    this.collectorCode = code;
  }

  // CollectorName
  public String getCollectorName() {
    return this.collectorName;
  }

  public void setCollectorName(String name) {
    this.collectorName = name;
  }

  // AgencyCode
  public String getAgencyCode() {
    return this.agencyCode;
  }

  public void setAgencyCode(String code) {
    this.agencyCode = code;
  }
  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("CollectionHistoryInfo:[\n");
      s.append("    collectionStepInfo=[").append(collectionStepInfo.toString()).append("]\n");
      s.append("    activityMode=[").append(activityMode).append("]\n");
      s.append("    collectorCode=[").append(collectorCode).append("]\n");
      s.append("    collectorName=[").append(collectorName).append("]\n");
      s.append("    agencyCode=[").append(agencyCode).append("]\n");
      s.append("]");

      return s.toString();
  }
}
