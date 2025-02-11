package com.telus.eas.framework.config;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Codes in this class can be simplified if this class is migrated to JDK5+ completely.
 * @author tongts
 *
 */

public abstract class BaseLdapConfigurationManager {

	protected Map<String, LdapPropertyInfo> registrationMap = Collections.synchronizedMap(new HashMap<String, LdapPropertyInfo>());
	protected LdapReader ldapReader;
	protected static final int SINGLE_REFRESH_INTERVAL_MINUTE = 5;
	protected static final int COMPLETE_REFRESH_INTERVAL_MINUTE = 60;
	private static final ValueParser stringValueParser = ValueParserFactory.getStringValueParser();
	
	private volatile Thread thread;
	
	public BaseLdapConfigurationManager(LdapReader ldapReader, Map ldapPathMap) {
		this(ldapReader, ldapPathMap, null);
	}
	
	public BaseLdapConfigurationManager(LdapReader ldapReader, Map ldapPathMap, Map systemPropertyMap) {
		this.ldapReader = ldapReader;
		loadValues(ldapPathMap, systemPropertyMap);
		scheduleRefresh();
	}

	protected void loadValues(Map ldapPathMap, Map systemPropertyMap) {
		Map valueMap = ldapReader.getValues(ldapPathMap);
		
		Set keys = ldapPathMap.keySet();
		Iterator iterator = keys.iterator();
		
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			LdapPropertyInfo ldapInfo = new LdapPropertyInfo();
			ldapInfo.setLdapPath((String[]) ldapPathMap.get(key));
			if (systemPropertyMap != null) {
				ldapInfo.setSystemPropertyName((String) systemPropertyMap.get(key));
			}
			ldapInfo.setLdapValue((Object[]) valueMap.get(key));
			ldapInfo.setLastUpdate(new Date());
			registrationMap.put(key, ldapInfo);
		}
	}

	protected Object[] getValue(String key) {
		LdapPropertyInfo ldapProperty = registrationMap.get(key);
		if (ldapProperty != null) {
			Object[] value = ldapProperty.getValue();
			
			if (value != null) {
				return value;
			}else {
				refreshSingleEntry(ldapProperty);
				return ldapProperty.getValue();
			}
		}
		
		return null;
	}

	protected synchronized void refreshSingleEntry(LdapPropertyInfo ldapProperty) {
		// single refresh is applicable only if there exists a ldap path and ldap value is null.
		if (ldapProperty.getLdapPath() != null && ldapProperty.getLdapValue() == null && isSingleRefreshEligible(ldapProperty.getLastUpdate())) {
			try {
				Object[] value = ldapReader.getValue(ldapProperty.getLdapPath());
				ldapProperty.setLdapValue(value);
			} catch (Exception e) {
				//error reading from ldap. entry DNE?
			} finally {
				//update the last attempt timestamp
				ldapProperty.setLastUpdate(new Date());
			}
		}
	}

	private static boolean isSingleRefreshEligible(Date time) {
		if (time == null) {
			return true;
		}
		
		Calendar now = Calendar.getInstance();
		Calendar date1 = Calendar.getInstance();
		
		date1.setTime(time);
		date1.add(Calendar.MINUTE, SINGLE_REFRESH_INTERVAL_MINUTE);
		
		return now.getTimeInMillis() >= date1.getTimeInMillis();
	}

	protected Map getLdapPathMap() {
		Set<String> keySet = registrationMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		Map<String, String[]> ldapPathMap = new HashMap<String, String[]>();
		
		while (iterator.hasNext()) {
			String key = iterator.next();
			LdapPropertyInfo ldapInfo = registrationMap.get(key);
			if (ldapInfo.getLdapPath() != null) {
				ldapPathMap.put(key, ldapInfo.getLdapPath());
			}
		}
		
		return ldapPathMap;
	}
	
	public LdapPropertyInfo getLdapPropertyInfo(String key) {
		return registrationMap.get(key);
	}

	protected void cacheParsedValue(String key, Class clazz, Object value) {
		LdapPropertyInfo info = getLdapPropertyInfo(key);
		if (info != null) {
			info.setParsedValue(clazz, value);
		}
	}

	protected void cacheParsedValue(String key, Class clazz, Object[] values) {
		LdapPropertyInfo info = getLdapPropertyInfo(key);
		if (info != null) {
			info.setParsedValue(clazz, values);
		}
	}

	protected Object getParsedValue(String key, Class clazz) {
		LdapPropertyInfo info = getLdapPropertyInfo(key);
		if (info != null) {
			return info.getParsedValue(clazz);
		}
		return null;
	}

	protected Object[] getParsedArrayValue(String key, Class clazz) {
		LdapPropertyInfo info = getLdapPropertyInfo(key);
		if (info != null) {
			return info.getParsedArrayValue(clazz);
		}
		return null;
	}

	protected Object get(String key, Class clazz, ValueParser parser, Object defaultValue) {
		Object result = get(key, clazz, parser);
		
		if (result == null && defaultValue != null) {
			result = defaultValue;
			cacheParsedValue(key, clazz, result);
		}
		
		return result;	
	}

	protected Object get(String key, Class clazz, ValueParser parser) {
		Object result = getParsedValue(key, clazz);
		
		if (result == null) {
			String value = getStringValue(key);
			
			if (value != null) {
				result = parser.parse(value);
				cacheParsedValue(key, clazz, result);
			}
		}
		
		return result;
	}
	
	public String getStringValue(String key) {
		String result = (String) getParsedValue(key, String.class);

		if (result == null) {
			Object[] value = getValue(key);

			if (value != null) {
				if (value.length == 1) {
					result = value[0].toString();
				} else {
					StringBuffer str = new StringBuffer();
					for (int i = 0; i < value.length; i++) {
						String s = (String) value[i];
						if (str.length() > 0) {
							str.append(",");
						}
						str.append(s);
					}

					result = str.toString();
				}

				if (result != null) {
					cacheParsedValue(key, String.class, result);
				}
			}
		}

		return result;
	}

	protected Object[] getArray(String key, Class clazz, ValueParser parser) {		
		Object[] result = getParsedArrayValue(key, clazz);
		
		if (result == null) {
			Object[] values = getValue(key);
			
			if (values != null) {
				result = (Object[]) Array.newInstance(clazz, values.length);
				
				for ( int i = 0 ; i < values.length; i++) {
					result[i] = parser.parse(values[i]);
				}
				
				cacheParsedValue(key, clazz, result);
			}else {
				result = (Object[]) Array.newInstance(clazz, 0);
			}
		}
		
		return result;
	}
	
	protected List getValueAsList(String key, String delimiter, ValueParser parser) {
		String value = getStringValue(key);
		List valueList = null;
		
		if (value != null) {
			String[] arrayValue = value.split(delimiter);
			valueList = new ArrayList();
			for (int i = 0 ; i < arrayValue.length; i++) {
				valueList.add(parser.parse(arrayValue[i]));
			}
		}
		
		return valueList;
	}

	protected synchronized void scheduleRefresh() {
		if (this.thread == null) {
			this.thread = new Thread("LdapDataRefreshThread") {
				
				public void run() {
					boolean keepRunning = true;
					
					System.out.println("Starting ldap refresh thread..");
					
					while (keepRunning) {
						try {
							sleep(COMPLETE_REFRESH_INTERVAL_MINUTE * 60 * 1000);
							refresh();
						} catch (InterruptedException ie) {
							keepRunning = false;
						} catch (Throwable e) {
							System.out.println("Error refreshing LDAP: " + e);
							e.printStackTrace();
						}
					}
					
					System.out.println("Exiting ldap refresh thread..");
				};
			};
			thread.setDaemon(true);
			thread.start();
		}
	}

	public String[] getKeys() {
		Set keys = registrationMap.keySet();
		return (String[]) keys.toArray(new String[keys.size()]);
	}
	
	public LdapPropertyInfo[] getAll() {
		Collection valueList = registrationMap.values();
		
		return (LdapPropertyInfo[]) valueList.toArray(new LdapPropertyInfo[valueList.size()]);
	}
	
	protected ValueParser getStringValueParser() {
		return stringValueParser;
	}

	public synchronized void refresh() {
		Map ldapPathMap = getLdapPathMap();
		Map valueMap = ldapReader.getValues(ldapPathMap);
		
		Set keySet = valueMap.keySet(); //update ldap entries that were retrieved successfully only.
		Iterator iterator = keySet.iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			LdapPropertyInfo ldapInfo = (LdapPropertyInfo) registrationMap.get(key);
			ldapInfo.setLdapValue((Object[]) valueMap.get(key));
			ldapInfo.setLastUpdate(new Date());
			ldapInfo.clearParsedCache();
		}
	}

	protected void overrideValue(String key, Object[] overrideValue, Date effectiveDate, Date expiryDate) {
		if (key != null && overrideValue != null) {
			LdapPropertyInfo ldapInfo = (LdapPropertyInfo) registrationMap.get(key);

			if (ldapInfo != null) {
				ldapInfo.setOverrideValue(overrideValue, effectiveDate, expiryDate);
			}
		}
	}
	
	protected void setOverrideValue(String key, Object overrideValue, Date effectiveDate, Date expiryDate) {
		if (key != null && overrideValue != null) {
			Object[] values = new Object[1];
			
			values[0] = String.valueOf(overrideValue);
			overrideValue (key, values, effectiveDate, expiryDate);
		}
	}
	
	public void clearOverride(String key) {
		if (key != null) {
			LdapPropertyInfo ldapInfo = (LdapPropertyInfo) registrationMap.get(key);

			if (ldapInfo != null) {
				ldapInfo.clearOverride();
			}
		}
	}
	
	public synchronized void destroy() {
		System.out.println("Shutting down ConfigurationManagerImpl..");
		if (this.thread != null) {
			try {
				this.thread.interrupt();
			}catch (Throwable t) {
				System.out.println("Error destroying refresh thread.." + t);
			}
			this.thread = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			super.finalize();
			destroy();
		} catch (Throwable t) {
			System.out.println("Error cleaning up refresh thread.." + t);
		}
	}
}
