package com.telus.cmb.common.ejb;

import java.util.Date;

public interface LdapTestPoint {
    void overrideLdapValue(String key, Object value, Date effectiveDate, Date expiryDate);
    void clearLdapOverride(String key);
    void refresh();
    String getStringValue(String key);
}
