package com.telus.api.equipment;

import java.util.Date;

public interface EquipmentSubscriber {
    int getBanId();
    String getSubscriberId();
    String getProductType();
    String getPhoneNumber();
    Date getEffectiveDate();
    Date getExpiryDate();
    int getEsnLevel();
}
