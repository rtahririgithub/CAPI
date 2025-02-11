package com.telus.api.reference;

import java.util.Date;

public interface WorkPosition extends Reference {
    String getId();

    String getSupervisorWorkPositionId();

    String getDesignateWorkPositionId();

    String[] getAssignedUsers();

    String getFunctionCode();

    String getDepartmentCode();

    Date getEffectiveDate();

    Date getExpiryDate();

    String[] getSubordinateWorkPositionIds();
}
