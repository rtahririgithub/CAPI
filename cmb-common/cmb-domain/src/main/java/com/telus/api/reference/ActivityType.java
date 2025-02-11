package com.telus.api.reference;



public interface ActivityType extends Reference

{

    String getActivityType();
    ReasonType[] getReasonTypes();

    /**
     * Returns the reasons applicable to non-system activities.
     *
     */
    ReasonType[] getManualReasonTypes();

    ReasonType getReasonType(String code);

}

