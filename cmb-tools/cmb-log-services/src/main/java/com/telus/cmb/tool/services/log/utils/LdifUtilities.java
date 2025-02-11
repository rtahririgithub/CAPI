package com.telus.cmb.tool.services.log.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.Ldap;
import com.telus.cmb.tool.services.log.domain.LdifSearchResult;
import com.telus.cmb.tool.services.log.ldif.exceptions.LdifDirectoryNotFoundException;
import com.telus.cmb.tool.services.log.ldif.exceptions.MultipleLdifMatchException;

public class LdifUtilities {

	public static final String STAGING_URL = "http://staging-qidc.tsl.telus.com:2014";
	public static final String LDAP_CONTEXT_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	private static Logger logger = Logger.getLogger(LdifUtilities.class);
	
	private LdifUtilities() {}
	
	/**
	 * Transforms the ldif staging path into a URL friendly url from which the ldif can be accessed
	 * @param environment
	 * @param stagingPath
	 * @return
	 */
	public static String getLdifUrl(String environment, String stagingPath) {
		String ldifPath = stagingPath;
		ldifPath = ldifPath.replaceFirst("/work", STAGING_URL);
		ldifPath = ldifPath.replaceAll(Pattern.quote("$env"), environment);
		return ldifPath;
	}

	/**
	 * Saves the ldif file in a temporary local directory from the staging url passed in and returns the local file path in URL format (prepended with 'file:')
	 * @param ldifPath The url of where the ldif is stored in the staging build environment
	 * @return The local filepath prefixed with "file:"
	 * @throws IOException
	 */
	public static String saveLdifFile(Environment environment, String stagingPath, String localPath) throws IOException {
		
		String ldifUrlPath = getLdifUrl(getLdapEnvName(environment), stagingPath);
		String localLdifPath = getLocalLdifPath(ldifUrlPath, localPath);
		File localCopy = new File(localLdifPath);
		URL ldifUrl = new URL(ldifUrlPath);
		FileUtils.copyURLToFile(ldifUrl, localCopy);
		
		return localLdifPath;
	}

	/**
	 * Transforms the ldif staging url into a local temporary file path
	 * @param ldifUrl
	 * @return
	 */
	private static String getLocalLdifPath(String ldifUrl, String localPath) {
		return localPath + "/" + ldifUrl.replaceFirst(STAGING_URL + "/staging/", "").replace("/", "_");
	}
	
	/**
	 * Method to retrieve the environment name for LDAP url purposes - need to remove flipper side if enabled
	 * @param environment
	 * @return
	 */
    public static String getLdapEnvName(Environment environment) {
		return environment.getLdap().isFlipper() ? environment.getShortname().substring(0, environment.getShortname().length() - 1) : environment.getShortname();
	}
	
    /**
     * Method to retrieve the flipper side of a flipper enabled environment (returns blank of is not enabled for flipper)
     * @param environment
     * @return
     */
	public static String getLdapFlipperSide(Environment environment) {
		return environment.getLdap().isFlipper() ? environment.getShortname().substring(environment.getShortname().length() - 1) : "";
	}

	/**
	 * Method to get an updated value for staging alias values
	 * @param value
	 * @param environment
	 * @param useAlias
	 * @return
	 */
	public static String getFlipperUpdatedValue(String value, String env, String flipperSide) {
		return value.replaceFirst(Pattern.quote(env + "."), env + "-" + flipperSide + ".");
	}
	
	/**
	 * Method to get a specific DirContext based on ldap settings (of an environment)
	 * @param environment
	 * @return
	 * @throws NamingException
	 */
	public static DirContext getDirContext(Ldap ldap) throws NamingException {
		
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_CONTEXT_FACTORY);
		env.put(Context.PROVIDER_URL, ldap.getUrl());				
		if (ldap.getPrincipal() != null) {
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, ldap.getPrincipal());
		}		
		if (ldap.getCredential() != null) {
			env.put(Context.SECURITY_CREDENTIALS, ldap.getCredential());
		}

		return new InitialDirContext(env);
	}
	
	/**
	 * Gets the value from a NamingEnumeration value list - supports strings and blobs (UTF-8 byte format)
	 * @param valueList
	 * @return
	 * @throws NamingException
	 * @throws UnsupportedEncodingException
	 */
	public static String getAttributeValue(NamingEnumeration<?> valueList) throws NamingException, UnsupportedEncodingException {
		Object o = valueList.next();
		String value;
		if (o instanceof byte[]) {
			value = new String((byte[]) o, "UTF-8");
		} else {
			value = (String) o;
		}
		return value;
	}
	
	/**
	 * Searches in the LDAP directory for a particular attribute and value
	 * 
	 * @param ctx
	 * @param ldapSearchBase
	 * @param attributeId
	 * @param attributeValue
	 * @return
	 * @throws NamingException
	 * @throws MultipleLdifMatchException
	 */
	public static SearchResult verifyRecord(DirContext ctx, String ldapSearchBase, String attributeId, String attributeValue) throws LdifDirectoryNotFoundException, MultipleLdifMatchException {

		SearchResult searchResult = null;
		String searchFilter = "(" + attributeId + "=" + attributeValue + ")";
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);
		
		try {
			NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);
			if (results.hasMoreElements()) {
				searchResult = (SearchResult) results.nextElement();
	
				// There should be only one match
				if (results.hasMoreElements()) {
					logger.error("Matched multiple attribute ID for the attribute value: " + attributeValue);
					throw new MultipleLdifMatchException(new LdifSearchResult("Matched multiple attribute ids", ldapSearchBase, attributeId, attributeValue));
				}
			}
		} catch (NamingException e) {
			logger.error("Naming Exception occurred when verifying record on ldapSearchBase=["+ ldapSearchBase + "] Exception=" + e.getMessage());
			throw new LdifDirectoryNotFoundException(new LdifSearchResult("Couldn't find ldap directory", ldapSearchBase, attributeId, attributeValue));
		}

		return searchResult;
	}
}
