package com.telus.eas.utility.info;

import com.telus.api.reference.CollectionPathDetail;


public class CollectionPathDetailsInfo extends ReferenceInfo implements CollectionPathDetail{

  static final long serialVersionUID = 1L;
  private String pathCode;
  private int stepNumber;
  private String activityCode;
  private int daysFromPreviousStep;
  private String pointOfDaysCount;
  private String approvalFollowUpTypeCode;
  private String followUpTypeCode;
  private String letterTitle;
  private String letterTitleFrench;
  private String billMessageActivityCode;
  private String billMessageActivityReasonCode;

  public String getPathCode(){
          return pathCode;
  }
  public int getStepNumber(){
          return stepNumber;
  }
  public String getActivityCode(){
          return activityCode;
  }

  public String getCode(){
          return pathCode + stepNumber;
  }

  public void setActivityCode(String activityCode) {
          this.activityCode = activityCode;
  }
  public void setPathCode(String pathCode) {
          this.pathCode = pathCode;
  }
  public void setStepNumber(int stepNumber) {
          this.stepNumber = stepNumber;
  }

  public int getDaysFromPreviousStep() {
    return this.daysFromPreviousStep;
  }

  public String getPointOfDaysCount() {
    return this.pointOfDaysCount;
  }

  public String getApprovalFollowUpTypeCode() {
    return this.approvalFollowUpTypeCode;
  }

  public String getFollowUpTypeCode() {
    return this.followUpTypeCode;
  }

  public String getLetterTitle() {
    return this.letterTitle;
  }

  public String getLetterTitleFrench() {
    return this.letterTitleFrench;
  }

  public String getBillMessageActivityCode() {
    return this.billMessageActivityCode;
  }

  public String getBillMessageActivityReasonCode() {
    return this.billMessageActivityReasonCode;
  }

  public void setDaysFromPreviousStep(int days) {
    this.daysFromPreviousStep = days;
  }

  public void setPointOfDaysCount(String pointOfDaysCount) {
    this.pointOfDaysCount = pointOfDaysCount;
  }

  public void setApprovalFollowUpTypeCode(String approvalFollowUpTypeCode) {
    this.approvalFollowUpTypeCode = approvalFollowUpTypeCode;
  }

  public void setFollowUpTypeCode(String followUpTypeCode) {
    this.followUpTypeCode = followUpTypeCode;
  }

  public void setLetterTitle(String letterTitle) {
    this.letterTitle = letterTitle;
  }

  public void setLetterTitleFrench(String letterTitleFrench) {
    this.letterTitleFrench = letterTitleFrench;
  }

  public void setBillMessageActivityCode(String billMessageActivityCode) {
    this.billMessageActivityCode = billMessageActivityCode;
  }

  public void setBillMessageActivityReasonCode(String billMessageActivityReasonCode) {
    this.billMessageActivityReasonCode = billMessageActivityReasonCode;
  }


  public String toString() {
      StringBuffer s = new StringBuffer();

      s.append("CollectionPathDetailsInfo:{\n");
      s.append("    pathCode=[").append(pathCode).append("]\n");
      s.append("    stepNumber=[").append(stepNumber).append("]\n");
      s.append("    activityCode=[").append(activityCode).append("]\n");
      s.append("    daysFromPreviousStep=[").append(this.daysFromPreviousStep).append("]\n");
      s.append("    pointOfDaysCount=[").append(this.pointOfDaysCount).append("]\n");
      s.append("    approvalFollowUpTypeCode=[").append(this.approvalFollowUpTypeCode).append("]\n");
      s.append("    followUpTypeCode=[").append(this.followUpTypeCode).append("]\n");
      s.append("    letterTitle=[").append(this.letterTitle).append("]\n");
      s.append("    letterTitleFrench=[").append(this.letterTitleFrench).append("]\n");
      s.append("    billMessageActivityCode=[").append(this.billMessageActivityCode).append("]\n");
      s.append("    billMessageActivityReasonCode=[").append(this.billMessageActivityReasonCode).append("]\n");
      s.append("}");

      return s.toString();
  }


}
