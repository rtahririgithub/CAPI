package com.telus.cmb.ldiftool;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
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

import com.telus.cmb.ldiftool.constants.EnvEnum;
import com.telus.cmb.ldiftool.utilities.LdifUtilities;

public class LdifShakedown {

	private static Logger logger = Logger.getLogger(LdifShakedown.class);

	private boolean useExternalSourceList = false;
	private boolean saveLdifLocally = true;
	private boolean suppressFlipper = true;
	private EnvEnum environment;

	public LdifShakedown(EnvEnum environment) {
		this.environment = environment;
	}
	
	public boolean isUseExternalSource() {
		return useExternalSourceList;
	}

	public void setUseExternalSource(boolean useExternalSource) {
		this.useExternalSourceList = useExternalSource;
	}

	public boolean isSaveLdifLocally() {
		return saveLdifLocally;
	}

	public void setSaveLdifLocally(boolean saveLdifLocally) {
		this.saveLdifLocally = saveLdifLocally;
	}

	public boolean isSuppressFlipper() {
		return suppressFlipper;
	}

	public void setSuppressFlipper(boolean supressFlipper) {
		this.suppressFlipper = supressFlipper;
	}

	public EnvEnum getEnvironment() {
		return environment;
	}

	public void setEnvironment(EnvEnum environment) {
		this.environment = environment;
	}

	public void shakedown(int releaseId) throws NamingException, IOException {
		
		LdifSourceExtractor extractor = useExternalSourceList ? new LocalLdifExtractor() : new RAPiDLdifExtractor(releaseId);
		List<String> buildFiles = extractor.getLdifBuildFiles(environment.getName(), saveLdifLocally);
		for (String buildFile : buildFiles) {
			logger.info("Shaking down ..." + buildFile);
			validateLdif(buildFile);
			logger.info(buildFile + " Completed!");
		}
	}

	private void validateLdif(String buildFile) throws NamingException, IOException {

		ApplicationContext context;
		String ldapSearchBase;

		try {
			context = new ClassPathXmlApplicationContext("application-context.xml");
			Resource resource = context.getResource(buildFile);
			LdifParser parser = new LdifParser(resource);
			parser.open();
			DirContext ctx = LdifUtilities.getDirContext(getEnvironment());

			while (parser.hasMoreRecords()) {
				LdapAttributes attr = parser.getRecord();
				LdapName ldapName = attr.getName();
				ldapSearchBase = environment.isFlipperEnabled() ? ldapName.toString() + "_" + environment.getFlipperSide() : ldapName.toString();
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
						result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), value);
						
						String resultMessage = "Success!";
						
						if ((result == null) && (suppressFlipper)) {
						    String updatedValue = value.replaceFirst(Pattern.quote(environment.getName() + "."), environment.getName() + "-" + environment.getFlipperSide() + ".");
						    result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), updatedValue);
						    if ( result == null ) {
						        if (environment.getAliasName() != null) {
						            updatedValue = value.replaceFirst(Pattern.quote(environment.getAliasName() + "."), environment.getAliasName() + "-" + environment.getFlipperSide() + ".");
						            result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), updatedValue);
						            if (result == null ) {
						               logger.error("No results found with alias flipper for " + ldapSearchBase + " " + attribute.getID() + ":" + updatedValue); 
						            }
						        }
						        else {
						            logger.error("No results found with flipper for " + ldapSearchBase + " " + attribute.getID() + ":" + updatedValue);
						        }
						    }
						}
						
						if (result == null) {
                            logger.error("No results found for " + ldapSearchBase + " " + attribute.getID() + ":[" + value + "]");
                        }
						
						logger.info("Success!");
					}
				}
			}

			parser.close();
			ctx.close();
		} catch (InvalidAttributeFormatException e) {
			logger.error(e.toString());
		}
	}

	/**
	 * LdifTest - 
	 *  INPUT:  first arg is a valid environment
	 *  		second arg is whether to use ldif-list.txt or RAPiD DB
	 *  		third arg is whether to save ldif locally before comparing to LDAP or maintain network connection while comparing
	 *  		fourth arg is whether to suppress Flipper error for downstream WS
	 *  OUTPUT: rolling and error files
	 */
	public static void main(String[] args) throws Throwable {
		
		if (args.length < 1) {
			logger.error("Choose an invalid environment as the first argument from " + EnvEnum.values().toString());
			return;
		}
		EnvEnum envEnum = LdifUtilities.parseEnvEnum(args[0]);
		if (envEnum == null) {
			logger.error(args[0] + " is an invalid environment, please choose from " + EnvEnum.values().toString());
			return;
		}
		
		LdifShakedown ldifShakedown = new LdifShakedown(envEnum);
		if (args.length > 1) {
			ldifShakedown.setUseExternalSource(Boolean.parseBoolean(args[1]));
		} else if (args.length > 2) {
			ldifShakedown.setSaveLdifLocally(Boolean.parseBoolean(args[2]));
		} else if (args.length > 3) {
			ldifShakedown.setSuppressFlipper(Boolean.parseBoolean(args[3]));			
		}

		//	50298 - 2017 Feb Wireless Major
		//	50299 - 2017 Apr Wireless Major
		//	50300 - 2017 Jul Wireless Major
		//	50370 - 2018 Jan Wireless Major
		//	50363 - 2018 Apr Wireless Major
		//  50421 - 2019 Feb Wireless Major
		logger.info("Shakedown Start!");
		logger.info( "UserExternal source " + ldifShakedown.getEnvironment().getFullName());
		logger.info( "UserExternal source " + ldifShakedown.isUseExternalSource());
		logger.info( "Save Ldif Locally " + ldifShakedown.isSaveLdifLocally());
		logger.info( "Suppress Flipper " + ldifShakedown.isSuppressFlipper());
		ldifShakedown.shakedown(50421);
		logger.info("Shakedown Complete!");
	}

}
