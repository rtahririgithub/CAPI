/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.InvoiceProperties;
import com.telus.eas.framework.info.Info;

import java.util.Date;

public class InvoicePropertiesInfo extends Info implements InvoiceProperties {

	 static final long serialVersionUID = 1L;

    private int ban;
    private Date holdRedirectFromDate;
    private Date holdRedirectToDate;
    private String holdRedirectDestinationCode;
    private String invoiceSuppressionLevel;

    public InvoicePropertiesInfo() {
    }
    
    public InvoicePropertiesInfo(InvoicePropertiesInfo invoiceProperties) {
    	ban = invoiceProperties.getBan();
    	holdRedirectFromDate = invoiceProperties.getHoldRedirectFromDate();
    	holdRedirectToDate = invoiceProperties.getHoldRedirectToDate();
    	holdRedirectDestinationCode = invoiceProperties.getHoldRedirectDestinationCode();
    	invoiceSuppressionLevel = invoiceProperties.getInvoiceSuppressionLevel();
    }

    /**
     *
     * @return int
     */
    public int getBan() {
        return ban;
    }

    /**
     *
     * @param ban
     */
    public void setBan(int ban) {
        this.ban = ban;
    }

    /**
     * @return Returns the holdRedirectDestinationCode.
     */
    public String getHoldRedirectDestinationCode() {
        return holdRedirectDestinationCode;
    }

    /**
     * @param holdRedirectDestinationCode - the holdRedirectDestinationCode to set.
     */
    public void setHoldRedirectDestinationCode(String holdRedirectDestinationCode) {
        this.holdRedirectDestinationCode = holdRedirectDestinationCode;
    }

    /**
     * @return Returns the holdRedirectFromDate.
     */
    public Date getHoldRedirectFromDate() {
        // temporary solution until Amdocs fixes the defect 37278
        return (holdRedirectDestinationCode != null && !holdRedirectDestinationCode.equals("0")) ? holdRedirectFromDate : null;
    }

    /**
     * @param holdRedirectFromDate - the holdRedirectFromDate to set.
     */
    public void setHoldRedirectFromDate(Date holdRedirectFromDate) {
        this.holdRedirectFromDate = holdRedirectFromDate;
    }

    /**
     * @return Returns the holdRedirectToDate.
     */
    public Date getHoldRedirectToDate() {
        // temporary solution until Amdocs fixes the defect 37278
        return (holdRedirectDestinationCode != null && !holdRedirectDestinationCode.equals("0")) ? holdRedirectToDate : null;
    }

    /**
     * @param holdRedirectToDate - the holdRedirectToDate to set.
     */
    public void setHoldRedirectToDate(Date holdRedirectToDate) {
        this.holdRedirectToDate = holdRedirectToDate;
    }

    /**
     * @return Returns the invoiceSuppressionLevel.
     */
    public String getInvoiceSuppressionLevel() {
        return invoiceSuppressionLevel;
    }

    /**
     * @param invoiceSuppressionLevel - the invoiceSuppressionLevel to set.
     */
    public void setInvoiceSuppressionLevel(String invoiceSuppressionLevel) {
        this.invoiceSuppressionLevel = invoiceSuppressionLevel;
    }

    /**
     * @return Returns the onHoldOrRedirected.
     */
    public boolean isOnHoldOrRedirected() {
        return (holdRedirectDestinationCode != null && Integer.parseInt(holdRedirectDestinationCode) > 0);
    }
   
    public String toString() {
        StringBuffer s = new StringBuffer(128);

        s.append("InvoicePropertiesInfo:[\n");
        s.append("    ban = [").append(ban).append("]\n");
        s.append("    isOnHoldOrRedirected = [").append(isOnHoldOrRedirected()).append("]\n");
        s.append("    holdRedirectDestinationCode = [").append(holdRedirectDestinationCode).append("]\n");
        s.append("    holdRedirectFromDate = [").append(holdRedirectFromDate).append("]\n");
        s.append("    holdRedirectToDate = [").append(holdRedirectToDate).append("]\n");
        s.append("    invoiceSuppressionLevel = [").append(invoiceSuppressionLevel).append("]\n");
        s.append("]");

        return s.toString();
    }
}



