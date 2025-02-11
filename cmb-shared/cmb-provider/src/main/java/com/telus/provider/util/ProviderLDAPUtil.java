package com.telus.provider.util;


/**
 * @deprecated Use AppConfiguration instead. This class should be removed.
 * @author tongts
 *
 */
public class ProviderLDAPUtil {
//	
//	private static Map variableMap = Collections.synchronizedMap(new HashMap());
//
//	public static boolean getPropertyValueFromLDAP(String[] path, String propertyName, boolean defaultValue) throws TelusAPIException{			
//		synchronized (variableMap) {	
//			String value = (String) variableMap.get(propertyName);
//			if (value == null) {
//				boolean booleanValue = defaultValue;
//				ConfigurationManager configurationManager = null;
//				try {
//					configurationManager = ConfigurationManager.getInstance();
//					Configuration cconf = configurationManager.lookup(path);
//					booleanValue = cconf.getPropertyAsBoolean(propertyName, defaultValue);
//					
//				} catch (ConfigurationException e) {
//					throw new TelusAPIException("ConfigurationException occurred - could not load configuration parameters!", e);
//				} finally {
//					if (configurationManager != null) {
//						configurationManager.destroy();
//					}
//				}
//				value = String.valueOf(booleanValue);
//				variableMap.put(propertyName, value);
//			}
//			Logger.debug("Value for propertyName [" + propertyName + "] is [" + value + "]");
//			return Boolean.valueOf(value).booleanValue();
//		}
//	}
	
}
