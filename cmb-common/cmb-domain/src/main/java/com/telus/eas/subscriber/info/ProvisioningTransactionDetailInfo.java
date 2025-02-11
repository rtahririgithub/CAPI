/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.account.ProvisioningTransactionDetail;
import com.telus.eas.framework.info.Info;

/**
 * Title:        ProvisioningTransactionDetailInfo<p>
 * Description:  The ProvisioningTransactionDetailInfo class holds all provisioning transaction detail attributes for a subscriber.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class ProvisioningTransactionDetailInfo extends Info implements ProvisioningTransactionDetail {

	static final long serialVersionUID = 1L;

	private String status;
	private Date effectiveDate;
	private String service;
	private String errorReason;

	public ProvisioningTransactionDetailInfo() {
	}
	
    /**
     * @return Returns the effectiveDate.
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    /**
     * @param effectiveDate The effectiveDate to set.
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /**
     * @return Returns the errorReason.
     */
    public String getErrorReason() {
        return errorReason;
    }
    /**
     * @param errorReason The errorReason to set.
     */
    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
    
    /**
     * @return Returns the service.
     */
    public String getService() {
        return service;
    }
    /**
     * @param service The service to set.
     */
    public void setService(String service) {
        this.service = service;
    }
    
    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
