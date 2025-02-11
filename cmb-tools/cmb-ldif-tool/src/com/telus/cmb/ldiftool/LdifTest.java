package com.telus.cmb.ldiftool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.ldap.core.LdapAttribute;
import org.springframework.ldap.core.LdapAttributes;
import org.springframework.ldap.ldif.InvalidAttributeFormatException;
import org.springframework.ldap.ldif.parser.LdifParser;
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class LdifTest {

	private static final String[] VALID_ENV = {"dv103", "pt140", "pt148", "pt168", "st101a", "st101b", "pra", "prb"};
	private static final Boolean SUPPRESS_FLIPPER_WARNING = true;
	private static final String RAPID_CONNECTION_STRING = "jdbc:mysql://emtools.tmi.telus.com:3306/";
	private static final String RAPID_USER = "envmatrixall";
	private static final String RAPID_PASSWORD = "envmatrixall";	
//	50298 - 2017 Feb Wireless Major
//	50299 - 2017 Apr Wireless Major
//	50300 - 2017 Jul Wireless Major
	private static final String QUERY_STRING = "select buildpath from rapidpr.rpd_deployment where release_id = '50299' and application_id in ('550', '445', '563') and valid = 1 and buildpath like '%ldif';";

	private static Logger logger = Logger.getLogger(LdifTest.class);

	/**
	 * LdifTest - 
	 *  INPUT: first arg is environment, second arg is whether to use *\LdifTool\resource\ldif-list.txt in place of RAPiD DB
	 * OUTPUT: C:/rolling.log and C:/error.log
	 */
	public static void main(String[] args) throws Throwable {
		List<String> validEnv = Arrays.asList(VALID_ENV);
		String env = args[0].toLowerCase();
		Boolean useExternalSourceFileIndicator = Boolean.parseBoolean(args[1]);
		if (!validEnv.contains(env)) {
			logger.error(env + " is an invalid environment, please choose from " + validEnv.toString());
			return;
		}
		logger.info("Shakedown Start!");
		process(env, useExternalSourceFileIndicator);
		logger.info("Shakedown Complete!");
	}

	private static void process(String env, Boolean useExternalSourceFileIndicator) throws NamingException, FileNotFoundException, IOException, SQLException {
		DirContext ctx = connectToLdap(env);
		
		String side = "";
		if (env.equals("pra") || env.equals("prb") || env.equals("st101a") || env.equals("st101b")) {
			side = env.substring(env.length()-1);			
			env = env.substring(0,  env.length()-1);
		} 
		
		List<String> buildFiles = (useExternalSourceFileIndicator) ? parseBuildFilesFromTextFile(env): parseBuildFilesFromRapidDb(env);
		for (String buildFile : buildFiles) {
			logger.info("Shaking Down " + buildFile);
			shakedownLdif(buildFile, ctx, env, side);
			logger.info(buildFile + " Completed!");
		}
	}


	private static DirContext connectToLdap(String environment)
			throws NamingException {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		
		String url;
		if (environment.equals("dv103")) {
			url = "ldapread-d3.tmi.telus.com";
		} else if (environment.equals("pt140")) {
			url = "ldap://ldapread-qa2.tmi.telus.com:1489";
		} else if (environment.equals("pt148")) {
			url = "ldap://ldapread-qa.tmi.telus.com:589";
		} else if (environment.equals("pt168")) {
			url = "ldap://ldapread-pt168.tmi.telus.com:589";
		} else if (environment.equals("st101a") || environment.equals("st101b") ) {
			url = "ldap://ldapread-" + environment + ".tmi.telus.com:1589/";
		} else if (environment.equals("pra") || environment.equals("prb") ){
			url = "ldap://ldapread-" + environment + ".tmi.telus.com:389/";
		} else {
			logger.error(environment + " is an invalid environment.");
			return null;
		}
		
		env.put(Context.PROVIDER_URL, url);
		String authMechanism = System.getProperty("com.telusmobility.config.java.naming.security.authentication");

		if (authMechanism != null && authMechanism.length() > 0) {
			env.put(Context.SECURITY_AUTHENTICATION, authMechanism);
		}
		String authPrincipal = System.getProperty("com.telusmobility.config.java.naming.security.principal");

		if (authPrincipal != null && authPrincipal.length() > 0) {
			env.put(Context.SECURITY_PRINCIPAL, authPrincipal);
		}
		String authCredentials = System.getProperty("com.telusmobility.config.java.naming.security.credentials");

		if (authCredentials != null && authCredentials.length() > 0) {
			env.put(Context.SECURITY_CREDENTIALS, authCredentials);
		}
		DirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
		} finally {

		}
		return ctx;
	}

	private static List<String> parseBuildFilesFromTextFile(String env) {
		File file = new File(LdifTest.class.getResource("/ldif-list.txt").getFile());
		List<String> ldifList = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(file);
			String ldifPath;
			while (scanner.hasNextLine()) {
				ldifPath = scanner.nextLine();
				if (!StringUtils.isEmpty(ldifPath)) {
					ldifPath = ldifPath.replaceFirst("/work", "http://staging-qidc.tsl.telus.com:2014");
					ldifPath = ldifPath.replaceAll(Pattern.quote("$env"), env);
					ldifList.add(ldifPath);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}

		return ldifList;
	}
	
	
	private static List<String> parseBuildFilesFromRapidDb(String env) throws SQLException {
		Connection con = null;
		List<String> ldifList = new ArrayList<String>();

		try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con=(Connection) DriverManager.getConnection(RAPID_CONNECTION_STRING,RAPID_USER,RAPID_PASSWORD);  
			Statement stmt=(Statement) con.createStatement();  
			ResultSet rs=stmt.executeQuery(QUERY_STRING);
			while(rs.next())  {
				String ldifPath = rs.getString(1);
				ldifPath = ldifPath.replaceFirst("/work", "http://staging-qidc.tsl.telus.com:2014");
				ldifPath = ldifPath.replaceAll(Pattern.quote("$env"), env);
				ldifList.add(ldifPath);
			}
		}catch(Exception e){ 
			System.out.println(e); 
		}finally{
			con.close(); 
		}
		return ldifList;
	}	

	private static void shakedownLdif(String buildFile, DirContext ctx, String env, String side) throws NamingException, IOException {
		ApplicationContext context;
		String ldapSearchBase;

		try {
			LdifTest ldifTest = new LdifTest();
			context = new ClassPathXmlApplicationContext("application-context.xml");
			Resource resource = context.getResource(buildFile);
			LdifParser parser = new LdifParser(resource);
			parser.open();

			while (parser.hasMoreRecords()) {
				LdapAttributes attr = parser.getRecord();
				LdapName ldapName = attr.getName();
				ldapSearchBase = env.equals("st101") || env.equals("pr") ? ldapName.toString() + "_" + side : ldapName.toString();
				logger.info("LDAP Search Base: " + ldapSearchBase);
				NamingEnumeration<Attribute> attributeList = attr.getAll();
				while (attributeList.hasMore()) {
					LdapAttribute attribute = (LdapAttribute) attributeList.next();
					logger.info("Attribute ID: " + attribute.getID());
					NamingEnumeration<?> valueList = attribute.getAll();
					SearchResult result;
					while (valueList.hasMore()) {
						Object o = valueList.next();
						String value;
						if (o instanceof byte[]) {
							value = new String((byte[]) o, "UTF-8");
						} else {
							value = (String) o;
						}
						logger.info("Value Name: " + value);
						result = ldifTest.verifyRecord(ctx, ldapSearchBase,	attribute.getID(), value);

						if (result == null) {
							if (SUPPRESS_FLIPPER_WARNING) {
								result = ldifTest.verifyRecord(ctx,	ldapSearchBase, attribute.getID(),
										value.replaceFirst(Pattern.quote(env + "."), env + "-" + side + "."));
							}
							if (result == null) {
								logger.error("No results found for " + ldapSearchBase + " "	+ attribute.getID() + ":" + value);
							}
						}
						logger.info("Success!");
					}
				}
			}
			parser.close();
		} catch (InvalidAttributeFormatException e) {
			logger.error(e.toString());
		} finally {

		}
	}

	public SearchResult verifyRecord(DirContext ctx, String ldapSearchBase,
			String attributeId, String attributeValue) throws NamingException {

		String searchFilter = "(" + attributeId + "=" + attributeValue + ")";

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.OBJECT_SCOPE);

		NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase,
				searchFilter, searchControls);

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
