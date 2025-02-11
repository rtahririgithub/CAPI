/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.interaction;
import java.util.Date;

public interface EquipmentChange extends InteractionDetail {

    long getSwapRequestId();

    String getChnlOrgCd();

    String getUserCode();

    String getRequestorId();

    String getSwapType();

    int getBan();

    String getSubscriberNo();

    String getRepairId();

    String getOldSmNum();

    String getOldTechnologyType();

    String getOldProductCd();

    String getOldProductStatusCd();

    String getOldProductClassCd();

    String getOldProductGpTypeCd();

    Date getOldWarrantyDate();

    Date getOldDoaDate();

    Date getOldInitialMfgDate();

    String getOldAssocMuleImei();

    Date getOldPendDate();

    String getOldPendProdGpTypeCd();

    String getNewSmNum();

    String getNewTechnologyType();

    String getNewProductCd();

    String getNewProductStatusCd();

    String getNewProductClassCd();

    String getNewProductGpTypeCd();

    Date getNewWarrantyDate();

    String getLeaseFlg();

    String getSapOrderNum();

    Date getCreateDate();

    String getCreateUser();

    Date getModifyDate();

    String getModifyUser();
}


