package com.telus.api.reference;

public interface CollectionPathDetail extends Reference {

    String getPathCode();
    int getStepNumber();
    String getActivityCode();

    int getDaysFromPreviousStep();
    String getPointOfDaysCount();
    String getApprovalFollowUpTypeCode();
    String getFollowUpTypeCode();
    String getLetterTitle();
    String getLetterTitleFrench();
    String getBillMessageActivityCode();
    String getBillMessageActivityReasonCode();
}
