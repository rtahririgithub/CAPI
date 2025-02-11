/*

 * $Id$

 * %E% %W%

 * Copyright (c) Clearnet Inc. All Rights Reserved.

 */



package com.telus.api.reference;



public interface ReasonType extends Reference {

    String getProcessCode();
    String getFeatureCode();
    String getDirection();
    boolean isPricePlanChangeRequired();

}

