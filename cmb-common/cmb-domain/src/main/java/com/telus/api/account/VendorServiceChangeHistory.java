/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface VendorServiceChangeHistory {

    String getVendorServiceCode(); //represented by Category Code
    ServiceChangeHistory[] getPromoSOCs();  //Add-on SOCs with promos
    
}
