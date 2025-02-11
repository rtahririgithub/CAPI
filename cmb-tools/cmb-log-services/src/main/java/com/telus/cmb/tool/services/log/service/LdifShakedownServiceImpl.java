package com.telus.cmb.tool.services.log.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.ldap.core.LdapAttribute;
import org.springframework.ldap.core.LdapAttributes;
import org.springframework.ldap.ldif.InvalidAttributeFormatException;
import org.springframework.ldap.ldif.parser.LdifParser;
import org.springframework.stereotype.Service;

import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.dao.EmtoolsDao;
import com.telus.cmb.tool.services.log.domain.LdifSearchResult;
import com.telus.cmb.tool.services.log.domain.RAPiDRelease;
import com.telus.cmb.tool.services.log.ldif.exceptions.LdifDirectoryNotFoundException;
import com.telus.cmb.tool.services.log.ldif.exceptions.LdifException;
import com.telus.cmb.tool.services.log.ldif.exceptions.MultipleLdifMatchException;
import com.telus.cmb.tool.services.log.utils.LdifUtilities;

@Service
public class LdifShakedownServiceImpl implements LdifShakedownService {

	private static Logger logger = Logger.getLogger(LdifShakedownServiceImpl.class);

	@Autowired
	private EmtoolsDao emtoolsDao;

	@Value("${temp.root}")
	private String tempFolderPath;

	@Value("${temp.ldif.folder}")
	private String ldifFolderName;

	@Override
	public List<LdifSearchResult> shakedown(Environment environment, int releaseId) {
		return shakedown(environment, Collections.<String> emptyList(), releaseId);
	}

	@Override
	public List<LdifSearchResult> shakedown(Environment environment, List<String> ldifPaths) {
		return shakedown(environment, ldifPaths, -1);
	}

	@Override
	public List<RAPiDRelease> getReleases() {
		return emtoolsDao.getActiveReleases();
	}
	
	private List<LdifSearchResult> shakedown(Environment environment, List<String> ldifPaths, int releaseId) {
		
		List<LdifSearchResult> results = new ArrayList<>();
		try {			
			List<String> buildFiles = releaseId < 0 ? getLdifBuildFiles(environment, ldifPaths) : getLdifBuildFiles(environment, releaseId);
			for (String buildFile : buildFiles) {
				logger.info("Shaking down ..." + buildFile);
				results.addAll(validateLdif(environment, buildFile, true));
				logger.info(buildFile + " Completed!");
			}
			if (releaseId >= 0) {
				cleanupTempLdifs(buildFiles);
			}
		} catch (IOException ioe) {
			results.add(new LdifSearchResult("Couldn't download ldif files due to: " + ioe.getMessage(), null, null, null));
		} catch (NamingException ne) {
			logger.error(ne.getMessage());
			results.add(new LdifSearchResult("Couldn't reach LDAP due to: " + ne.getMessage(), null, null, null));
		}

		return results;
	}

	private void cleanupTempLdifs(List<String> buildFiles) {
		if (!buildFiles.isEmpty()) {
			String sampleFilePath = buildFiles.get(0);
			String filePath = sampleFilePath.substring(0, sampleFilePath.lastIndexOf("/"));
			File folder = new File(filePath);
			for (File file : folder.listFiles()) {
				file.delete();
			}
			folder.delete();
		}
	}
	
	private List<String> getLdifBuildFiles(Environment environment, int releaseId) throws IOException {

		List<String> ldifList = new ArrayList<>();
		String localPath = tempFolderPath + ldifFolderName + "/" + releaseId;
		File file = new File(localPath);
		file.mkdir();
		for (String ldifPath : emtoolsDao.getLdifpaths(releaseId)) {
			ldifList.add(LdifUtilities.saveLdifFile(environment, ldifPath, localPath));
		}

		return ldifList;
	}

	private List<String> getLdifBuildFiles(Environment environment, List<String> ldifPaths) {

		List<String> ldifList = new ArrayList<>();
		for (String ldifPath : ldifPaths) {
			ldifList.add(LdifUtilities.getLdifUrl(LdifUtilities.getLdapEnvName(environment), ldifPath.trim()));
		}
		return ldifList;
	}

