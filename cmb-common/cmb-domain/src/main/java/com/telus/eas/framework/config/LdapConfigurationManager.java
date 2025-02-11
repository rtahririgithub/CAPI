package com.telus.eas.framework.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Codes in this class can be simplified if this class is migrated to JDK5+ completely.
 * @author tongts
 *
 */

public class LdapConfigurationManager extends BaseLdapConfigurationManager implements LdapManager {
	
	public LdapConfigurationManager(LdapReader ldapReader, Map ldapPathMap) {
		super(ldapReader, ldapPathMap, null);
	}
	
	public LdapConfigurationManager(LdapReader ldapReader, Map ldapPathMap, Map systemPropertyMap) {
		super (ldapReader, ldapPathMap, systemPropertyMap);
	}
	
	public String getStringValue(String key, String defaultValue) {
		return (String) get(key, String.class, getStringValueParser(), defaultValue);
	  }


	public Boolean getBooleanValue(String key) {
		return getBooleanValue(key, null);
	}
	
	public Boolean getBooleanValue(String key, boolean defaultValue) {
		return getBooleanValue(key, Boolean.valueOf(defaultValue));
	}
	
	public Boolean getBooleanValue(String key, Boolean defaultValue) {
		return (Boolean) get(key, Boolean.class, new ValueParser() {
			
			public Object parse(Object value) {
				return Boolean.valueOf(value.toString());
			}
		}, defaultValue);
	}
	
	protected abstract class LdapParsedValueWithDefaultCallBack {
		abstract Object getDefault(String key);
				
		public Object get(String key, Class clazz, Object defaultValue) {
			Object result = getDefault(key);
			
			if (result == null && defaultValue != null) {
				result = defaultValue;
				cacheParsedValue(key, clazz, result);
			}
			
			return result;	
		}
	}
	
	public Date getDateValue(String key) {
		return getDateValue(key, null);
	}
	
	public Date getDateValue(String key, Date defaultValue) {
		return (Date) get(key, Date.class, new ValueParser() {
			
			public Object parse(Object value) {
				return new Date(value.toString());
			}
		}, defaultValue);
	}
		
	public Integer getIntegerValue(String key) {
		return getIntegerValue(key, null);
	}
	
	public Integer getIntegerValue(String key, int defaultValue) {
		return getIntegerValue(key, new Integer(defaultValue));
	}
	
	public Integer getIntegerValue(String key, Integer defaultValue) {
		return (Integer) get(key, Integer.class, new ValueParser() {
			
			public Object parse(Object value) {
				return Integer.valueOf(value.toString());
			}
		}, defaultValue);

	}
	
	public Long getLongValue(String key) {
		return getLongValue(key, null);
	}
	
	public Long getLongValue(String key, long defaultValue) {
		return getLongValue(key, new Long(defaultValue));
	}
	
	public Long getLongValue(String key, Long defaultValue) {
		return (Long) get(key, Long.class, new ValueParser() {
			
			public Object parse(Object value) {
				return Long.valueOf(value.toString());
			}
		}, defaultValue);
	}
	
	public String[] getStringArrayValues(String key, String defaultValue) {
		if (defaultValue == null) {
			defaultValue = "";
		}
		
		String[] values = getStringArrayValues(key);
		
		if (values == null || values.length == 0) {
			values = new String[1];
			values[0] = defaultValue;
		}
		
		return values;
	}
	
	public String[] getStringArrayValues(String key, String[] defaultArrayValues) {	
		String[] values = getStringArrayValues(key);
		
		if (values == null || values.length == 0) {
			values = defaultArrayValues;
		}
		
		return values;
	}

	public String[] getStringArrayValues(String key) {
		return (String[]) getArray(key, String.class, new ValueParser() {

			public Object parse(Object value) {
				return value.toString();
			}
		});
	}
	
	public List getValueAsList(String key, String delimiter, List defaultList, ValueParser parser) {
		List valueList = getValueAsList (key, delimiter, parser);
		
		if (valueList != null) {
			return valueList;
		}
		
		return defaultList;
	}

	public void overrideValue(String key, Object overrideValue, Date effectiveDate, Date expiryDate) {	
		setOverrideValue(key, overrideValue, effectiveDate, expiryDate);
	}
	
	public void overrideValue(String key, boolean overrideValue, Date effectiveDate, Date expiryDate) {	
		setOverrideValue(key, String.valueOf(overrideValue), effectiveDate, expiryDate);
	}
	
	public void overrideValue(String key, int overrideValue, Date effectiveDate, Date expiryDate) {
		setOverrideValue(key, String.valueOf(overrideValue), effectiveDate, expiryDate);
	}

	public void overrideValue(String key, long overrideValue, Date effectiveDate, Date expiryDate) {
		setOverrideValue(key, String.valueOf(overrideValue), effectiveDate, expiryDate);
	}




	
}
