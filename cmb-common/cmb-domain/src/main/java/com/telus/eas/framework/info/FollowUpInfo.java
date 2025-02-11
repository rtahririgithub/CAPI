package com.telus.eas.framework.info;

/**
 * Title:        FollowUpInfo<p>
 * Description:  The FollowUpInfo holds all attributes for a memo.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import com.telus.api.TelusAPIException;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpText;

import java.util.Date;
import java.util.ArrayList;

public class FollowUpInfo extends Info implements FollowUp {

	static final long serialVersionUID = 1L;

    private int banId = 0;
    private int followUpId;
    private String type = "";
    private Date dueDate = new Date();
    private String subscriberId = "";
    private String phoneNumber = "";
    private String productType = "";
    private String text = "";
    private boolean considerBanStamp = false;
    private char status;
    private Date openDate = new Date();
    private Date closeDate;
    private int operatorId;
    private String openedBy;
    private String closedBy;
    private String assignedToWorkPositionId;
    private String closeReasonCode;
    private ArrayList additionalTextList;
    private FollowUp[] history;

    public FollowUpInfo() {
    }

    public int getBanId() {
        return banId;
    }

    public void setBanId(int newBanId) {
        banId = newBanId;
    }

    public int getBan() {
        return banId;
    }

    public void setBan(int newBan) {
        banId = newBan;
    }

    public String getFollowUpType() {
        return type;
    }

    public void setFollowUpType(String newType) {
        type = newType;
    }

    public String getType() {
        return type;
    }

    public void setType(String newType) {
        type = newType;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date newDueDate) {
        dueDate = newDueDate;
    }

    /**
     * @deprecated - use getAssignedToWorkPositionId()
     * @return String
     */
    public String getAssignedTo() {
        return getAssignedToWorkPositionId();
    }

    /**
     * @deprecated - use setAssignedToWorkPositionId(String assignedToWorkPositionId)
     * @param newAssignedTo
     */
    public void setAssignedTo(String newAssignedTo) {
        setAssignedToWorkPositionId(newAssignedTo);
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String newSubscriberId) {
        subscriberId = newSubscriberId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String newProductType) {
        productType = newProductType;
    }

    public String getText() {
        return text;
    }

    public void setText(String newText) {
        text = newText;
    }

    public boolean isConsiderBanStamp() {
        return considerBanStamp;
    }

    public void setConsiderBanStamp(boolean newConsiderBanStamp) {
        considerBanStamp = newConsiderBanStamp;
    }

    public void setStatus(char newStatus) {
        status = newStatus;
    }

    public char getStatus() {
        return status;
    }

    public boolean isSubscriberLevel() {
        if (subscriberId == null || productType == null) return false;
        if (subscriberId.trim().equals("") || productType.trim().equals("")) return false;
        return true;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public int getFollowUpId() {
        return followUpId;
    }

    public void setFollowUpId(int followUpId) {
        this.followUpId = followUpId;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getAssignedToWorkPositionId() {
        return assignedToWorkPositionId;
    }

    public void setAssignedToWorkPositionId(String assignedToWorkPositionId) {
        this.assignedToWorkPositionId = assignedToWorkPositionId;
    }

    public String getCloseReasonCode() {
        return closeReasonCode;
    }

    public void setCloseReasonCode(String closeReasonCode) {
        this.closeReasonCode = closeReasonCode;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public FollowUpText[] getAdditionalText() throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public FollowUpText[] getAdditionalText(boolean refresh) throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public FollowUpText[] getAdditionalText0() {
        FollowUpText[] additionalText = null;

        int additionalTextSize = additionalTextList != null ? additionalTextList.size() : 0;

        if (additionalTextSize > 0) {
            additionalText = new FollowUpText[additionalTextSize];

            for (int i = 0; i < additionalTextSize; i++) {
                additionalText[i] = (FollowUpText) additionalTextList.get(i);
            }
        }

        return additionalText;
    }

    public void setAdditionalText0(FollowUpText[] additionalText) {
        int additionalTextSize = additionalText != null ? additionalText.length : 0;

        if (additionalTextSize > 0) {
            additionalTextList = new ArrayList();

            for (int i = 0; i < additionalTextSize; i++) {
                additionalTextList.add(i, additionalText[i]);
            }
        }
    }

    public FollowUp[] getHistory() throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public FollowUp[] getHistory(boolean refresh) throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public FollowUp[] getHistory0() {
        return history;
    }

    public void setHistory0(FollowUp[] history) {
        this.history = history;
    }

    public void addText(String text) {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public void addText0(FollowUpText text) {
        if (additionalTextList == null)
            additionalTextList = new ArrayList();

        additionalTextList.add(text);
    }

    public void copyFrom(FollowUpInfo source) {
        setBanId(source.getBanId());
        setFollowUpId(source.getFollowUpId());
        setFollowUpType(source.getFollowUpType());
        setDueDate(source.getDueDate());
        setSubscriberId(source.getSubscriberId());
        setPhoneNumber(source.getPhoneNumber());
        setProductType(source.getProductType());
        setText(source.getText());
        setConsiderBanStamp(source.isConsiderBanStamp());
        setStatus(source.getStatus());
        setOpenDate(source.getOpenDate());
        setCloseDate(source.getCloseDate());
        setOperatorId(source.getOperatorId());
        setOpenedBy(source.getOpenedBy());
        setClosedBy(source.getClosedBy());
        setAssignedToWorkPositionId(source.getAssignedToWorkPositionId());
        setCloseReasonCode(source.getCloseReasonCode());
        setAdditionalText0(source.getAdditionalText0());
        setHistory0(source.getHistory0());
    }

    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append("FollowUpInfo:{\n");
        s.append("    banId=[").append(banId).append("]\n");
        s.append("    followUpId=[").append(followUpId).append("]\n");
        s.append("    type=[").append(type).append("]\n");
        s.append("    dueDate=[").append(dueDate).append("]\n");
        s.append("    openDate=[").append(openDate).append("]\n");
        s.append("    closeDate=[").append(closeDate).append("]\n");
        s.append("    assignedToWorkPositionId=[").append(assignedToWorkPositionId).append("]\n");
        s.append("    subscriberId=[").append(subscriberId).append("]\n");
        s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
        s.append("    productType=[").append(productType).append("]\n");
        s.append("    text=[").append(text).append("]\n");
        s.append("    considerBanStamp=[").append(considerBanStamp).append("]\n");
        s.append("    status=[").append(((status == 0) ? "" : String.valueOf(status))).append("]\n");
        s.append("    operatorId=[").append(operatorId).append("]\n");
        s.append("    openedBy=[").append(openedBy).append("]\n");
        s.append("    closedBy=[").append(closedBy).append("]\n");
        s.append("    closeReasonCode=[").append(closeReasonCode).append("]\n");
        s.append("}");

        return s.toString();
    }

    public void create() throws TelusAPIException {
    }

    public void save() throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public void approve() throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }

    public void deny() throws TelusAPIException {
        throw new UnsupportedOperationException("Method not implemented here");
    }


}