	private List<LdifSearchResult> validateLdif(Environment environment, String buildFile, boolean suppressFlipper) throws NamingException, IOException {

		List<LdifSearchResult> results = new ArrayList<>();
		LdifParser parser = buildFile.startsWith("http") ? new LdifParser(new UrlResource(buildFile)) : new LdifParser(new FileSystemResource(buildFile));
		DirContext ctx = null;

		try {
			parser.open();
			ctx = LdifUtilities.getDirContext(environment.getLdap());
			String ldapSearchBase;
			while (parser.hasMoreRecords()) {
				LdapAttributes attr = parser.getRecord();
				LdapName ldapName = attr.getName();
				ldapSearchBase = environment.getLdap().isFlipper() ? ldapName.toString() + "_" + LdifUtilities.getLdapFlipperSide(environment) : ldapName.toString();
				logger.info("LDAP Search Base: " + ldapSearchBase);
				NamingEnumeration<Attribute> attributeList = attr.getAll();

				try {
					while (attributeList.hasMore()) {
						LdapAttribute attribute = (LdapAttribute) attributeList.next();
						logger.info("Attribute ID: " + attribute.getID());
						NamingEnumeration<?> valueList = attribute.getAll();
						while (valueList.hasMore()) {
							String value = LdifUtilities.getAttributeValue(valueList);
							results.addAll(validateEntry(environment, suppressFlipper, ctx, ldapSearchBase, attribute, value));
						}
					}
				} catch (LdifDirectoryNotFoundException e) {
					results.add(e.getResult());
				}
			}
		} catch (InvalidAttributeFormatException e) {
			logger.error(e.toString());
		} finally {
			parser.close();
			if (ctx != null) {
				ctx.close();
			}
		}

		return results;
	}

	/**
	 * Method to validate each result from the ldif file (on the ldap tree)
	 * TODO: May want to consider optimizing the way we retrieve the value from staging so we don't have to retry so many times
	 * 
	 * @param environment
	 * @param suppressFlipper
	 * @param ctx
	 * @param ldapSearchBase
	 * @param attribute
	 * @param value
	 * @return
	 * @throws NamingException
	 * @throws LdifException 
	 */
	private List<LdifSearchResult> validateEntry(Environment environment, boolean suppressFlipper, DirContext ctx, String ldapSearchBase, LdapAttribute attribute, String value)
			throws NamingException, LdifDirectoryNotFoundException {

		List<LdifSearchResult> results = new ArrayList<>();
		logger.info("Value Name: " + value);
		SearchResult result;
		try {
			result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), value);
			if (result == null && suppressFlipper && environment.getLdap().isFlipper()) {				
				result = retryValidationWithFlipperSide(environment, ctx, ldapSearchBase, attribute, value, results);
			}
			if (result == null) {
				logger.error("No results found for " + ldapSearchBase + " " + attribute.getID() + ":[" + value + "]");
				results.add(new LdifSearchResult("No results found", ldapSearchBase, attribute.getID(), value));
			}
		} catch (MultipleLdifMatchException e) {
			results.add(e.getResult());
		}
		
		return results;
	}

	/**
	 * Method to retry the validation with updated values with flipper side "on" in the values.  
	 * For example:
	 *     t3://amdocsapi-its04.tsl.telus.com:40000 -> t3://amdocsapi-its04-a.tsl.telus.com:40000
	 * 
	 * @param environment
	 * @param ctx
	 * @param ldapSearchBase
	 * @param attribute
	 * @param value
	 * @param results
	 * @return
	 * @throws NamingException
	 * @throws MultipleLdifMatchException
	 * @throws LdifDirectoryNotFoundException 
	 */
	private SearchResult retryValidationWithFlipperSide(Environment environment, DirContext ctx, String ldapSearchBase, LdapAttribute attribute, String value, List<LdifSearchResult> results)
			throws NamingException, LdifDirectoryNotFoundException, MultipleLdifMatchException {
		
		// First try with regular environment name
		String flipperSide = LdifUtilities.getLdapFlipperSide(environment);
		String updatedValue = LdifUtilities.getFlipperUpdatedValue(value, LdifUtilities.getLdapEnvName(environment), flipperSide);
		SearchResult result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), updatedValue);
		
		// Next try with alias environment name
		if (result == null && StringUtils.isNotBlank(environment.getAliasname())) {
			updatedValue = LdifUtilities.getFlipperUpdatedValue(value, environment.getAliasname(), flipperSide);
			result = LdifUtilities.verifyRecord(ctx, ldapSearchBase, attribute.getID(), updatedValue);
		}

		if (result == null) {
			logger.error("No results found with updated flipper values for " + ldapSearchBase + " " + attribute.getID() + ":" + updatedValue);
			results.add(new LdifSearchResult("No results found with updated flipper values", ldapSearchBase, attribute.getID(), updatedValue));
		}
		
		return result;
	}
}
