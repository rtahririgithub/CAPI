package com.telus.eas.equipment.info;

import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.eas.framework.info.Info;

import java.util.Date;

public class EquipmentSubscriberInfo extends Info implements EquipmentSubscriber {

	static final long serialVersionUID = 1L;

    private int banId;
    private String subscriberId;
    private String productType;
    private String phoneNumber;
    private Date effectiveDate;
    private Date expiryDate;
    private int esnLevel;

    public int getBanId() {
        return banId;
    }

    public void setBanId(int banId) {
        this.banId = banId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public int getEsnLevel() {
        return esnLevel;
    }

    public void setEsnLevel(int esnLevel) {
        this.esnLevel = esnLevel;
    }
}
