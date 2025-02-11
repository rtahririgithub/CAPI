package com.telus.provider.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.telus.eas.framework.config.LdapConfigurationManager;
import com.telus.eas.framework.config.LdapManager;
import com.telus.eas.framework.config.LdapReader;

public class ProviderLdapConfigurationManager extends LdapConfigurationManager {
	private static LdapManager INSTANCE;
	private static Object mutexObject = new Object();
	private static final String LDAPPATH_PROPERTIES_FILENAME = "/ldapPath.properties";
	private static final String LDAPPATH_SYSPROPERTIES_FILENAME = "/ldapSysProp.properties";
	
	public static LdapManager getInstance() {
		if (INSTANCE == null) {
			synchronized (mutexObject) {
				if (INSTANCE == null) {
					LdapReader ldapReader = new ProviderClassicLdapReader();
					Properties ldapPathProperties = new LdapProperties();				
					readProperties (ldapPathProperties, LDAPPATH_PROPERTIES_FILENAME);

					Properties ldapSystemProperties = new Properties();
					readProperties (ldapSystemProperties, LDAPPATH_SYSPROPERTIES_FILENAME);
					INSTANCE = new ProviderLdapConfigurationManager(ldapReader, ldapPathProperties, ldapSystemProperties);
				}
			}
		}

		return INSTANCE;
	}
	
	private ProviderLdapConfigurationManager(LdapReader ldapReader, Map ldapPathMap, Map systemPropertyMap) {
		super(ldapReader, ldapPathMap, systemPropertyMap);
	}
	
	private static void readProperties(Properties properties, String filename) {
		InputStream inputStream = null;
		ReadFileFromClassPath fileReaderHelper = new ReadFileFromClassPath();
		try {
			inputStream = fileReaderHelper.openFile(filename);
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load " + filename + ". ", e);
		} finally {
			closeInputStream(inputStream);
		}
	}
	
	private static void closeInputStream(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			}catch (IOException e) {
				
			}
		}
	}

}
