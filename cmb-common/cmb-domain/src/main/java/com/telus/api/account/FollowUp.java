package com.telus.api.account;

import com.telus.api.TelusAPIException;

import java.util.Date;

public interface FollowUp {

    int getBanId();

    int getFollowUpId();

    String getFollowUpType();

    void setFollowUpType(String newType);

    Date getOpenDate();

    Date getDueDate();

    void setDueDate(Date newDueDate);

    /**
     * @deprecated - use getAssignedToWorkPositionId()
     * @return String
     */
    String getAssignedTo();

    /**
     * @deprecated - use setAssignedToWorkPositionId(String assignedToWorkPositionId)
     * @param newAssignedTo
     */
    void setAssignedTo(String newAssignedTo);

    String getAssignedToWorkPositionId();

    void setAssignedToWorkPositionId(String assignedToWorkPositionId);

    String getSubscriberId();

    String getPhoneNumber();

    String getProductType();

    void setProductType(String newProductType);

    String getText();

    void setText(String newText);

    boolean isConsiderBanStamp();

    void setConsiderBanStamp(boolean newConsiderBanStamp);

    char getStatus();

    int getOperatorId();

    /**
     * Saves a new FollowUp with the contained information.
     * <p/>
     * <P>This method may involve a remote method call.
     */
    void create() throws TelusAPIException;

    String getOpenedBy();

    String getClosedBy();

    Date getCloseDate();

    String getCloseReasonCode();

    FollowUpText[] getAdditionalText() throws TelusAPIException;

    FollowUpText[] getAdditionalText(boolean refresh) throws TelusAPIException;

    FollowUp[] getHistory() throws TelusAPIException;

    FollowUp[] getHistory(boolean refresh) throws TelusAPIException;

    void addText(String text);

    void setCloseReasonCode(String closeReasonCode);

    void save() throws TelusAPIException;

    void approve() throws TelusAPIException;

    void deny() throws TelusAPIException;
}

