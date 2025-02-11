package com.telus.eas.framework.config;

import java.util.Map;

public interface LdapReader {
	/**
	 * This method should retrun value of the LDAP path specified in the parameter.
	 * 
	 * @param path
	 * @return
	 */
	Object[] getValue(String [] path);
	
	/**
	 * This method should return values for LDAP paths specified in String[].
	 * 
	 * @param keyPathPairs Map<String, String[]> (key name, ldap path)
	 * @return Map<String, Object> (key name, value from ldap path). value is null if path is invalid.
	 */
	Map getValues(Map keyPathPairs);
}
