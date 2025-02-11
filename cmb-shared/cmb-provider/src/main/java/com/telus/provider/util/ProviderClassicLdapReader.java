package com.telus.provider.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.telus.api.config.Configuration;
import com.telus.api.config.ConfigurationManager;
import com.telus.eas.framework.config.LdapReader;

/**
 * This class uses EM's ConfigurationManager API to retrieve LDAP entries.
 * @author Tsz Chung Tong
 *
 */
public class ProviderClassicLdapReader implements LdapReader {

	public ProviderClassicLdapReader() {
	}

	public Object[] getValue(String[] path) {
		String[] subPath = new String[path.length];
		System.arraycopy(path, 0, subPath, 0, path.length-1);
		
		ConfigurationManager configManager = null;
		try {
			configManager = ConfigurationManager.getInstance();
			Configuration config = configManager.lookup(subPath);
			Object[] values = config.getPropertyAsStringArray(path[path.length-1]);
			return values;
		}
		catch( Exception e ) {
			Logger.warning("encounter error while lookingup " + createDnPath(path) );
			throw new RuntimeException( e);
		} finally {
			if (configManager != null) {
				configManager.destroy();
			}
		}
	}

	public Map getValues(Map keyPathPairs) {
		Map keyPathPairsMap = keyPathPairs;
		Map valueMap = new HashMap();
		
		Set keys = keyPathPairs.keySet();
		ConfigurationManager configManager = null;

		try {
			configManager = ConfigurationManager.getInstance();
			Map configCacheMap = new HashMap();
			Iterator keysIterator = keys.iterator();
			while (keysIterator.hasNext()) {
				String key = (String) keysIterator.next();
				String[] path = (String[]) keyPathPairsMap.get(key);
				try {
					String[] subPath = getSubPath(path);
					final String subPathDn = createDnPath(subPath);
					Configuration config = (Configuration) configCacheMap.get(subPathDn);
					if (config == null) {
						config = configManager.lookup(subPath);
						configCacheMap.put(subPathDn, config);
					}
					Object[] values = config.getPropertyAsStringArray(path[path.length-1].trim());
					valueMap.put(key, values);
					Logger.debug("Loaded "+ createDnPath(path));
				} catch (Exception e) {
					Logger.warning("Error retrieving value for DN [" + createDnPath(path) + "]: [" + e+"]");
					//log only. do not stop processing for the rest of entries.
				} 
			}
		} catch (Exception e) {
			Logger.warning("Unexpected error in ProviderClassicLdapReader. " +e);
		} finally {
			if (configManager != null) {
				configManager.destroy();
			}
		}
		
		return valueMap;
	}

	protected static String createDnPath (String[] path) {
		StringBuffer buffer = new StringBuffer();
		for (int idx = path.length - 1; idx >= 0; idx--) {
			if (idx != path.length - 1) {
				buffer.append(", ");
			}
			buffer.append("cn=").append(path[idx].trim());
		}
		
		return buffer.toString();
	}
	
	protected static String[] getSubPath(String[] path) {
		if (path != null && path.length > 1) {
			String[] subPath = new String[path.length-1];
			
			System.arraycopy(path, 0, subPath, 0, path.length-1);
			
			return subPath;
		}
		
		return path;
	}
	
}
