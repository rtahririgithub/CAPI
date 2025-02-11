/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.FollowUpType;
import com.telus.eas.framework.info.Info;

public class FollowUpTypeInfo extends Info implements FollowUpType {

	static final long serialVersionUID = 1L;

    protected String code;
    protected String description;
    protected String descriptionFrench;
    protected String manualOpenInd;
    protected String category;
    protected String systemText;
    protected String systemTextFrench;
    protected int defaultNoOfDays;
    protected int priority;
    protected String[] defaultDepartmentCodes;
    protected String[] defaultWorkPositionIds;

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionFrench() {
        return descriptionFrench;
    }

    public String getManualOpenInd() {
        return manualOpenInd;
    }

    public String getCategory() {
        return category;
    }

    public String getSystemText() {
        return systemText;
    }

    public String getSystemTextFrench() {
        return systemTextFrench;
    }

    public void setCode(String newCode) {
        code = newCode;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public void setDescriptionFrench(String newDescriptionFrench) {
        descriptionFrench = newDescriptionFrench;
    }

    public void setManualOpenInd(String newManualOpenInd) {
        manualOpenInd = newManualOpenInd;
    }

    public void setCategory(String newCategory) {
        category = newCategory;
    }

    public void setSystemText(String newSystemText) {
        systemText = newSystemText;
    }

    public void setSystemTextFrench(String newSystemTextFrench) {
        systemTextFrench = newSystemTextFrench;
    }

    public int getDefaultNoOfDays() {
        return defaultNoOfDays;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDefaultNoOfDays(int defaultNoOfDays) {
        this.defaultNoOfDays = defaultNoOfDays;
    }

    public String[] getDefaultDepartmentCodes() {
        return defaultDepartmentCodes;
    }

    public void setDefaultDepartmentCodes(String[] defaultDepartmentCodes) {
        this.defaultDepartmentCodes = defaultDepartmentCodes;
    }

    public String[] getDefaultWorkPositionIds() {
        return defaultWorkPositionIds;
    }

    public void setDefaultWorkPositionIds(String[] defaultWorkPositionIds) {
        this.defaultWorkPositionIds = defaultWorkPositionIds;
    }

}
