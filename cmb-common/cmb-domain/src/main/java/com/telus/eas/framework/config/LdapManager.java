package com.telus.eas.framework.config;

import java.util.Date;
import java.util.List;

public interface LdapManager {
	String[] getKeys();
	Boolean getBooleanValue(String key);
	Boolean getBooleanValue(String key, boolean defaultValue);
	Boolean getBooleanValue(String key, Boolean defaultValue);
	Date getDateValue(String key);
	Date getDateValue(String key, Date defaultValue);
	Integer getIntegerValue(String key);
	Integer getIntegerValue(String key, int defaultValue);
	Integer getIntegerValue(String key, Integer defaultValue);
	Long getLongValue(String key);
	Long getLongValue(String key, long defaultValue);
	Long getLongValue(String key, Long defaultValue);
	String[] getStringArrayValues(String key);
	String[] getStringArrayValues(String key, String defaultValue);
	String[] getStringArrayValues(String key, String[] defaultArrayValues);
	String getStringValue(String key);
	String getStringValue(String key, String defaultValue);
	List getValueAsList(String key, String delimiter, List defaultList, ValueParser parser);
	void refresh();
	void overrideValue(String key, Object overrideValue, Date effectiveDate, Date expiryDate);
	void overrideValue(String key, boolean overrideValue, Date effectiveDate, Date expiryDate);
	void overrideValue(String key, int overrideValue, Date effectiveDate, Date expiryDate);
	void overrideValue(String key, long overrideValue, Date effectiveDate, Date expiryDate);
	void clearOverride(String key);
	void destroy();
}
