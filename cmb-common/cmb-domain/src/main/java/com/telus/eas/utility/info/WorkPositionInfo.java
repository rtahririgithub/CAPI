package com.telus.eas.utility.info;

import com.telus.api.reference.WorkPosition;
import com.telus.eas.framework.info.Info;

import java.util.Date;

public class WorkPositionInfo extends Info implements WorkPosition {

	static final long serialVersionUID = 1L;

    private String id;
    private String supervisorWorkPositionId;
    private String designateWorkPositionId;
    private String[] assignedUsers;
    private String functionCode;
    private String departmentCode;
    private Date effectiveDate;
    private Date expiryDate;
    private String[] subordinateWorkPositionIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupervisorWorkPositionId() {
        return supervisorWorkPositionId;
    }

    public void setSupervisorWorkPositionId(String supervisorWorkPositionId) {
        this.supervisorWorkPositionId = supervisorWorkPositionId;
    }

    public String getDesignateWorkPositionId() {
        return designateWorkPositionId;
    }

    public void setDesignateWorkPositionId(String designateWorkPositionId) {
        this.designateWorkPositionId = designateWorkPositionId;
    }

    public String[] getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(String[] assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String[] getSubordinateWorkPositionIds() {
        return subordinateWorkPositionIds;
    }

    public void setSubordinateWorkPositionIds(String[] subordinateWorkPositionIds) {
        this.subordinateWorkPositionIds = subordinateWorkPositionIds;
    }

    public String getDescription() {
        return "";  // no description
    }

    public String getDescriptionFrench() {
        return "";  // no description
    }

    public String getCode() {
        return id;  
    }
}
