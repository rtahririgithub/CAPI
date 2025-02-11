package com.telus.api.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.*;

public interface TaxExemption {
    boolean isGSTExempt();
    boolean isPSTExempt();
    boolean isHSTExempt();
    Date getGSTExemptEffectiveDate();
    Date getPSTExemptEffectiveDate();
    Date getHSTExemptEffectiveDate();
    Date getGSTExemptExpiryDate();
    Date getPSTExemptExpiryDate();
    Date getHSTExemptExpiryDate();
    String getGSTCertificateNumber();
    String getPSTCertificateNumber();
    String getHSTCertificateNumber();
    
    void isGSTExempt(boolean val);
    void isPSTExempt(boolean val);
    void isHSTExempt(boolean val);

    void setGSTExemptEffectiveDate(Date effDate);
    void setPSTExemptEffectiveDate(Date effDate);
    void setHSTExemptEffectiveDate(Date effDate);
    void setGSTExemptExpiryDate(Date expDate);
    void setPSTExemptExpiryDate(Date expDate);
    void setHSTExemptExpiryDate(Date expDate);
    void setGSTCertificateNumber(String certificate);
}
