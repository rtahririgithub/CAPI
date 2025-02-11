package com.telus.cmb.ldiftool.utilities;

import java.io.File;
import java.io.IOException;
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
import org.springframework.util.StringUtils;

import com.telus.cmb.ldiftool.LdifShakedown;
import com.telus.cmb.ldiftool.constants.EnvEnum;

public class LdifUtilities {

	private static Logger logger = Logger.getLogger(LdifShakedown.class);

	public static final String STAGING_URL = "http://staging-qidc.tsl.telus.com:2014";
	public static final String LOCAL_TEMP_DIR = "C:/work/infra/temp/";

	public static EnvEnum parseEnvEnum(String environment) {
		if (StringUtils.isEmpty(environment)) {
			return null;
		}
		try {
			return Enum.valueOf(EnvEnum.class, environment.toUpperCase().trim());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

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
	public static String saveLdifFile(String environment, String stagingPath) throws IOException {
		
		String ldifUrlPath = getLdifUrl(environment, stagingPath);
		String localPath = getLocalTempPath(ldifUrlPath);
		File localCopy = new File(localPath);
		URL ldifUrl = new URL(ldifUrlPath);
		FileUtils.copyURLToFile(ldifUrl, localCopy);
		
		return "file:" + localPath;
	}

	/**
	 * Transforms the ldif staging url into a local temporary file path
	 * @param ldifUrl
	 * @return
	 */
	private static String getLocalTempPath(String ldifUrl) {
		return LOCAL_TEMP_DIR + ldifUrl.replaceFirst(STAGING_URL + "/staging/", "").replace("/", "_");
	}

	public static DirContext getDirContext(EnvEnum environment) throws NamingException {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, environment.getLdapUrl());
		String authMechanism = System.getProperty("com.telusmobility.config.java.naming.security.authentication");

		if (authMechanism != null && authMechanism.length() > 0) {
			env.put(Context.SECURITY_AUTHENTICATION, authMechanism);
		}
		String authPrincipal = System.getProperty("com.telusmobility.config.java.naming.security.principal");

		if (authPrincipal != null && authPrincipal.length() > 0) {
			env.put(Context.SECURITY_PRINCIPAL, authPrincipal);
		}
		else if ( environment.getPrinciple() != null ) {
		    env.put(Context.SECURITY_AUTHENTICATION, "simple");
		    env.put(Context.SECURITY_PRINCIPAL, environment.getPrinciple());
		}
		String authCredentials = System.getProperty("com.telusmobility.config.java.naming.security.credentials");

		if (authCredentials != null && authCredentials.length() > 0) {
			env.put(Context.SECURITY_CREDENTIALS, authCredentials);
		}
		else if ( environment.getCredential() != null) {
		    env.put( Context.SECURITY_CREDENTIALS, environment.getCredential() );
		}
		return new InitialDirContext(env);
	}
	
	/**
	 * Searches in the LDAP directory for a particular attribute and value
	 * @param ctx
	 * @param ldapSearchBase
	 * @param attributeId
	 * @param attributeValue
	 * @return
	 * @throws NamingException
	 */
	public static SearchResult verifyRecord(DirContext ctx, String ldapSearchBase, String attributeId, String attributeValue) throws NamingException {

		String searchFilter = "(" + attributeId + "=" + attributeValue + ")";
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);
		NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

		SearchResult searchResult = null;
		if (results.hasMoreElements()) {
			searchResult = (SearchResult) results.nextElement();

			// there should be only one match
			if (results.hasMoreElements()) {
				logger.error("Matched multiple attribute ID for the attribute value: " + attributeValue);
				return null;
			}
		}

		return searchResult;
	}

}
