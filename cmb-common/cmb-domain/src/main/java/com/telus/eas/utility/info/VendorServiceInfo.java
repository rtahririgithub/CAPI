package com.telus.eas.utility.info;

import com.telus.api.reference.VendorService;

public class VendorServiceInfo  extends ReferenceInfo implements VendorService {
    static final long serialVersionUID = 1L;
    
    private String historyUnitOfMeasure;
    private int historyCheckPeriod;
    private String vendorName;
    private boolean restrictionRequired;
    private boolean notificationRequired;
    
    
    public String getHistoryUnitOfMeasure() {
    	return historyUnitOfMeasure;
    }

    public void setHistoryUnitOfMeasure(String unitOfMeasure) {
    	this.historyUnitOfMeasure = unitOfMeasure;
    }
    
    public int getHistoryCheckPeriod() {
    	return historyCheckPeriod;
    }

    public void setHistoryCheckPeriod(int period) {
    	this.historyCheckPeriod = period;
    }
    
    public String getVendorName() {
    	return vendorName;
    }

    public void setVendorName(String name) {
    	this.vendorName = name;
    }
        
    public boolean isRestrictionRequired() {
    	return restrictionRequired;
    }

    public void isRestrictionRequired(boolean required) {
    	this.restrictionRequired = required;
    }
    
    public boolean isNotificationRequired() {
    	return notificationRequired;
    }
    
    public void isNotificationRequired(boolean required) {
    	this.notificationRequired = required;
    }

}
