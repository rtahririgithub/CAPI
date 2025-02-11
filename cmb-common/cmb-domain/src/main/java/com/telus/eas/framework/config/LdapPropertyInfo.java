package com.telus.eas.framework.config;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LdapPropertyInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String[] ldapPath;
	private String systemPropertyName;
	private Object[] ldapValue;
	private Object[] overrideValue;
	private Map parsedArrayValueMap = Collections.synchronizedMap(new HashMap());
	private Map parsedValueMap =  Collections.synchronizedMap(new HashMap());
	private Date lastUpdate;
	private Date overrideEffectiveDate;
	private Date overrideExpiryDate;
	
	public LdapPropertyInfo() {
		
	}
	
	public String[] getLdapPath() {
		return ldapPath;
	}
	public void setLdapPath(String[] ldapPath) {
		this.ldapPath = ldapPath;
	}
	public String getSystemPropertyName() {
		return systemPropertyName;
	}
	public void setSystemPropertyName(String systemPropertyName) {
		this.systemPropertyName = systemPropertyName;
	}
	public Object[] getLdapValue() {
		return ldapValue;
	}
	public void setLdapValue(Object[] value) {
		this.ldapValue = value;
	}
	/**
	 * @param overrideValue
	 * @param effectiveDate null means it's effective immediately
	 * @param expiryDate    null means the value won't expire.
	 */
	public void setOverrideValue(Object[] overrideValue, Date effectiveDate, Date expiryDate) {
		this.overrideValue = overrideValue;
		this.overrideEffectiveDate = effectiveDate;
		this.overrideExpiryDate = expiryDate;
		clearParsedCache(); //clear cache
	}
	
	public void clearOverride() {
		this.overrideValue = null;
		this.overrideEffectiveDate = null;
		this.overrideExpiryDate = null;
		clearParsedCache();
	}

	public Object[] getValue() {
		Object[] value = null;
		
		value = getOverrideValue(); //the overridden value cannot never be null.
		
		if (value == null && systemPropertyName != null) {
			String sysPropValue = System.getProperty(systemPropertyName);
			if (sysPropValue != null) {
				value = new Object[1];
				value[0] = sysPropValue;
			}
		}
		
		if (value == null) {
			return ldapValue;
		}
		
		return value;
	}
	
	private Object[] getOverrideValue() {	
		if (overrideValue != null) {
			if (isEffective(overrideEffectiveDate) && isExpired(overrideExpiryDate) == false) {
				return overrideValue;
			}
		}
		
		return null;
	}
	
	private boolean isEffective(Date date) {
		if (date == null) {
			return true;
		}
		
		Calendar now = Calendar.getInstance();
		Calendar effectiveCalendar = Calendar.getInstance();
		effectiveCalendar.setTime(date);

		return effectiveCalendar.getTimeInMillis() <= now.getTimeInMillis();
	}
	
	private boolean isExpired(Date date) {
		if (date == null) {
			return false;
		}
		
		Calendar now = Calendar.getInstance();
		Calendar expiryCalendar = Calendar.getInstance();
		expiryCalendar.setTime(date);

		return expiryCalendar.getTimeInMillis() <= now.getTimeInMillis();
	}
	

	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public void setParsedValue (Class clazz, Object value) {
		parsedValueMap.put(clazz, value);
	}
	
	public void setParsedValue (Class clazz, Object[] value) {
		parsedArrayValueMap.put(clazz, value);
	}
	
	public Object getParsedValue(Class clazz) {
		if (isOverrideExpired()) {
			clearOverride();
		}
		return parsedValueMap.get(clazz);
	}
	
	public Object[] getParsedArrayValue(Class clazz) {
		if (isOverrideExpired()) {
			clearOverride();
		}
		return (Object[]) parsedArrayValueMap.get(clazz);
	}
	
	public void clearParsedCache() {
		parsedValueMap.clear();
		parsedArrayValueMap.clear();
	}
	
	private boolean isOverrideExpired() {
		return (overrideValue != null && getOverrideValue() == null);
	}
}
